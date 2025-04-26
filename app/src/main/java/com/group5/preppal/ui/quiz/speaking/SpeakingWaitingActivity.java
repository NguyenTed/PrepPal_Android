package com.group5.preppal.ui.quiz.speaking;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.group5.preppal.utils.ShowToast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.User;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.ui.course.CourseDetailActivity;
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
    private ImageButton backButton;
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
        backButton = findViewById(R.id.backButton);

        String speakingTestId = this.getIntent().getStringExtra("quizId");
        speakingTestViewModel = new ViewModelProvider(this).get(SpeakingTestViewModel.class);
        setTitle(speakingTestId);

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        if (user != null) {
            setWaitingInfo(speakingTestId);
        }
        backButton.setOnClickListener((view -> {
            navigateBackToCourse();
        }));
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

                long now = System.currentTimeMillis();
                long start = bookedDate.getTime();
                long end = start + 30 * 60 * 1000L;

                boolean canJoin = now >= start && now <= end;

                if (!canJoin) {
                    // Không nằm trong khung giờ → đổi màu nút + thông báo khi click
                    btnJoin.setBackgroundResource(R.drawable.rounded_20dp_gray); // màu xám
                    btnJoin.setOnClickListener(view -> {
                        ShowToast.show(this, "Bạn chỉ có thể tham gia trong khung giờ đã đặt!", ShowToast.ToastType.WARNING);
                    });
                } else {
                    // Trong khung giờ → cho phép vào phòng
                    btnJoin.setOnClickListener(view -> {
                        navigateToVideoCallActivity(bookedDate);
                    });
                }
                btnBookAgain.setOnClickListener(view -> {
                    long bookedTime = bookedDate.getTime();
                    long diff = Math.abs(bookedTime - now);
                    Log.d("Speaking Waiting", "diff: " + diff);
                    // Nếu hiện tại < 24h so với giờ booking
                    if (diff < 24 * 60 * 60 * 1000L && diff > 0) {
                        ShowToast.show(this, "Bạn chỉ có thể tham gia trong khung giờ đã đặt!", ShowToast.ToastType.WARNING);
                    } else {
                        navigateToSpeakingBookingActivity();
                    }
                });
            } else {
                tvBookingTime.setText("No booking found");
                btnJoin.setEnabled(false);
            }
        });
    }

    private void navigateToVideoCallActivity(Date date) {
        Intent intent = new Intent(this, VideoCallActivity.class);
        intent.putExtra("role", "Student");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        intent.putExtra("startTime", timeFormat.format(date));
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

    private void navigateBackToCourse() {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra("courseId", this.getIntent().getStringExtra("courseId"));
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
