package com.group5.preppal.ui.quiz.speaking;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.User;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.ui.video_call.VideoCallActivity;
import com.group5.preppal.viewmodel.SpeakingTestViewModel;
import com.group5.preppal.viewmodel.StudentViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SpeakingWaitingActivity extends AppCompatActivity {
    private StudentViewModel studentViewModel;
    private SpeakingTestViewModel speakingTestViewModel;
    private TextView tvTitle, tvType, tvBookingTime;
    private Button btnJoin, btnBookAgain;
    @Inject
    AuthRepository authRepository;
    FirebaseUser user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_waiting);
        user = authRepository.getCurrentUser();
        tvTitle = findViewById(R.id.tvTitle);
        tvType = findViewById(R.id.tvTestType);
        tvBookingTime = findViewById(R.id.tvBookingTime);
        btnJoin = findViewById(R.id.btnJoin);
        btnBookAgain = findViewById(R.id.btnBookAgain);

        String speakingTestId = this.getIntent().getStringExtra("quizId");
        speakingTestViewModel = new ViewModelProvider(this).get(SpeakingTestViewModel.class);
        setTitle(speakingTestId);

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        if (user != null) {
            setWaitingInfo(speakingTestId);
        }
    }

    private void setTitle(String speakingTestId) {
        speakingTestViewModel.getSpeakingTestById(speakingTestId).observe(this, speakingTest -> {
            if (speakingTest != null) {
                tvTitle.setText(speakingTest.getName());
                tvType.setText(speakingTest.getType());
            }
        });
    }

    private void setWaitingInfo(String speakingTestId) {
        studentViewModel.fetchStudentBookedSpeakingById(speakingTestId, user.getUid()).observe(this, booked -> {
            if (booked != null) {
                Date bookedDate = booked.getBookedDate();
                tvBookingTime.setText(formatBookedDate(bookedDate));
                btnJoin.setOnClickListener(view -> {
                    navigateToVideoCallActivity();
                });
                btnBookAgain.setOnClickListener(view -> {
                    navigateToSpeakingBookingActivity();
                });
            } else {
                tvBookingTime.setText("No booking found");
                btnJoin.setEnabled(false);
            }
        });
    }

    private void navigateToVideoCallActivity() {
        Intent intent = new Intent(this, VideoCallActivity.class);
        intent.putExtra("courseId", this.getIntent().getStringExtra("courseId"));
        intent.putExtra("quizId", this.getIntent().getStringExtra("quizId"));
        startActivity(intent);
    }

    private void navigateToSpeakingBookingActivity() {
        Intent intent = new Intent(this, SpeakingBookingActivity.class);
        intent.putExtra("courseId", this.getIntent().getStringExtra("courseId"));
        intent.putExtra("quizId", this.getIntent().getStringExtra("quizId"));
        startActivity(intent);
    }

    private String formatBookedDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 30);
        Date endDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String result = sdf.format(date) + " - " + timeFormat.format(endDate);
        return result;
    }
}
