package com.group5.preppal.ui.grading;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.group5.preppal.R;
import com.group5.preppal.data.model.WritingQuizSubmission;
import com.group5.preppal.data.repository.WritingQuizSubmissionRepository;
import com.group5.preppal.service.GeminiService;
import com.group5.preppal.viewmodel.WritingTestViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class GradingWritingTestActivity extends AppCompatActivity {
    private TextView taskTitle, tvDescription, tvStudentAnswer;
    private ImageView imgQuestion;
    private ImageButton backButton;
    private EditText etComment, etScore;
    private Spinner spinnerState;
    private Button btnSave, btnAIComment;
    TextView tvClearComment;

    private WritingTestViewModel writingTestViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading_writing_test);

        imgQuestion = findViewById(R.id.imgQuestion);
        tvDescription = findViewById(R.id.tvDescription);
        tvStudentAnswer = findViewById(R.id.tvStudentAnswer);
        etComment = findViewById(R.id.etComment);
        etScore = findViewById(R.id.etScore);
        taskTitle = findViewById(R.id.taskTitle);
        spinnerState = findViewById(R.id.spinnerStatus);
        btnSave = findViewById(R.id.btnSave);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
        //Set up Clear comment
        tvClearComment = findViewById(R.id.tvClearComment);
        setUpTVClearComment();
        //Set up button AI Comment
        btnAIComment = findViewById(R.id.btnAIComment);
        btnAIComment.setOnClickListener(v -> {
            String studentAnswer = tvStudentAnswer.getText().toString();
            etComment.setText("Generating feedback...");
            etScore.setText("...");
            btnAIComment.setEnabled(false);
            new Thread(() -> {
                String feedback = GeminiService.generateFeedback(studentAnswer);
                String grade = GeminiService.generateGrade(studentAnswer);
                runOnUiThread(() -> {
                    etComment.setText(feedback);
                    etScore.setText(grade);
                    btnAIComment.setEnabled(true);

                });
            }).start();
        });

        etComment.setOnTouchListener((v, event) -> {
            handleEditTextScroll(v, event);

            return false; // Cho phép EditText xử lý scroll
        });

        tvStudentAnswer.setOnTouchListener((v, event) -> {
            handleEditTextScroll(v, event);

            return false; // Cho phép EditText xử lý scroll
        });



        writingTestViewModel = new ViewModelProvider(this).get(WritingTestViewModel.class);
        String submissionId = getIntent().getStringExtra("submissionId");
        writingTestViewModel.getQuizSubmissionById(submissionId).observe(this, submission -> {
            if (submission != null) {
                writingTestViewModel.getTaskById(submission.getTaskId()).observe(this, task -> {
                    if (task != null) {
                        if (task.getImgUrl() != "") {
                            Glide.with(this)
                                    .load(task.getImgUrl())
                                    .placeholder(R.drawable.loading)
                                    .into(imgQuestion);
                        } else imgQuestion.setVisibility(View.GONE);
                        tvDescription.setText(task.getDescription());
                        taskTitle.setText(task.getTitle());
                        tvStudentAnswer.setText(submission.getAnswer());
                        etScore.setText(String.valueOf(submission.getPoints()));
                        etComment.setText(String.valueOf(submission.getComment()));
                        String state = submission.getState();
                        String[] states = getResources().getStringArray(R.array.submission_states);

                        for (int i = 0; i < states.length; i++) {
                            if (states[i].equalsIgnoreCase(state)) {
                                spinnerState.setSelection(i);
                                break;
                            }
                        }

                        btnSave.setOnClickListener(view -> {
                            String comment = String.valueOf(etComment.getText());
                            float score = Float.valueOf(etScore.getText().toString());
                            String answer = String.valueOf(tvStudentAnswer.getText());
                            String selectedState = spinnerState.getSelectedItem().toString().toLowerCase();

                            WritingQuizSubmission writingQuizSubmission = new WritingQuizSubmission(submissionId, answer, score, submission.getTaskId(), submission.getUserId(), selectedState, comment);
                            writingTestViewModel.saveWritingQuizSubmission(writingQuizSubmission,  submission.getTaskId(), submission.getUserId(), new WritingQuizSubmissionRepository.SubmissionCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    Toast.makeText(GradingWritingTestActivity.this, "Grading successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(GradingWritingTestActivity.this, GradingTestActivity.class);
                                    intent.putExtra("courseId", getIntent().getStringExtra("courseId"));
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Toast.makeText(GradingWritingTestActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    }
                });
            }
        });



        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void handleEditTextScroll(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true); // Ngăn ScrollView bắt sự kiện

        // Khi ngón tay nhấc ra, cho ScrollView bắt lại
        if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
            v.getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    private void setUpTVClearComment() {
        tvClearComment.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tvClearComment.setPaintFlags(tvClearComment.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    tvClearComment.setPaintFlags(tvClearComment.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                    break;
            }
            return false;
        });


        tvClearComment.setOnClickListener(view -> {
            etComment.setText("");
        });
    }
}
