package com.group5.preppal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.viewmodel.AuthViewModel;

import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText emailEditText, passwordEditText, nameEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        nameEditText = findViewById(R.id.editTextName);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
                authViewModel.register(email, password, name, new Date());
            }
        });

        authViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
