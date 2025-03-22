package com.group5.preppal.ui.grading;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.group5.preppal.R;

public class GradingWritingTestActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading_writing_test);

        // Khởi tạo TextView mà không gán giá trị từ Intent
        titleTextView = findViewById(R.id.title);
        backButton = findViewById(R.id.backButton);  // Lấy nút Back từ layout

        // Xử lý khi nhấn nút Back
        backButton.setOnClickListener(v -> finish());
        // Hiển thị nút back trên ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
