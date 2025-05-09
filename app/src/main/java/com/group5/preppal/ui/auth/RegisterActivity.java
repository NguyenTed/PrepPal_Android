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
import com.group5.preppal.utils.ShowToast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.data.model.User;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.ui.choose_band.ChooseBandActivity;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.viewmodel.AuthViewModel;

import java.util.Date;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText, dobEditText;
    private Spinner genderSpinner;
    private Button registerButton;
    private TextView loginTextView;

    private Calendar selectedDate = Calendar.getInstance(); // ✅ Stores selected DOB

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
        loginTextView = findViewById(R.id.loginTextView);

        // ✅ Disable keyboard input for DOB field
        dobEditText.setInputType(InputType.TYPE_NULL);
        dobEditText.setFocusable(false);
        dobEditText.setOnClickListener(v -> showDatePickerDialog());

        // ✅ Populate Gender Spinner
        ArrayAdapter<User.Gender> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, User.Gender.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        registerButton.setOnClickListener(v -> registerUser());
        loginTextView.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        observeAuthState();
    }

    // ✅ Date Picker Dialog (Only Shows Dialog, No Keyboard)
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    dobEditText.setText(sdf.format(selectedDate.getTime())); // ✅ Display selected DOB
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String genderStr = genderSpinner.getSelectedItem().toString();
        User.Gender gender;

        if (Objects.equals(genderStr, "MALE"))
            gender = User.Gender.MALE;
        else if (Objects.equals(genderStr, "FEMALE"))
            gender = User.Gender.FEMALE;
        else
            gender = User.Gender.OTHER;

        // ✅ Input validation
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
        if (TextUtils.isEmpty(dob)) {
            dobEditText.setError("Date of Birth is required");
            return;
        }

        // ✅ Pass DOB as a `Date` object
        Date dateOfBirth = selectedDate.getTime();

        authViewModel.signUpWithEmail(email, password, name, dateOfBirth, gender);
    }

    private void observeAuthState() {
        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                ShowToast.show(this, "Registration successful", ShowToast.ToastType.SUCCESS);
                startActivity(new Intent(this, ChooseBandActivity.class));
                finish();
            }
        });

        authViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                ShowToast.show(this, errorMessage, ShowToast.ToastType.ERROR);
            }
        });
    }
}


