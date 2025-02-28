package com.group5.preppal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private TextView usernameTextView, emailTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        usernameTextView = findViewById(R.id.textViewUsername);
        emailTextView = findViewById(R.id.textViewEmail);
        logoutButton = findViewById(R.id.buttonLogout);

        authViewModel.getUserDetailsLiveData().observe(this, user -> {
            if (user != null) {
                usernameTextView.setText(user.getName());
                emailTextView.setText(user.getEmail());
            }
        });

        logoutButton.setOnClickListener(v -> authViewModel.logout());

        authViewModel.getIsLoggedOutLiveData().observe(this, isLoggedOut -> {
            if (isLoggedOut) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}


