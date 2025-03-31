package com.group5.preppal.ui.quiz.speaking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.group5.preppal.R;
import com.group5.preppal.ui.course.CourseDetailActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SpeakingBookingActivity extends AppCompatActivity {
    private TextView tvTitle, tvTestType;
    private ImageButton backButton;
    private CalendarView calendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_booking);

        tvTitle = findViewById(R.id.tvTitle);
        tvTestType = findViewById(R.id.tvTestType);
        backButton = findViewById(R.id.backButton);

        tvTitle.setText("Speaking Test Booking");
        tvTestType.setText("Speaking Test");

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra("courseId", this.getIntent().getStringExtra("courseId"));
            startActivity(intent);
        });

        // Gắn Fragment BookingSpeakingFragment vào container
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new BookingSpeakingFragment());
        fragmentTransaction.commit();
    }

    public void updateTitle(String title, String testType) {
        tvTitle.setText(title);
        tvTestType.setText(testType);
    }
}