package com.group5.preppal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Admin;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.Teacher;
import com.group5.preppal.data.model.User;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.viewmodel.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private TextView userInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        userInfoTextView = findViewById(R.id.userInfoTextView);

        // Initialize ViewModel using Hilt
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observe user data
        userViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                displayUserInfo(user);
            } else {
                userInfoTextView.setText("User not found.");
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
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

    private void displayUserInfo(User user) {
        String userInfo;
        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            userInfo = "Admin: " + admin.getName();
        } else if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            userInfo = "Teacher: " + teacher.getName();
        } else if (user instanceof Student) {
            Student student = (Student) user;
            userInfo = "Student: " + student.getName();
        } else {
            userInfo = "Unknown user type.";
        }
        userInfoTextView.setText(userInfo);
    }
}
