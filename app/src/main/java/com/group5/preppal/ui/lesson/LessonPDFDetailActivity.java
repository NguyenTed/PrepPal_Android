package com.group5.preppal.ui.lesson;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;

public class LessonPDFDetailActivity extends AppCompatActivity {
    private VideoView videoView;
    private String videoUrl = "https://drive.google.com/uc?id=1GB0t-1gJpH_N8pI1hzhR7-Y7GxBO6Fxy";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_pdf_detail);

        videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);

        // Thêm điều khiển phát/tạm dừng cho VideoView
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start(); // Phát video ngay khi mở
    }
}
