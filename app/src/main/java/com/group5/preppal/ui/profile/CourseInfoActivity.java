package com.group5.preppal.ui.profile;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseInfoActivity extends AppCompatActivity {
    private ImageButton backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> shrinkAndFinish());
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
