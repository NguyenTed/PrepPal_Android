package com.group5.preppal.ui.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.R;
import com.group5.preppal.data.repository.AuthRepository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class WritingTestActivity extends AppCompatActivity {
    private TextView tvTitle, tvQuestion;
    private EditText etAnswer;
    private Button btnSubmit;
    private ImageView btnBack, imgQuestion;
    private FirebaseFirestore db;

    @Inject
    AuthRepository authRepository;
    private FirebaseUser user;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_test);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        tvTitle = findViewById(R.id.tvTitle);
        tvQuestion = findViewById(R.id.tvQuestion);
        etAnswer = findViewById(R.id.etAnswer);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        imgQuestion = findViewById(R.id.imgQuestion);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String imgUrl = intent.getStringExtra("imgUrl");

            Log.d("WritingTestActivity", "Image URL: " + imgUrl);

            tvTitle.setText(title);
            tvQuestion.setText(description);

            if (imgUrl != null && !imgUrl.isEmpty()) {
                imgQuestion.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(imgUrl)
                        .placeholder(R.drawable.loading)
                        .error(new ColorDrawable(Color.TRANSPARENT))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                imgQuestion.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                imgQuestion.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(imgQuestion);
            } else {
                imgQuestion.setVisibility(View.GONE);
            }
        }

        // Kiểm tra người dùng đăng nhập
        user = authRepository.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity nếu chưa đăng nhập
            return;
        } else {
            Log.d("WritingTestActivity", "User ID: " + user.getUid());
            Log.d("WritingTestActivity", "User Email: " + user.getEmail());
        }

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý nút Submit
        btnSubmit.setOnClickListener(v -> submitAnswer());
    }

    private void submitAnswer() {
        String answer = etAnswer.getText().toString().trim();
        if (answer.isEmpty()) {
            Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getIntent();
        String userId = user.getUid();
        String taskId=intent.getStringExtra("id");
        // Tạo dữ liệu để gửi lên Firestore
        Map<String, Object> submission = new HashMap<>();
        submission.put("userId", userId);
        submission.put("answer", answer);
        submission.put("taskId",taskId);
        submission.put("timestamp", System.currentTimeMillis());

        // Lưu vào Firestore
        db.collection("writing_submissions")
                .add(submission)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Answer submitted successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to submit answer", Toast.LENGTH_SHORT).show());
    }
}