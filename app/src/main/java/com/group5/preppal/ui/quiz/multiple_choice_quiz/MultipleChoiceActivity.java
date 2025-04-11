package com.group5.preppal.ui.quiz.multiple_choice_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
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
public class MultipleChoiceActivity extends AppCompatActivity {
    private MultipleChoiceQuizViewModel quizViewModel;
    private String quizId;
    private ImageButton backBtn;
    private TextView quizName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        quizId = getIntent().getStringExtra("quizId");
        quizName = findViewById(R.id.quizName);
        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra("courseId", getIntent().getStringExtra("courseId"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        quizViewModel = new ViewModelProvider(this).get(MultipleChoiceQuizViewModel.class);
//        Log.d("quizId", "quizId: " + quizId);
        quizViewModel.getQuizById(quizId).observe(this, quiz -> {
            if (quiz != null) {
//                Log.d("Question length: ", "Question length: " + quiz.getQuestions().size());
                loadFirstQuestion(quiz.getQuestions(), quizId, quiz.getPassPoint());
            }

            quizName.setText(quiz.getName());
        });

    }
    private void loadFirstQuestion(List<MultipleChoiceQuestion> multipleChoiceQuestions, String quizId, float passPoint) {
        String courseId = getIntent().getStringExtra("courseId");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.quizFragmentContainer, QuestionMultipleChoiceFragment.newInstance(0, multipleChoiceQuestions, quizId, passPoint, courseId))
                .commit();
    }
}
