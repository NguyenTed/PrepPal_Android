package com.group5.preppal.ui.test_set.listening;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.viewmodel.ListeningViewModel;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

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
        adapter = new ListeningQuestionGroupAdapter();
        adapter.setUserAnswers(new HashMap<>());

        groupRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ListeningViewModel.class);

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

                // Play audio
                playAudio(part.getAudioUrl());
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

    private void playAudio(String audioUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to play audio", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Listening submitted! 🎧", Toast.LENGTH_SHORT).show();
                    finish(); // or move to result screen
                },
                e -> Toast.makeText(this, "Submit failed", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) mediaPlayer.release();
        super.onDestroy();
    }
}
