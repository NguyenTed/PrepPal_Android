package com.group5.preppal.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.Course;
import com.group5.preppal.viewmodel.CourseViewModel;

import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseDetailActivity extends AppCompatActivity {
    private String courseId;
    private CourseViewModel courseViewModel;
    private TextView courseName, courseLevel, txtIntroduction;
    private ImageButton backBtn;

    private RecyclerView recyclerViewSections;
    private SectionAdapter sectionAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseId = getIntent().getStringExtra("courseId");
        if (courseId == null) {
            Toast.makeText(this, "Course ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        courseName = findViewById(R.id.courseName);
        courseLevel = findViewById(R.id.courseLevel);
        txtIntroduction = findViewById(R.id.txtIntroduction);
        recyclerViewSections = findViewById(R.id.recyclerViewSections);
        recyclerViewSections.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CourseListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.fetchCourseById(courseId);

        courseViewModel.getSelectedCourse().observe(this, course -> {
            if (course != null) {
                courseName.setText(course.getName());
                courseLevel.setText("Entry Level: " + course.getEntryLevel() + " - " + (course.getEntryLevel() + 0.5) + " / " + "Target Level: " + (course.getTargetLevel() == 6.5 ? course.getTargetLevel() +  " - " + "7.0+" : course.getTargetLevel() + "+") );
                txtIntroduction.setText(course.getIntroduction());
            }

            List<Map<String, Object>> sections = course.getSections();
            Log.d("CourseDetailActivity", "Section length: " + sections.size());
            if (sections != null) {
                sectionAdapter = new SectionAdapter(sections);
                recyclerViewSections.setAdapter(sectionAdapter);
            }
        });
    }


}