package com.group5.preppal.ui.admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.data.model.User;
import com.group5.preppal.utils.LanguageUtils;
import com.group5.preppal.viewmodel.AddNewTeacherViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNewTeacherActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput;
    private TextView dobText;
    private Spinner genderSpinner;
    private Button addButton;
    private ProgressBar progressBar;

    private Calendar dobCalendar = Calendar.getInstance();
    private AddNewTeacherViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_teacher);

        viewModel = new ViewModelProvider(this).get(AddNewTeacherViewModel.class);

        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        dobText = findViewById(R.id.input_dob);
        genderSpinner = findViewById(R.id.input_gender);
        addButton = findViewById(R.id.btn_add_teacher);
        progressBar = findViewById(R.id.progress_bar);

        setupGenderSpinner();
        setupDobPicker();

        addButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            User.Gender gender = User.Gender.valueOf(genderSpinner.getSelectedItem().toString().toUpperCase());
            Date dob = dobCalendar.getTime();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            viewModel.addNewTeacher(this, name, email, password, dob, gender, () -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Teacher added!", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }, error -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
            });
        });
    }

    private void setupGenderSpinner() {
        String language = LanguageUtils.getSavedLanguage(this);
        String[] genders;
        Log.d("LANGUAGE_CHECK", "Current language: " + language);
        if (language.equals("en")) {
            genders = new String[]{"MALE", "FEMALE", "OTHER"};
        } else {
            genders = new String[]{"Nam", "Nữ", "Khác"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }


    private void setupDobPicker() {
        dobText.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                dobCalendar.set(Calendar.YEAR, year);
                dobCalendar.set(Calendar.MONTH, month);
                dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDobLabel();
            }, dobCalendar.get(Calendar.YEAR), dobCalendar.get(Calendar.MONTH), dobCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        updateDobLabel();
    }

    private void updateDobLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dobText.setText(sdf.format(dobCalendar.getTime()));
    }


}

