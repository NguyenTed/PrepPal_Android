package com.group5.preppal.ui.test_set.writing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.group5.preppal.R;
import com.group5.preppal.data.model.test.writing.WritingTask;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WritingActivity extends AppCompatActivity {

    private TextView title, instruction, taskText, descriptionText;
    private ImageView chartImage;
    private Button btnTask1, btnTask2;

    private WritingTask task1, task2;
    private String testName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        title = findViewById(R.id.tvWritingTitle);
        instruction = findViewById(R.id.tvInstruction);
        taskText = findViewById(R.id.tvTask);
        descriptionText = findViewById(R.id.tvDescription);
        chartImage = findViewById(R.id.imgChart);

        btnTask1 = findViewById(R.id.btnTask1);
        btnTask2 = findViewById(R.id.btnTask2);

        testName = getIntent().getStringExtra("testName");
        WritingTask task1 = getIntent().getParcelableExtra("task1");
        WritingTask task2 = getIntent().getParcelableExtra("task2");

        title.setText(testName);

        btnTask1.setOnClickListener(v -> showTask(task1, 1));
        btnTask2.setOnClickListener(v -> showTask(task2, 2));

        showTask(task1, 1); // Show Task 1 by default
    }

    private void showTask(WritingTask task, int taskNumber) {
        instruction.setText(taskNumber == 1
                ? "You should spend about 20 minutes on this task."
                : "You should spend about 40 minutes on this task.");

        taskText.setText(task.getTask());
        descriptionText.setText(task.getDescription());

        if (task.getImg_url() != null && !task.getImg_url().isEmpty()) {
            chartImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(task.getImg_url()).into(chartImage);
        } else {
            chartImage.setVisibility(View.GONE);
        }

        // Visually highlight the selected tab
        btnTask1.setBackgroundResource(taskNumber == 1 ? R.drawable.tab_selected : R.drawable.tab_unselected);
        btnTask2.setBackgroundResource(taskNumber == 2 ? R.drawable.tab_selected : R.drawable.tab_unselected);
    }
}

