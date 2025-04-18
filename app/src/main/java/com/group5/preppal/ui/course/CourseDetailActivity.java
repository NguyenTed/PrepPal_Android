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

import com.google.firebase.auth.FirebaseAuth;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Course;
import com.group5.preppal.viewmodel.CourseViewModel;
import com.group5.preppal.viewmodel.StudentViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseDetailActivity extends AppCompatActivity {
    private String courseId;
    private CourseViewModel courseViewModel;
    private TextView courseName, courseLevel, txtIntroduction;
    private ImageButton backBtn;
    private RecyclerView recyclerViewSections;
    private SectionAdapter sectionAdapter;
    private StudentViewModel studentViewModel;

    @Inject
    FirebaseAuth firebaseAuth;

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
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        String studentId = firebaseAuth.getCurrentUser().getUid();
        studentViewModel.getStudentById(studentId).observe(this, student -> {
            if (student == null) return;
            List<String> finishedLessons = student.getFinishedLessons();
            courseViewModel.fetchCourseById(courseId);
            courseViewModel.getSelectedCourse().observe(this, course -> {
                if (course != null) {
                    courseName.setText(course.getName());
                    courseLevel.setText("Entry Level: " + course.getEntryLevel() + " - " + (course.getEntryLevel() + 0.5) +
                            " / Target Level: " + (course.getTargetLevel() == 6.5 ? "6.5 - 7.0+" : course.getTargetLevel() + "+"));
                    txtIntroduction.setText(course.getIntroduction());

                    List<Map<String, Object>> sections = course.getSections();
                    if (sections != null) {
                        sectionAdapter = new SectionAdapter(sections, this, courseId, firebaseAuth, this, finishedLessons, studentViewModel);
                        recyclerViewSections.setAdapter(sectionAdapter);
                    }
                }
            });
        });
    }


}