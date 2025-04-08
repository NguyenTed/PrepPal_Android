package com.group5.preppal.ui.test_set.reading;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.reading.ReadingSection;
import com.group5.preppal.viewmodel.ReadingViewModel;

import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReadingActivity extends AppCompatActivity {

    private ReadingViewModel viewModel;
    private TextView tvPartLabel, tvTimer;
    private ViewPager2 readingViewPager;
    private Button btnPrevious, btnNextOrSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        tvPartLabel = findViewById(R.id.tvReadingPartLabel);
        readingViewPager = findViewById(R.id.readingViewPager);
        btnPrevious = findViewById(R.id.btnPreviousPassage);
        btnNextOrSubmit = findViewById(R.id.btnNextOrSubmit);
        tvTimer = findViewById(R.id.tvReadingTimer); // Add this to layout

        viewModel = new ViewModelProvider(this).get(ReadingViewModel.class);

        viewModel.getTimeLeft().observe(this, time -> {
            tvTimer.setText(time);

            if ("00:00".equals(time)) {
                Toast.makeText(this, "Time’s up!", Toast.LENGTH_LONG).show();
                viewModel.setTimeUp(true);
                submitAnswers(); // or disable buttons only if you want manual submission
            }
        });

        viewModel.startTimer(60 * 60 * 1000); // 60 mins

        // Get data from Intent
        ReadingSection readingSection = getIntent().getParcelableExtra("readingSection");
        if (readingSection == null) {
            Toast.makeText(this, "Reading section is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.setReadingSection(readingSection);

        // Observe current passage
        viewModel.getCurrentPassage().observe(this, passage -> {
            if (passage != null) {
                ReadingPagerAdapter pagerAdapter = new ReadingPagerAdapter(
                        passage,
                        viewModel.getUserAnswers(),
                        viewModel.isTimeUp()
                );
                readingViewPager.setAdapter(pagerAdapter);
            }
        });

        // Observe current part number
        viewModel.getCurrentPart().observe(this, partNum -> {
            tvPartLabel.setText("Passage " + partNum);
            btnPrevious.setVisibility(partNum == 1 ? View.INVISIBLE : View.VISIBLE);
            btnNextOrSubmit.setText(partNum == 3 ? "Submit" : "Next");
        });

        btnPrevious.setOnClickListener(v -> viewModel.goToPreviousPart());

        btnNextOrSubmit.setOnClickListener(v -> {
            if (viewModel.getCurrentPart().getValue() == 3) {
                submitAnswers();
            } else {
                viewModel.goToNextPart();
            }
        });
    }

    private void submitAnswers() {
        Log.d("ReadingActivity", "User answers: " + viewModel.getUserAnswers());

        String testId = getIntent().getStringExtra("testId");
        String testSetId = getIntent().getStringExtra("testSetId");

        viewModel.submitReadingAttempt(
                testId,
                testSetId,
                new Date(),
                new Date(),
                unused -> {
                    Toast.makeText(this, "Submitted! 🎉", Toast.LENGTH_SHORT).show();
                    finish(); // or go to results
                },
                error -> {
                    Toast.makeText(this, "Submit failed: ", Toast.LENGTH_SHORT).show();
                }
        );
    }
}

