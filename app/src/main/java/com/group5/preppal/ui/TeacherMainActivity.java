package com.group5.preppal.ui;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.ui.auth.LoginActivity;
import com.group5.preppal.ui.course.CourseAdapter;
import com.group5.preppal.ui.course.teacher.TeacherCourseAdapter;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.ui.profile.teacher.TeacherProfileActivity;
import com.group5.preppal.viewmodel.AuthViewModel;
import com.group5.preppal.viewmodel.CourseViewModel;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TeacherMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TeacherCourseAdapter adapter;
    private ImageView iconHome;
    private TextView tvHome;
    private LinearLayout navHome, navProfile, navLogOut;
    private CourseViewModel courseViewModel;
    private AuthViewModel authViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teacher_drawer_base);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        ViewGroup contentFrame = findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_teacher_main, contentFrame, true);

        recyclerView = findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getTeacherCourses().observe(this, courses -> {
            adapter = new TeacherCourseAdapter(courses);
            recyclerView.setAdapter(adapter);
        });

        iconHome = findViewById(R.id.iconHome);
        navHome = findViewById(R.id.navHome);
        navProfile =  findViewById( R.id.navProfile);
        navLogOut = findViewById(R.id.navLogOut);
        tvHome = findViewById(R.id.tvHome);

        navHome.setBackgroundColor(Color.parseColor("#FFFFFF"));
        iconHome.setImageResource(R.drawable.ic_home_black);
        tvHome.setTextColor(Color.parseColor("#000000"));


        findViewById(R.id.btnToggleDrawer).setOnClickListener(v -> {
            DrawerLayout drawer = findViewById(R.id.drawerLayout);
            drawer.openDrawer(GravityCompat.START);
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherProfileActivity.class));
        });

        navLogOut.setOnClickListener(v -> {
            handleLogOut();
        });
    }

    private void handleLogOut() {
        authViewModel.signOut();
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(TeacherMainActivity.this, LoginActivity.class));
        finish();
    }
}

