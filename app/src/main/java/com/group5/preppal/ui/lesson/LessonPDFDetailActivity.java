package com.group5.preppal.ui.lesson;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.User;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.ui.course.CourseDetailActivity;
import com.group5.preppal.ui.course.CourseListActivity;
import com.group5.preppal.viewmodel.LessonViewModel;
import com.group5.preppal.viewmodel.StudentViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LessonPDFDetailActivity extends AppCompatActivity {
    private String lessonId;
    private TextView lessonName;
    private WebView webView;
    private ImageButton backBtn;
    private Button btnComplete;

    @Inject
    AuthRepository authRepository;
    FirebaseUser user;
    private StudentViewModel studentViewModel;


    private LessonViewModel lessonViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_lesson);

        user = authRepository.getCurrentUser();
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        lessonId = getIntent().getStringExtra("lessonId");
        lessonName = findViewById(R.id.lessonName);
        backBtn = findViewById(R.id.backButton);
        webView = findViewById(R.id.webView);
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
