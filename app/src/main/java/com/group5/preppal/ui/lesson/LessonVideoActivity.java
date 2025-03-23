package com.group5.preppal.ui.lesson;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.ui.course.CourseDetailActivity;
import com.group5.preppal.viewmodel.LessonViewModel;
import com.group5.preppal.viewmodel.StudentViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

@AndroidEntryPoint
public class LessonVideoActivity extends AppCompatActivity {
    private String lessonId;
    private TextView lessonName;
    private WebView webView;
    private FrameLayout webViewContainer;
    private ImageButton backBtn;
    private LessonViewModel lessonViewModel;
    private Button btnComplete;

    @Inject
    AuthRepository authRepository;
    FirebaseUser user;
    private StudentViewModel studentViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_lesson);

        user = authRepository.getCurrentUser();
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        lessonId = getIntent().getStringExtra("lessonId");
        lessonName = findViewById(R.id.lessonName);
        backBtn = findViewById(R.id.backButton);
        webView = findViewById(R.id.webView);
        webViewContainer = findViewById(R.id.webViewContainer);
        btnComplete = findViewById(R.id.btnComplete);

        lessonName.setText("");
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra("courseId", getIntent().getStringExtra("courseId"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnComplete.setOnClickListener(view -> {
            studentViewModel.saveFinishedLesson(lessonId, user.getUid());
            Toast.makeText(this, "Lesson is finished", Toast.LENGTH_SHORT).show();
            this.finish();
        });

        lessonViewModel = new ViewModelProvider(this).get(LessonViewModel.class);
        try {
            lessonViewModel.fetchLessonById(lessonId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        lessonViewModel.getSelectedLesson().observe(this, lesson -> {
            if (lesson != null) {
                lessonName.setText(lesson.getName());

                String videoUrl = lesson.getVideoUrl();
                if (videoUrl != null && !videoUrl.isEmpty()) {
                    loadVideoInWebView(videoUrl);
                } else {
                    Toast.makeText(this, "No Video available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        studentViewModel.getStudentById(user.getUid()).observe(this, student -> {
            if (student != null) {
                if (student.getFinishedLessons().contains(lessonId)) {
                    btnComplete.setText("Completed");
                    btnComplete.setEnabled(false);
                    btnComplete.setBackgroundResource(R.drawable.rounded_5dp_white_2dp_border_gray);
                    btnComplete.setTextColor(Color.parseColor("#A3A5A4"));
                } else {
                    Log.e("Student", "Student not found!");
                }
            }
        });
    }

    private void loadVideoInWebView(String videoUrl) {
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.clearHistory();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());

        if (isYouTubeUrl(videoUrl)) {
            String videoId = extractYouTubeVideoId(videoUrl);
            if (videoId != null) {
                String embedHtml = getYouTubeEmbedHtml(videoId);
                webView.loadData(embedHtml, "text/html", "utf-8");
            } else {
                Toast.makeText(this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            }
        } else if (isGoogleDriveUrl(videoUrl)) {
            webViewContainer.setRotation(90f);
            String fileId = extractDriveFileId(videoUrl);
            if (fileId != null) {
                String embedHtml = getGoogleDriveEmbedHtml(fileId);
                webView.loadData(embedHtml, "text/html", "utf-8");
            } else {
                Toast.makeText(this, "Invalid Google Drive URL", Toast.LENGTH_SHORT).show();
            }
        } else {
            webView.loadUrl(videoUrl);
        }
    }

    private boolean isYouTubeUrl(String url) {
        return url.contains("youtube.com/watch?v=") || url.contains("youtu.be/");
    }

    private String getYouTubeEmbedHtml(String videoId) {
        return "<html><head><style>" +
                "body { margin: 0; padding: 0; background-color: black; } " +
                "iframe { width: 100%; height: 100vh; border: none; background-color: black; } " +
                "</style></head><body>" +
                "<iframe src='https://www.youtube.com/embed/" + videoId + "' " +
                "frameborder='0' allow='accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture' " +
                "allowfullscreen></iframe></body></html>";
    }

    private String extractYouTubeVideoId(String url) {
        String pattern = "(?:youtube\\.com/watch\\?v=|youtu\\.be/|youtube\\.com/embed/)([\\w-]+)";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean isGoogleDriveUrl(String url) {
        return url.contains("drive.google.com/file/d/");
    }

    private String getGoogleDriveEmbedHtml(String fileId) {
        return "<html><head><style>" +
                "body { margin: 0; padding: 0; background-color: black; overflow: hidden; } " +
                "iframe { width: 100%; height: 100vh; border: none; background-color: black; } " +
                "</style></head><body>" +
                "<iframe src='https://drive.google.com/file/d/" + fileId + "/preview?embedded=true' " +
                "frameborder='0' allow='autoplay'></iframe>" +
                "<script>setTimeout(function() { " +
                "var toolbar = document.querySelector('.ndfHFb-c4YZDc-Wrql6b'); " +
                "if (toolbar) { toolbar.style.display = 'none'; }" +
                "}, 1000);</script>" +
                "</body></html>";
    }

    private String extractDriveFileId(String url) {
        String pattern = "https://drive\\.google\\.com/file/d/([\\w-]+)/view.*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
