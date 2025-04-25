package com.group5.preppal.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.User;
import com.group5.preppal.ui.course.CourseListActivity;
import com.group5.preppal.ui.course_payment.CoursePaymentActivity;
import com.group5.preppal.ui.dictionary.DictionaryActivity;
import com.group5.preppal.ui.profile.CourseInfoActivity;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.ui.test_set.TestListActivity;
import com.group5.preppal.ui.test_set.TestSetListActivity;
import com.group5.preppal.ui.vocabulary.TopicActivity;
import com.group5.preppal.viewmodel.StudentViewModel;
import com.group5.preppal.viewmodel.UserViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private UserViewModel viewModel;
    private TextView txtGreetingUser, txtContact, txtInteractCourseButton;
    private LinearLayout myCourseBtn, viewMoreBtn, testSetBtn, vocabBtn, btnInteractCourse;
    private StudentViewModel studentViewModel;
    BottomNavigationView bottomNav;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        Log.d("SDK_VERSION", "Current SDK: " + sdkVersion);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        LiveData<User> currentUserLiveData = viewModel.getCurrentUser();
        setContentView(R.layout.activity_main);

        txtGreetingUser = findViewById(R.id.txtGreetingUser);
        myCourseBtn = findViewById(R.id.myCourseBtn);
        viewMoreBtn = findViewById(R.id.viewMoreBtn);
        testSetBtn = findViewById(R.id.testSetBtn);
        vocabBtn = findViewById(R.id.vocabBtn);
        btnInteractCourse = findViewById(R.id.btnInteractCourse);
        txtContact = findViewById(R.id.txtContact);
        txtInteractCourseButton = findViewById(R.id.txtInteractCourseButton);
        bottomNav = findViewById(R.id.bottom_nav);

        currentUserLiveData.observe(this, user -> {
            if (user != null) {
                String greeting = "Hello, " + user.getName();
                txtGreetingUser.setText(greeting);
                studentViewModel.getStudentById(user.getUid()).observe(this, student -> {
                    if (student != null) {
                        List<String> courses = student.getCourses();
                        if (courses == null || courses.isEmpty()) {
                            btnInteractCourse.setOnClickListener(view -> {
                                Intent intent = new Intent(this, CoursePaymentActivity.class);
                                startActivity(intent);
                            });
                        } else {
                            String courseText = "You currently have " + courses.size() +
                                    (courses.size() == 1 ? " course" : " courses") + ". Let's check out.";
                            txtContact.setText(courseText);
                            txtInteractCourseButton.setText("Learn course");
                            btnInteractCourse.setOnClickListener(view -> {
                                Intent intent = new Intent(this, CourseListActivity.class);
                                startActivity(intent);
                            });
                        }
                    }
                });
            } else {
                txtGreetingUser.setText("Hello!");
            }
        });


        myCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });

        viewMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CoursePaymentActivity.class);
                startActivity(intent);
            }
        });

        testSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TestSetListActivity.class);
                startActivity(intent);
            }
        });

        vocabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TopicActivity.class);
                startActivity(intent);
            }
        });

        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_courses) {
                startActivity(new Intent(MainActivity.this, CourseListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_test_set) {
                startActivity(new Intent(MainActivity.this, TestSetListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_vocab) {
                Log.d("MainActivity", "Navigating to Vocabulary");
                startActivity(new Intent(MainActivity.this, TopicActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}
