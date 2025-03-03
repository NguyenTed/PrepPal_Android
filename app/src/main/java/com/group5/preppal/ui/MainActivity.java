package com.group5.preppal.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.data.model.Admin;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.Teacher;
import com.group5.preppal.data.model.User;
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
