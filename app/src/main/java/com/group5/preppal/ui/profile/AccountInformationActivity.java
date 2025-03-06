package com.group5.preppal.ui.profile;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountInformationActivity extends AppCompatActivity {
    private ImageButton backButton;
    private EditText edtDateOfBirth;
    private Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        // Ánh xạ View
        backButton = findViewById(R.id.backButton);
        edtDateOfBirth = findViewById(R.id.edtDateOfBirth);

        // Khởi tạo calendar
        calendar = Calendar.getInstance();

        // Xử lý sự kiện khi bấm vào nút Back
        backButton.setOnClickListener(v -> shrinkAndFinish());

        // Mở DatePicker khi nhấn vào EditText
        edtDateOfBirth.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    updateDateField();
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void updateDateField() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        edtDateOfBirth.setText(sdf.format(calendar.getTime()));
    }

    private void shrinkAndFinish() {
        View rootView = findViewById(android.R.id.content);

        // Tạo hiệu ứng thu nhỏ (scale)
        ObjectAnimator shrinkAnimation = ObjectAnimator.ofPropertyValuesHolder(
                rootView,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0.5f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.5f),
                PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)
        );

        shrinkAnimation.setDuration(100); // Thời gian hiệu ứng
        shrinkAnimation.setInterpolator(new AccelerateInterpolator()); // Tăng tốc hiệu ứng

        // Khi animation kết thúc, đóng Activity
        shrinkAnimation.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                finish();
                overridePendingTransition(0, 0); // Không có hiệu ứng hệ thống để tránh xung đột
            }
        });

        shrinkAnimation.start();
    }
}
