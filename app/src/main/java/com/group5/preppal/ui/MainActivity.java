package com.group5.preppal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.User;
import com.group5.preppal.ui.course.CourseListActivity;
import com.group5.preppal.ui.course_payment.CoursePaymentActivity;
import com.group5.preppal.ui.dictionary.DictionaryActivity;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.ui.test_set.TestListActivity;
import com.group5.preppal.ui.test_set.TestSetListActivity;
import com.group5.preppal.ui.vocabulary.TopicActivity;
import com.group5.preppal.viewmodel.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private UserViewModel viewModel;
    private TextView txtGreetingUser;
    private LinearLayout myCourseBtn, viewMoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        Log.d("SDK_VERSION", "Current SDK: " + sdkVersion);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        LiveData<User> currentUserLiveData = viewModel.getCurrentUser();
        setContentView(R.layout.activity_main);

        txtGreetingUser = findViewById(R.id.txtGreetingUser);
        myCourseBtn = findViewById(R.id.myCourseBtn);
        viewMoreBtn = findViewById(R.id.viewMoreBtn);

        currentUserLiveData.observe(this, user -> {
            if (user != null) {
                String greeting = "Hello, " + user.getName();
                txtGreetingUser.setText(greeting);
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

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_courses) {
                startActivity(new Intent(MainActivity.this, CourseListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_vocab) {
                startActivity(new Intent(MainActivity.this, TestSetListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_dictionary) {
                startActivity(new Intent(MainActivity.this, DictionaryActivity.class));
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

}
