package com.group5.preppal.ui.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.R;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

public class WritingTestActivity extends AppCompatActivity {

    private static final String TAG = "WritingTestActivity";
    private TextView tvTitle;
    private TextView tvQuestion;
    private EditText etAnswer;
    private Button btnSubmit;
    private ImageView btnBack;
    private FirebaseFirestore db;
    private String writingTestId; // ID của bài viết từ Firestore

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_test); // Gán layout XML

        // Ánh xạ các thành phần giao diện
        tvTitle=findViewById(R.id.tvTitle);
        tvQuestion = findViewById(R.id.tvQuestion);
        etAnswer = findViewById(R.id.etAnswer);
        btnSubmit = findViewById(R.id.registerButton);
        btnBack = findViewById(R.id.btnBack);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Nhận dữ liệu từ Intent
        if (getIntent().hasExtra("topic_id") && getIntent().hasExtra("title")) {
            writingTestId = getIntent().getStringExtra("topic_id");
            String title = getIntent().getStringExtra("title");
            String question = getIntent().getStringExtra("description");

            Log.d(TAG, "📥 Received Intent Data - ID: " + writingTestId + ", Question: " + question);
            tvTitle.setText(title);
            tvQuestion.setText(question);
        } else {
            Log.e(TAG, "❌ Intent data missing!");
            Toast.makeText(this, "Error: Missing test data!", Toast.LENGTH_LONG).show();
            finish(); // Đóng activity nếu thiếu dữ liệu
            return;
        }

        // Xử lý khi nhấn nút back
        btnBack.setOnClickListener(v -> finish());

        // Xử lý khi nhấn nút submit
        btnSubmit.setOnClickListener(v -> saveAnswerToFirestore());
    }

    private void saveAnswerToFirestore() {
        String answer = etAnswer.getText().toString().trim();

        if (answer.isEmpty()) {
            Toast.makeText(this, "Please write your answer before submitting!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo dữ liệu để lưu
        Map<String, Object> answerData = new HashMap<>();
        answerData.put("writingTestId", writingTestId);
        answerData.put("answer", answer);
        answerData.put("timestamp", System.currentTimeMillis());

        // Lưu vào Firestore
        db.collection("WritingAnswers")
                .add(answerData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(WritingTestActivity.this, "✅ Answer submitted successfully!", Toast.LENGTH_SHORT).show();
                    etAnswer.setText(""); // Xóa nội dung sau khi gửi
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "❌ Failed to submit answer", e);
                    Toast.makeText(WritingTestActivity.this, "❌ Failed to submit answer.", Toast.LENGTH_SHORT).show();
                });
    }
}
