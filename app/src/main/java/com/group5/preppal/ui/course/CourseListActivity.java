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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Course;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.ui.test_set.TestSetListActivity;
import com.group5.preppal.ui.vocabulary.TopicActivity;
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
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        bottomNav = findViewById(R.id.bottom_nav);

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

        bottomNav.setSelectedItemId(R.id.nav_courses);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_courses) {
                return true;
            } else if (itemId == R.id.nav_test_set) {
                startActivity(new Intent(this, TestSetListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_vocab) {
                startActivity(new Intent(this, TopicActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}
