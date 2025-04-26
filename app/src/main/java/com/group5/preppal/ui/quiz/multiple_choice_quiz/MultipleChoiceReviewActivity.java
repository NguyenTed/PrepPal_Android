package com.group5.preppal.ui.quiz.multiple_choice_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.group5.preppal.R;
import com.group5.preppal.data.model.MultipleChoiceQuestion;
import com.group5.preppal.data.model.MultipleChoiceQuizResult;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MultipleChoiceReviewActivity extends AppCompatActivity {

    private String quizId;
    private String courseId;
    private MultipleChoiceQuizResult quizResult;
    private List<MultipleChoiceQuestion> multipleChoiceQuestions;

    private ImageButton backButton;
    private TextView quizName;
    private LinearLayout btnTryAgain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_answer);

        quizId = getIntent().getStringExtra("quizId");
        courseId = getIntent().getStringExtra("courseId");
        quizResult = (MultipleChoiceQuizResult) getIntent().getSerializableExtra("quizResult");
        multipleChoiceQuestions = (List<MultipleChoiceQuestion>) getIntent().getSerializableExtra("questions");

        backButton = findViewById(R.id.backButton);
        quizName = findViewById(R.id.quizName);
        btnTryAgain = findViewById(R.id.btnTryAgain);

        quizName.setText("Review Answers"); // Hoặc lấy tên từ quizResult nếu có

        loadFirstQuestion();

        backButton.setOnClickListener(view -> finish());

        btnTryAgain.setOnClickListener(view -> {
            Intent intent = new Intent(this, MultipleChoiceActivity.class);
            intent.putExtra("quizId", quizId);
            intent.putExtra("courseId", courseId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadFirstQuestion() {
        if (multipleChoiceQuestions == null || multipleChoiceQuestions.isEmpty()) return;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.quizAnswerFragmentContainer,
                        QuestionMultipleChoiceAnswerFragment.newInstance(
                                0,
                                multipleChoiceQuestions,
                                quizId,
                                0,
                                quizResult))
                .commit();
    }
}
