package com.group5.preppal.ui.profile;

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
    private ImageButton rightDetailArrowButton;
    private FrameLayout detailContainer;
    private boolean isDetailVisible = false;

    private LinearLayout detailCourseListLayout;
    private ImageButton rightArrowButton;
    private FrameLayout listContainer;
    private boolean isCourseListVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Khởi tạo các thành phần giao diện
        detailInfoLayout = findViewById(R.id.detailInfoLayout);
        rightDetailArrowButton = findViewById(R.id.rightDetailArrowButton);
        detailContainer = findViewById(R.id.detailContainer);

        detailCourseListLayout = findViewById(R.id.detailCourseListLayout);
        rightArrowButton = findViewById(R.id.rightArrowButton);
        listContainer = findViewById(R.id.listContainer);

        // Thiết lập sự kiện khi nhấn vào detailInfoLayout
        detailInfoLayout.setOnClickListener(v -> toggleUserDetails());
        detailCourseListLayout.setOnClickListener(v -> toggleCourseList());
    }

    private void toggleUserDetails() {
        if (!isDetailVisible) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailContainer, new UserDetailsFragment())
                    .commit();
            detailContainer.setVisibility(View.VISIBLE);
            rightDetailArrowButton.setRotation(90f);  // Xoay nút 90 độ
        } else {
            detailContainer.setVisibility(View.GONE);
            rightDetailArrowButton.setRotation(0f);  // Đưa nút về trạng thái ban đầu
        }
        isDetailVisible = !isDetailVisible;
    }

    private void toggleCourseList() {
        if (!isCourseListVisible) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer, new CourseListProfileFragment())
                    .commit();
            listContainer.setVisibility(View.VISIBLE);
            rightArrowButton.setRotation(90f);  // Xoay nút 90 độ
        } else {
            listContainer.setVisibility(View.GONE);
            rightArrowButton.setRotation(0f);  // Đưa nút về trạng thái ban đầu
        }
        isCourseListVisible = !isCourseListVisible;
    }
}
