package com.group5.preppal.ui.quiz.writing_quiz;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WritingQuizActivity extends AppCompatActivity {
    private TextView tvTitle, tvTaskType;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_quiz);

        tvTitle = findViewById(R.id.tvTitle);
        tvTaskType = findViewById(R.id.tvTaskType);
        btnBack = findViewById(R.id.backButton);

        btnBack.setOnClickListener(v -> finish());
    }

    // Cho phép Fragment cập nhật tiêu đề
    public void updateTitle(String title, String taskType) {
        tvTitle.setText(title);
        tvTaskType.setText(taskType);
    }
}
