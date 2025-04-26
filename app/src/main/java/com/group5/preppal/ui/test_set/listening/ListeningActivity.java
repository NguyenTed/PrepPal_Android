package com.group5.preppal.ui.test_set.listening;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.viewmodel.ListeningViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.group5.preppal.utils.ShowToast;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListeningActivity extends AppCompatActivity {

    private ListeningViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private TextView tvPartLabel, tvTimer;
    private RecyclerView groupRecyclerView;
    private Button btnPrevious, btnNextOrSubmit;
    private View[] stepViews;
    private ListeningQuestionGroupAdapter adapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Exit")
                    .setMessage("Are you sure you want to end the test?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        setResult(RESULT_OK);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        // View bindings
        tvPartLabel = findViewById(R.id.partLabelTextView);
        tvTimer = findViewById(R.id.tvListeningTimer);
        btnPrevious = findViewById(R.id.btnPreviousPart);
        btnNextOrSubmit = findViewById(R.id.btnNextPart);
        groupRecyclerView = findViewById(R.id.questionGroupRecyclerView);
        stepViews = new View[]{
                findViewById(R.id.step1),
                findViewById(R.id.step2),
                findViewById(R.id.step3),
                findViewById(R.id.step4)
        };

        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = new ViewModelProvider(this).get(ListeningViewModel.class);
        adapter = new ListeningQuestionGroupAdapter();
        adapter.setUserAnswers(viewModel != null ? viewModel.getUserAnswers() : new HashMap<>());

        groupRecyclerView.setAdapter(adapter);

        // Get data from intent
        String testId = getIntent().getStringExtra("testId");
        String testSetId = getIntent().getStringExtra("testSetId");
        Date startedAt = new Date();

        viewModel.setMeta(testId, testSetId, startedAt);

        ListeningSection listeningSection = getIntent().getParcelableExtra("listeningSection");
        if (listeningSection == null) {
            Toast.makeText(this, "Listening section is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.setListeningSection(listeningSection);

        // 👇 Start continuous playback of 4 parts
        List<String> audioUrls = new ArrayList<>();
        audioUrls.add(listeningSection.getPart1().getAudioUrl());
        audioUrls.add(listeningSection.getPart2().getAudioUrl());
        audioUrls.add(listeningSection.getPart3().getAudioUrl());
        audioUrls.add(listeningSection.getPart4().getAudioUrl());

        playAllAudiosInOrder(audioUrls);


        // Start countdown timer
        viewModel.startTimer(60 * 60 * 1000); // 60 minutes
        viewModel.getTimeLeft().observe(this, time -> {
            tvTimer.setText(time);
            if ("00:00".equals(time)) {
                viewModel.setTimeUp(true);
                adapter.setTimeUp(true);
                submitAnswers(); // auto submit
            }
        });

        // Observe current part
        viewModel.getCurrentPartData().observe(this, part -> {
            if (part != null) {
                int partNum = viewModel.getCurrentPart().getValue(); // 1 to 4
                tvPartLabel.setText("Part " + partNum);

                // Update progress steps
                for (int i = 0; i < stepViews.length; i++) {
                    if (i < partNum) {
                        stepViews[i].setBackgroundResource(R.drawable.progress_step_active);
                    } else {
                        stepViews[i].setBackgroundResource(R.drawable.progress_step_inactive);
                    }
                }

                // Show/hide buttons
                btnPrevious.setVisibility(partNum == 1 ? View.INVISIBLE : View.VISIBLE);
                btnNextOrSubmit.setText(partNum == 4 ? "Submit" : "Next →");

                // Update questions
                adapter.setTimeUp(viewModel.isTimeUp());
                adapter.submitList(part.getListeningQuestionGroups());
            }
        });

        // Button navigation
        btnPrevious.setOnClickListener(v -> viewModel.goToPreviousPart());
        btnNextOrSubmit.setOnClickListener(v -> {
            if (viewModel.getCurrentPart().getValue() == 4) {
                submitAnswers();
            } else {
                viewModel.goToNextPart();
            }
        });
    }

    private void playAllAudiosInOrder(List<String> audioUrls) {
        if (audioUrls == null || audioUrls.isEmpty()) return;

        playNextAudio(audioUrls, 0);
    }

    private void playNextAudio(List<String> audioUrls, int index) {
        if (index >= audioUrls.size()) {
            Log.d("ListeningActivity", "✅ Finished all audios.");
            return; // Done all parts
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrls.get(index));
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(mp -> {
                playNextAudio(audioUrls, index + 1);
            });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to play audio part " + (index + 1), Toast.LENGTH_SHORT).show();
            // Try next audio anyway
            playNextAudio(audioUrls, index + 1);
        }
    }


    private void submitAnswers() {
        Log.d("ListeningActivity", "User answers: " + viewModel.getUserAnswers());

        String testId = getIntent().getStringExtra("testId");
        String testSetId = getIntent().getStringExtra("testSetId");

        viewModel.submitListeningAttempt(
                testId,
                testSetId,
                new Date(),
                new Date(),
                unused -> {
                    ShowToast.show(this, "Listening submitted! 🎧", ShowToast.ToastType.SUCCESS);
                    finish(); // or move to result screen
                },

                e ->
                        ShowToast.show(this, "Submit failed", ShowToast.ToastType.ERROR)
        );
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) mediaPlayer.release();
        super.onDestroy();
    }
}
