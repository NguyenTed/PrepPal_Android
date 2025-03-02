package com.group5.preppal.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout detailInfoLayout;
    private ImageButton rightArrowButton;
    private FrameLayout detailContainer;
    private boolean isDetailVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Khởi tạo các thành phần giao diện
        detailInfoLayout = findViewById(R.id.detailInfoLayout);
        rightArrowButton = findViewById(R.id.rightArrowButton);
        detailContainer = findViewById(R.id.detailContainer);

        // Thiết lập sự kiện khi nhấn vào detailInfoLayout
        detailInfoLayout.setOnClickListener(v -> toggleUserDetails());
    }

    private void toggleUserDetails() {
        if (!isDetailVisible) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailContainer, new UserDetailsFragment())
                    .commit();
            detailContainer.setVisibility(View.VISIBLE);
            rightArrowButton.setRotation(90f);  // Xoay nút 90 độ
        } else {
            detailContainer.setVisibility(View.GONE);
            rightArrowButton.setRotation(0f);  // Đưa nút về trạng thái ban đầu
        }
        isDetailVisible = !isDetailVisible;
    }
}
