package com.group5.preppal.ui.test_set.listening;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.group5.preppal.data.model.test.listening.ListeningGrader;
import com.group5.preppal.data.model.test.listening.ListeningPart;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.viewmodel.ListeningViewModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListeningActivity extends AppCompatActivity {

    private ListeningViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private TextView partLabel, tvTimer;
    private RecyclerView groupRecyclerView;
    private CountDownTimer countDownTimer;
    private QuestionGroupAdapter adapter;

    private Button btnPreviousPart, btnNextPart;
    private int currentPartIndex = 0; // 0 = Part 1

    private ListeningSection section;
    private boolean isTimeUp = false;
    private final Map<Integer, String> userAnswers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        partLabel = findViewById(R.id.partLabelTextView);
        tvTimer = findViewById(R.id.tvListeningTimer);
        btnPreviousPart = findViewById(R.id.btnPreviousPart);
        btnNextPart = findViewById(R.id.btnNextPart);

        startTimer(60 * 60 * 1000);

        groupRecyclerView = findViewById(R.id.questionGroupRecyclerView);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionGroupAdapter();
        adapter.setTimeUp(false);
        adapter.setUserAnswers(userAnswers);
        groupRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ListeningViewModel.class);
        section = getIntent().getParcelableExtra("listeningSection");

        viewModel.getCurrentPart().observe(this, part -> {
            if (part != null) {
                partLabel.setText("Part " + (currentPartIndex + 1));
                adapter.submitList(part.getQuestionGroups());
                playAudio(part.getAudioUrl());
                updateNavButtons();
            }
        });

        btnPreviousPart.setOnClickListener(v -> {
            if (currentPartIndex > 0) {
                currentPartIndex--;
                switchToPart(currentPartIndex);
            }
        });

        btnNextPart.setOnClickListener(v -> {
            if (currentPartIndex < 3) {
                currentPartIndex++;
                switchToPart(currentPartIndex);
            } else {
                submitTest();
            }
        });

        switchToPart(currentPartIndex); // load part 1 by default
    }

    private void switchToPart(int partIndex) {
        ListeningPart part = getPartByIndex(partIndex);
        if (part != null) {
            viewModel.setPart(part);
        }
    }

    private ListeningPart getPartByIndex(int index) {
        switch (index) {
            case 0: return section.getPart1();
            case 1: return section.getPart2();
            case 2: return section.getPart3();
            case 3: return section.getPart4();
            default: return null;
        }
    }

    private void updateNavButtons() {
        btnPreviousPart.setVisibility(currentPartIndex == 0 ? View.INVISIBLE : View.VISIBLE);
        btnNextPart.setText(currentPartIndex == 3 ? "Submit" : "Next");
    }

    private void playAudio(String audioUrl) {
        if (mediaPlayer != null) mediaPlayer.release();

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTimer(long durationMillis) {
        countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                tvTimer.setText(time);
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                Toast.makeText(ListeningActivity.this, "Time's up!", Toast.LENGTH_LONG).show();
                isTimeUp = true;
                adapter.setTimeUp(true);
                adapter.disableAllInputs();
            }
        }.start();
    }

    private void submitTest() {
        if (section == null) return;

        String testId = getIntent().getStringExtra("testId");
        String testSetId = getIntent().getStringExtra("testSetId");

        viewModel.submitListeningAttempt(
                testId,
                testSetId,
                section,
                userAnswers,
                unused -> {
                    Toast.makeText(this, "Attempt saved!", Toast.LENGTH_SHORT).show();
                    finish();
                },
                e -> {
                    Log.e("LISTENING_SUBMIT", "Failed to save attempt", e);
                    Toast.makeText(this, "Failed to save attempt", Toast.LENGTH_SHORT).show();
                }
        );
    }


    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}

