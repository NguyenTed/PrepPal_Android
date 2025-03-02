package com.group5.preppal.ui.profile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ View
        setupClickListener(R.id.accountInformationRoute, AccountInformationActivity.class);
        setupClickListener(R.id.courseRoute, CourseInfoActivity.class);
    }

    private void setupClickListener(int layoutId, Class<?> destination) {
        LinearLayout layout = findViewById(layoutId);
        layout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, destination);

            // Tạo hiệu ứng mở rộng từ nhỏ ra lớn
            ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                    v,  // View bắt đầu hiệu ứng
                    v.getWidth() / 2, v.getHeight() / 2, // Tâm mở rộng
                    0, 0 // Kích thước ban đầu
            );

            startActivity(intent, options.toBundle());
        });
    }
}
