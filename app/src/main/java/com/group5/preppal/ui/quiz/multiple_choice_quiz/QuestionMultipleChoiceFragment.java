package com.group5.preppal.ui.quiz.multiple_choice_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.data.model.MultipleChoiceAnsweredQuestion;
import com.group5.preppal.data.model.Question;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.data.repository.UserRepository;
import com.group5.preppal.ui.course.CourseDetailActivity;
import com.group5.preppal.viewmodel.MultipleChoiceQuizViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuestionMultipleChoiceFragment extends Fragment {
    private static final String ARG_QUESTION_INDEX = "questionIndex";
    private static final String ARG_QUESTIONS = "questions";

    @Inject
    AuthRepository authRepository;

    FirebaseUser user;

    private MultipleChoiceQuizViewModel quizViewModel;

    private String quizId;
    private float passPoint;

    private int questionIndex;
    private List<Question> questions;
    private TextView questionName, questionOrder, questionPoint;
    private RadioGroup answerGroup;
    private LinearLayout btnNext, btnPrevious;
    private TextView btnSubmit;
    private String courseId;


    public static QuestionMultipleChoiceFragment newInstance(int questionIndex, List<Question> questions, String quizId, float passPoint, String courseId) {
        QuestionMultipleChoiceFragment fragment = new QuestionMultipleChoiceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_INDEX, questionIndex);
        args.putSerializable(ARG_QUESTIONS, (java.io.Serializable) questions);
        args.putString("quizId", quizId);
        args.putFloat("passPoint", passPoint);
        args.putString("courseId", courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionIndex = getArguments().getInt(ARG_QUESTION_INDEX);
            questions = (List<Question>) getArguments().getSerializable(ARG_QUESTIONS);
            quizId = getArguments().getString("quizId");
            passPoint = getArguments().getFloat("passPoint");
            courseId = getArguments().getString("courseId");
            user = authRepository.getCurrentUser();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_multiple_choice, container, false);

        questionName = view.findViewById(R.id.questionName);
        questionOrder = view.findViewById(R.id.questionOrder);
        questionPoint = view.findViewById(R.id.questionPoint);
        answerGroup = view.findViewById(R.id.answerGroup);
        btnNext = getActivity().findViewById(R.id.btnNext);
        btnPrevious = getActivity().findViewById(R.id.btnPrevious);
        btnSubmit = getActivity().findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            float score = 0;
            List<MultipleChoiceAnsweredQuestion> answeredQuestions = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                Question currentQuestion = questions.get(i);
                Integer selectedOptionIndex = quizViewModel.getSavedAnswer(i);
                if (selectedOptionIndex != null) {
                    boolean isCorrect = currentQuestion.getOptions().get(selectedOptionIndex).isCorrect();
                    if (isCorrect) {
                        score += currentQuestion.getPoint();
                    }
                    answeredQuestions.add(new MultipleChoiceAnsweredQuestion(
                            currentQuestion.getId(),
                            currentQuestion.getOptions().get(selectedOptionIndex).getAnswer(),
                            isCorrect
                    ));
                }
                else {
                    answeredQuestions.add(new MultipleChoiceAnsweredQuestion(
                            currentQuestion.getId(),
                            "",
                            false
                    ));
                }
            }
            quizViewModel.saveQuizResult(user.getUid(), quizId, score, passPoint, answeredQuestions);
            Intent intent = new Intent(requireContext(), CourseDetailActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });



        quizViewModel = new ViewModelProvider(requireActivity()).get(MultipleChoiceQuizViewModel.class);

        loadQuestion(inflater);

        btnNext.setOnClickListener(v -> navigateToQuestion(questionIndex + 1));
        btnPrevious.setOnClickListener(v -> navigateToQuestion(questionIndex - 1));

        return view;
    }

    private void loadQuestion(LayoutInflater inflater) {
        if (questionIndex < 0 || questionIndex >= questions.size()) return;

        Question currentQuestion = questions.get(questionIndex);
        questionName.setText(currentQuestion.getQuestionName());


        questionPoint.setText(currentQuestion.getPoint() + " points");
        questionOrder.setText("Question " + (questionIndex + 1) + "/" + questions.size());

        answerGroup.removeAllViews();

        Integer savedAnswerIndex = quizViewModel.getSavedAnswer(questionIndex);


        for (int i = 0; i < currentQuestion.getOptions().size(); i++) {
            View optionView = inflater.inflate(R.layout.item_answer_option, answerGroup, false);
            LinearLayout layout = optionView.findViewById(R.id.answerOptionLayout);
            RadioButton radioButton = optionView.findViewById(R.id.answerRadioButton);

            radioButton.setText(currentQuestion.getOptions().get(i).getAnswer());

            if (savedAnswerIndex != null && savedAnswerIndex == i) {
                radioButton.setChecked(true);
                layout.setBackgroundResource(R.drawable.radio_option_selected);
            }

            int finalI = i;

            // Cho phép click vào cả LinearLayout để chọn RadioButton
            layout.setOnClickListener(view -> {
                radioButton.setChecked(true);
                updateSelection(radioButton);
                quizViewModel.saveAnswer(questionIndex, finalI);
            });

            // Cho phép click vào RadioButton trực tiếp
            radioButton.setOnClickListener(view -> {
                updateSelection(radioButton);
                quizViewModel.saveAnswer(questionIndex, finalI);
            });

            answerGroup.addView(optionView);
        }

        btnPrevious.setVisibility(questionIndex == 0 ? View.INVISIBLE : View.VISIBLE);
        btnNext.setVisibility(questionIndex == questions.size() - 1 ? View.INVISIBLE : View.VISIBLE);
        btnSubmit.setVisibility(questionIndex == questions.size() - 1 ? View.VISIBLE : View.INVISIBLE);

    }

    private void navigateToQuestion(int newIndex) {
        if (newIndex >= 0 && newIndex < questions.size()) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.quizFragmentContainer, QuestionMultipleChoiceFragment.newInstance(newIndex, questions,quizId, passPoint, courseId))
                    .commit();
        }
    }

    private void updateSelection(RadioButton selectedRadioButton) {
        for (int i = 0; i < answerGroup.getChildCount(); i++) {
            View optionView = answerGroup.getChildAt(i);
            LinearLayout layout = optionView.findViewById(R.id.answerOptionLayout);
            RadioButton radioButton = optionView.findViewById(R.id.answerRadioButton);

            if (radioButton == selectedRadioButton) {
                layout.setBackgroundResource(R.drawable.radio_option_selected);
                radioButton.setChecked(true);
            } else {
                layout.setBackgroundResource(R.drawable.radio_option_unselected);
                radioButton.setChecked(false);
            }
        }
    }
}
