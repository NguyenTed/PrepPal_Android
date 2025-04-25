package com.group5.preppal.ui.test_set;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.preppal.R;
import com.group5.preppal.data.model.test.SkillAttempt;
import com.group5.preppal.data.model.test.TestAttempt;
import com.group5.preppal.data.model.test.listening.ListeningAttempt;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.data.model.test.reading.ReadingAttempt;
import com.group5.preppal.data.model.test.reading.ReadingSection;
import com.group5.preppal.data.model.test.speaking.SpeakingSection;
import com.group5.preppal.data.model.test.writing.WritingSection;
import com.group5.preppal.ui.test_set.listening.ListeningActivity;
import com.group5.preppal.ui.test_set.listening.ListeningReviewActivity;
import com.group5.preppal.ui.test_set.reading.ReadingActivity;
import com.group5.preppal.ui.test_set.reading.ReadingReviewActivity;
import com.group5.preppal.ui.test_set.speaking.SpeakingActivity;
import com.group5.preppal.ui.test_set.writing.WritingActivity;
import com.group5.preppal.viewmodel.TestDetailViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TestDetailActivity extends AppCompatActivity {

    private TestDetailViewModel viewModel;

    private TextView tvTestTitle,scoreOverall,scoreListening,scoreReading,scoreWriting,scoreSpeaking;
    private Button btnRetakeListening, btnStartReading, btnStartWriting, btnStartSpeaking;

    private LinearLayout listeningAttemptsLayout, readingAttemptsLayout, writingAttemptsLayout, speakingAttemptsLayout;

    private String testId, testSetId, testName;
    private ListeningSection listeningSection;
    private ReadingSection readingSection;
    private WritingSection writingSection;
    private SpeakingSection speakingSection;
    private ImageView btnBack;
    private final OnAttemptClickListener attemptClickListener = new OnAttemptClickListener() {
        @Override
        public void onListeningAttemptClick(ListeningAttempt attempt, ListeningSection section) {
            Intent intent = new Intent(TestDetailActivity.this, ListeningReviewActivity.class);
            intent.putExtra("listeningSection", section);
            intent.putExtra("attempt", attempt);
            startActivity(intent);
        }

        @Override
        public void onReadingAttemptClick(ReadingAttempt attempt, ReadingSection section) {
            Intent intent = new Intent(TestDetailActivity.this, ReadingReviewActivity.class);
            intent.putExtra("readingSection", section);
            intent.putExtra("attempt", attempt);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);

        // Bind Views
        tvTestTitle = findViewById(R.id.testTitleTextView);
        btnRetakeListening = findViewById(R.id.btnRetakeListening);
        btnStartReading = findViewById(R.id.btnStartReading);
        btnStartWriting = findViewById(R.id.btnStartWriting);
        btnStartSpeaking = findViewById(R.id.btnStartSpeaking);

        listeningAttemptsLayout = findViewById(R.id.listeningAttemptsLayout);
        readingAttemptsLayout = findViewById(R.id.readingAttemptsLayout);
        writingAttemptsLayout = findViewById(R.id.writingAttemptsLayout);
        speakingAttemptsLayout = findViewById(R.id.speakingAttemptsLayout);

        // Get intent data
        testId = getIntent().getStringExtra("testId");
        testSetId = getIntent().getStringExtra("testSetId");
        testName = getIntent().getStringExtra("testName");
        listeningSection = getIntent().getParcelableExtra("listeningSection");
        readingSection = getIntent().getParcelableExtra("readingSection");
        writingSection = getIntent().getParcelableExtra("writingSection");
        speakingSection = getIntent().getParcelableExtra("speakingSection");

        tvTestTitle.setText(testName);

        scoreOverall = findViewById(R.id.scoreOverall);
        scoreListening = findViewById(R.id.scoreListening);
        scoreReading = findViewById(R.id.scoreReading);
        scoreWriting = findViewById(R.id.scoreWriting);
        scoreSpeaking = findViewById(R.id.scoreSpeaking);


        // ViewModel
        viewModel = new ViewModelProvider(this).get(TestDetailViewModel.class);

        viewModel.loadListeningAttempts(testId);
        viewModel.loadReadingAttempts(testId);

        observeAttempts();
        setupButtonActions();
        updateOverallScore();
        //btnBack
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
    }

    private void observeAttempts() {
        viewModel.getListeningAttempts().observe(this, attempts -> {
            updateSkillUI(attempts, listeningAttemptsLayout, btnRetakeListening, "listening");
            if (attempts != null && !attempts.isEmpty()) {
                SkillAttempt latestAttempt = attempts.get(0);
                String lScore = latestAttempt.getBandScore() != 0.0f
                        ? String.format(Locale.US, "%.1f", latestAttempt.getBandScore())
                        : "0.0";
                scoreListening.setText(lScore);
            } else {
                scoreListening.setText("0.0");
            }
        });

        viewModel.getReadingAttempts().observe(this, attempts -> {
            updateSkillUI(attempts, readingAttemptsLayout, btnStartReading, "reading");
            if (attempts != null && !attempts.isEmpty()) {
                SkillAttempt latestAttempt = attempts.get(0);
                String lScore = latestAttempt.getBandScore() != 0.0f
                        ? String.format(Locale.US, "%.1f", latestAttempt.getBandScore())
                        : "0.0";
                scoreReading.setText(lScore);
            } else {
                scoreReading.setText("0.0");
            }
        });

        // Writing + Speaking: Not yet implemented — default to "Start"
        btnStartWriting.setText("Start");
        btnStartSpeaking.setText("Start");
    }

    @SuppressLint("SetTextI18n")
    private <T extends SkillAttempt> void updateSkillUI(
            List<T> attempts,
            LinearLayout container,
            Button button,
            String skillType
    ) {
        container.removeAllViews();

        if (attempts == null || attempts.isEmpty()) {
            button.setText("Start");
            return;
        }

        button.setText("Retake");

        int attemptNumber = attempts.size();
        for (T attempt : attempts) {

            View view = LayoutInflater.from(this).inflate(R.layout.item_attempt, container, false);

            TextView tvAttemptLabel = view.findViewById(R.id.tvAttemptLabel);
            TextView tvSubmittedTime = view.findViewById(R.id.tvSubmittedTime);
            TextView tvScore = view.findViewById(R.id.tvScore);

            tvAttemptLabel.setText("Attempt " + attemptNumber--);
            tvSubmittedTime.setText("Submitted on " +
                    new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US).format(attempt.getSubmittedAt()));
            tvScore.setText(String.format(Locale.US, "%.1f", attempt.getBandScore()));

            switch (skillType) {
                case "listening":
                    view.setOnClickListener(v -> attemptClickListener.onListeningAttemptClick(
                            (ListeningAttempt) attempt, listeningSection));
                    break;
                case "reading":
                    view.setOnClickListener(v -> attemptClickListener.onReadingAttemptClick(
                            (ReadingAttempt) attempt, readingSection));
                    break;
            }

            container.addView(view);
        }
    }

    private void setupButtonActions() {
        btnRetakeListening.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListeningActivity.class);
            intent.putExtra("testId", testId);
            intent.putExtra("testSetId", testSetId);
            intent.putExtra("testName", testName);
            intent.putExtra("listeningSection", listeningSection);
            startActivity(intent);
        });

        btnStartReading.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReadingActivity.class);
            intent.putExtra("testId", testId);
            intent.putExtra("testSetId", testSetId);
            intent.putExtra("testName", testName);
            intent.putExtra("readingSection", readingSection);
            startActivity(intent);
        });

        btnStartWriting.setOnClickListener(v -> {
            Intent intent = new Intent(this, WritingActivity.class);
            intent.putExtra("testId", testId);
            intent.putExtra("testSetId", testSetId);
            intent.putExtra("testName", testName);
            intent.putExtra("task1", writingSection.getTask1());
            intent.putExtra("task2", writingSection.getTask2());
            startActivity(intent);
        });

        btnStartSpeaking.setOnClickListener(v -> {
            Intent intent = new Intent(this, SpeakingActivity.class);
            intent.putExtra("testName", testName);
            intent.putStringArrayListExtra("part1", new ArrayList<>(speakingSection.getPart1()));
            intent.putExtra("part2", speakingSection.getPart2());
            intent.putStringArrayListExtra("part3", new ArrayList<>(speakingSection.getPart3()));
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload the attempts each time we come back to this screen
        viewModel.loadListeningAttempts(testId);
        viewModel.loadReadingAttempts(testId);
        // Later: viewModel.loadWritingAttempts(testId);
        // Later: viewModel.loadSpeakingAttempts(testId);
    }
    private void updateOverallScore() {
        float total = 0;
        int count = 0;

        for (TextView tv : new TextView[]{scoreListening, scoreReading, scoreWriting, scoreSpeaking}) {
            try {
                total += Float.parseFloat(tv.getText().toString());
                count++;
            } catch (NumberFormatException e) {
                // Bỏ qua nếu chưa có điểm
            }
        }

        if (count > 0) {
            float avg = total / count;
            scoreOverall.setText(String.format(Locale.US, "%.1f", avg));
        } else {
            scoreOverall.setText("0.0");
        }
    }
}



