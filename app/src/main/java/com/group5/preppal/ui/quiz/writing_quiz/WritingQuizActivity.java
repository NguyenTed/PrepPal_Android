package com.group5.preppal.ui.quiz.writing_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;
import com.group5.preppal.ui.course.CourseDetailActivity;

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

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra("courseId", getIntent().getStringExtra("courseId"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    // Cho phép Fragment cập nhật tiêu đề
    public void updateTitle(String title, String taskType) {
        tvTitle.setText(title);
        tvTaskType.setText(taskType);
    }
}
