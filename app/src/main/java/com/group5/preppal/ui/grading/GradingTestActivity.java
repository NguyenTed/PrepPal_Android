package com.group5.preppal.ui.grading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.TestItem;
import com.group5.preppal.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GradingTestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private TextView emptyView;
    private ImageButton backButton;  // Nút Back
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading_test);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        recyclerView = findViewById(R.id.recyclerViewTestList);
        emptyView = findViewById(R.id.emptyView);
        backButton = findViewById(R.id.backButton);  // Lấy nút Back từ layout

        // Xử lý khi nhấn nút Back
        backButton.setOnClickListener(v -> finish());

        String courseId = getIntent().getStringExtra("courseId");
        courseViewModel.fetchCourseById(courseId);
        courseViewModel.getSelectedCourse().observe(this, course -> {
            List<Map<String, Object>> writingList = course.extractWritingQuizzes();
            if (writingList.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new TestAdapter(writingList, this::onTestItemClick);
                recyclerView.setAdapter(adapter);
            }
        });


    }

    private void onTestItemClick(Map<String, Object> item) {
        Intent intent = new Intent(this, WritingAnswerListActivity.class);
//        intent.putExtra("testType",  item.get("type").toString());
//        intent.putExtra("topic", item.get("name").toString());
        intent.putExtra("taskId", item.get("id").toString());
        intent.putExtra("courseId", getIntent().getStringExtra("courseId"));
        startActivity(intent);
    }
}
