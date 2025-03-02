package com.group5.preppal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.ui.LoginActivity;
import com.group5.preppal.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.ui.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText, dobEditText;
    private Spinner genderSpinner, roleSpinner;
    private Button registerButton;
    private TextView loginTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        dobEditText = findViewById(R.id.dobEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        roleSpinner = findViewById(R.id.roleSpinner);
        registerButton = findViewById(R.id.registerButton);
        loginTextView = findViewById(R.id.loginTextView);

        registerButton.setOnClickListener(v -> registerUser());
        loginTextView.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        observeAuthState();
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String role = roleSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }
        if (TextUtils.isEmpty(dob)) {
            dobEditText.setError("Date of Birth is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
//        authViewModel.signUpWithEmail(email, password, name, dob, gender, role);
    }

    private void observeAuthState() {
        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });

        authViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
