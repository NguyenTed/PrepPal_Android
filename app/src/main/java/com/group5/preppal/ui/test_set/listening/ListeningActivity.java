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
import com.group5.preppal.data.model.test.listening.ListeningPart;
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

    private QuestionGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        tvPartLabel = findViewById(R.id.partLabelTextView);
        tvTimer = findViewById(R.id.tvListeningTimer);
        btnPrevious = findViewById(R.id.btnPreviousPart);
        btnNextOrSubmit = findViewById(R.id.btnNextPart);
        groupRecyclerView = findViewById(R.id.questionGroupRecyclerView);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new QuestionGroupAdapter();
        adapter.setUserAnswers(viewModel != null ? viewModel.getUserAnswers() : new HashMap<>());
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

        // Start timer
        viewModel.startTimer(60 * 60 * 1000); // 60 mins
        viewModel.getTimeLeft().observe(this, time -> {
            tvTimer.setText(time);
            if ("00:00".equals(time)) {
                viewModel.setTimeUp(true);
                adapter.setTimeUp(true);
                adapter.disableAllInputs();
                submitAnswers(); // auto submit
            }
        });

        // Observe part switching
        viewModel.getCurrentPart().observe(this, partNum -> {
            tvPartLabel.setText("Part " + partNum);
            btnPrevious.setVisibility(partNum == 1 ? View.INVISIBLE : View.VISIBLE);
            btnNextOrSubmit.setText(partNum == 4 ? "Submit" : "Next");

            ListeningPart part = viewModel.getCurrentPartData();
            if (part != null) {
                adapter.setTimeUp(viewModel.isTimeUp());
                adapter.submitList(part.getListeningQuestionGroups());
                playAudio(part.getAudioUrl());
            }
        });

        // Navigation
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
        Date submittedAt = new Date();
        viewModel.submitListeningAttempt(
                submittedAt,
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