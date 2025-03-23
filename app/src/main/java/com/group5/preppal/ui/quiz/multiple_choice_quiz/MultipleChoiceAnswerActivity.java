package com.group5.preppal.ui.quiz.multiple_choice_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.data.model.MultipleChoiceQuestion;
import com.group5.preppal.ui.course.CourseDetailActivity;
import com.group5.preppal.viewmodel.MultipleChoiceQuizViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MultipleChoiceAnswerActivity extends AppCompatActivity {
    private MultipleChoiceQuizViewModel quizViewModel;
    private String quizId;
    private ImageButton backBtn;
    private TextView quizName;
    private LinearLayout btnTryAgain;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_answer);

        quizId = getIntent().getStringExtra("quizId");
        quizName = findViewById(R.id.quizName);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra("courseId", getIntent().getStringExtra("courseId"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnTryAgain.setOnClickListener(view -> {
            Intent intent = new Intent(this, MultipleChoiceActivity.class);
            intent.putExtra("quizId", quizId);
            intent.putExtra("courseId", getIntent().getStringExtra("courseId"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        quizViewModel = new ViewModelProvider(this).get(MultipleChoiceQuizViewModel.class);

        quizViewModel.getQuizById(quizId).observe(this, quiz -> {
            if (quiz != null) {
                loadFirstQuestion(quiz.getQuestions(), quizId, quiz.getPassPoint());
            }

            quizName.setText(quiz.getName());
        });

    }
    private void loadFirstQuestion(List<MultipleChoiceQuestion> multipleChoiceQuestions, String quizId, float passPoint) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.quizAnswerFragmentContainer, QuestionMultipleChoiceAnswerFragment.newInstance(0, multipleChoiceQuestions, quizId, passPoint))
                .commit();
    }
}
