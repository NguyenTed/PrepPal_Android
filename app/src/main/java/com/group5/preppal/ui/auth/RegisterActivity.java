package com.group5.preppal.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.data.model.User;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.viewmodel.AuthViewModel;

import java.util.Date;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText, dobEditText;
    private Spinner genderSpinner;
    private Button registerButton;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        dobEditText = findViewById(R.id.dobEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        registerButton = findViewById(R.id.registerButton);
//        loginTextView = findViewById(R.id.loginTextView);

        ArrayAdapter<User.Gender> adapter = new ArrayAdapter<User.Gender>(this, android.R.layout.simple_spinner_item, User.Gender.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        registerButton.setOnClickListener(v -> registerUser());
//        loginTextView.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        observeAuthState();
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String genderStr = genderSpinner.getSelectedItem().toString();
        User.Gender gender;

        if (Objects.equals(genderStr, "MALE"))
            gender = User.Gender.MALE;
        else if (Objects.equals(genderStr, "FEMALE"))
            gender = User.Gender.FEMALE;
        else
            gender = User.Gender.OTHER;


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
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }
//        if (TextUtils.isEmpty(dob)) {
//            dobEditText.setError("Date of Birth is required");
//            return;
//        }

        authViewModel.signUpWithEmail(email, password, name, new Date(), gender);
    }

    private void observeAuthState() {
        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });

        authViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
