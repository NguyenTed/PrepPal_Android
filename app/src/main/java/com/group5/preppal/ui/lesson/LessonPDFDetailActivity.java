package com.group5.preppal.ui.lesson;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.ui.course.CourseDetailActivity;
import com.group5.preppal.ui.course.CourseListActivity;
import com.group5.preppal.viewmodel.LessonViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LessonPDFDetailActivity extends AppCompatActivity {
    private String lessonId;
    private TextView lessonName;
    private WebView webView;
    private ImageButton backBtn;

    private LessonViewModel lessonViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_lesson);

        lessonId = getIntent().getStringExtra("lessonId");

        lessonName = findViewById(R.id.lessonName);
        backBtn = findViewById(R.id.backButton);
        webView = findViewById(R.id.webView);

        lessonName.setText("");
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra("courseId", getIntent().getStringExtra("courseId"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        lessonViewModel = new ViewModelProvider(this).get(LessonViewModel.class);
        lessonViewModel.fetchLessonById(lessonId);

        lessonViewModel.getSelectedLesson().observe(this, lesson -> {
            if (lesson != null) {
                lessonName.setText(lesson.getName());

                String pdfUrl = lesson.getReadingUrl();
                if (pdfUrl != null && !pdfUrl.isEmpty()) {
                    loadPdfInWebView(pdfUrl);
                } else {
                    Toast.makeText(this, "No PDF available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void loadPdfInWebView(String pdfUrl) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(pdfUrl);
    }

}
