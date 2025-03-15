package com.group5.preppal.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Course;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private CourseViewModel courseViewModel;
    private List<Course> courseList = new ArrayList<>();
    private ImageButton backButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerViewCourse);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter(this, courseList, course -> {
            Toast.makeText(this, "Enrolled in: " + course.getEntryLevel(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(courseAdapter);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        courseViewModel.getStudentCourses().observe(this, courses -> {
            if (courses != null) {
                courseList.clear();
                courseList.addAll(courses);
                courseAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Lỗi: Không có dữ liệu từ Firestore!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
