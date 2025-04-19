package com.group5.preppal.ui.quiz.multiple_choice_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.data.model.MultipleChoiceQuestion;
import com.group5.preppal.data.model.MultipleChoiceQuizResult;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.ui.course.CourseDetailActivity;
import com.group5.preppal.viewmodel.MultipleChoiceQuizViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MultipleChoiceAnswerActivity extends AppCompatActivity {
    private MultipleChoiceQuizViewModel quizViewModel;
    private String quizId;
    private String courseId;
    private ImageButton backBtn;
    private TextView quizName;
    private LinearLayout btnTryAgain;
    private MultipleChoiceQuizResult multipleChoiceQuizResult;

    @Inject
    AuthRepository authRepository;
    FirebaseUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_answer);

        quizId = getIntent().getStringExtra("quizId");
        courseId = getIntent().getStringExtra("courseId");
        quizName = findViewById(R.id.quizName);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        backBtn = findViewById(R.id.backButton);
        quizViewModel = new ViewModelProvider(this).get(MultipleChoiceQuizViewModel.class);
        user = authRepository.getCurrentUser();

        if (user != null && quizId != null) {
            quizViewModel.getQuizResult(quizId).observe(this, result -> {
                if (result != null) {
                    multipleChoiceQuizResult = result;
                    quizViewModel.getAnsweredQuizByResult(multipleChoiceQuizResult).observe(this, quiz -> {
                        Toast.makeText(this, "No null1", Toast.LENGTH_SHORT).show();
                        if (quiz != null && quiz.getQuestions() != null) {
                            Toast.makeText(this, "No null", Toast.LENGTH_SHORT).show();
                            List<String> questionIds = quiz.getQuestionIds();
                            int size = (questionIds != null) ? questionIds.size() : 0;
                            Toast.makeText(this, "Question size: " + size, Toast.LENGTH_SHORT).show();
                            quizName.setText(quiz.getName());
                            loadFirstQuestion(
                                    quiz.getQuestions(),
                                    quizId,
                                    quiz.getPassPoint(),
                                    multipleChoiceQuizResult
                            );
                        } else Toast.makeText(this, "No Result ", Toast.LENGTH_SHORT).show();
                    });
                }  else {
                    Log.d("CHECK", "User or quizId is null");
                    Toast.makeText(this, "Missing user or quizId", Toast.LENGTH_SHORT).show();
                }
            });
        }

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });

        btnTryAgain.setOnClickListener(view -> {
            Intent intent = new Intent(this, MultipleChoiceActivity.class);
            intent.putExtra("quizId", quizId);
            intent.putExtra("courseId", courseId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });



    }
    private void loadFirstQuestion(List<MultipleChoiceQuestion> multipleChoiceQuestions, String quizId, float passPoint, MultipleChoiceQuizResult multipleChoiceQuizResult) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.quizAnswerFragmentContainer, QuestionMultipleChoiceAnswerFragment.newInstance(0, multipleChoiceQuestions, quizId, passPoint, multipleChoiceQuizResult))
                .commit();
    }
}
