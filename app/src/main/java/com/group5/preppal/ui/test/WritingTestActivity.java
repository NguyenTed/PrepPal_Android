package com.group5.preppal.ui.test;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.group5.preppal.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WritingTestActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_test);

        tvTitle = findViewById(R.id.tvTitle);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
    }

    // Cho phép Fragment cập nhật tiêu đề
    public void updateTitle(String title) {
        tvTitle.setText(title);
    }
}
