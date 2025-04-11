package com.group5.preppal.ui.quiz.multiple_choice_quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.data.model.MultipleChoiceQuizResult;
import com.group5.preppal.data.model.MultipleChoiceQuestion;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.viewmodel.MultipleChoiceQuizViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuestionMultipleChoiceAnswerFragment extends Fragment {
    private static final String ARG_QUESTION_INDEX = "questionIndex";
    private static final String ARG_QUESTIONS = "questions";
    private static final String ARG_QUIZ_ID = "quizId";
    private static final String ARG_PASS_POINT = "passPoint";
    private MultipleChoiceQuizResult multipleChoiceQuizResult;

    @Inject
    AuthRepository authRepository;

    FirebaseUser user;

    private MultipleChoiceQuizViewModel quizViewModel;

    private String quizId;
    private float passPoint;

    private int questionIndex;
    private List<MultipleChoiceQuestion> multipleChoiceQuestions;
    private TextView questionName, questionOrder, questionPoint;
    private RadioGroup answerGroup;
    private LinearLayout btnNext, btnPrevious;


    public static QuestionMultipleChoiceAnswerFragment newInstance(int questionIndex, List<MultipleChoiceQuestion> multipleChoiceQuestions, String quizId, float passPoint) {
        QuestionMultipleChoiceAnswerFragment fragment = new QuestionMultipleChoiceAnswerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_INDEX, questionIndex);
        args.putSerializable(ARG_QUESTIONS, (java.io.Serializable) multipleChoiceQuestions);
        args.putString(ARG_QUIZ_ID, quizId);
        args.putFloat(ARG_PASS_POINT, passPoint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionIndex = getArguments().getInt(ARG_QUESTION_INDEX);
            multipleChoiceQuestions = (List<MultipleChoiceQuestion>) getArguments().getSerializable(ARG_QUESTIONS);
            quizId = getArguments().getString(ARG_QUIZ_ID);
            passPoint = getArguments().getFloat(ARG_PASS_POINT);
        }

        quizViewModel = new ViewModelProvider(requireActivity()).get(MultipleChoiceQuizViewModel.class);
        user = authRepository.getCurrentUser();
        if (user != null) {
            quizViewModel.getQuizResult(quizId).observe(this, new Observer<MultipleChoiceQuizResult>() {
                @Override
                public void onChanged(MultipleChoiceQuizResult result) {
                    if (result != null) {
                        multipleChoiceQuizResult = result;
                        Log.d("Multiple Choice quiz result", "Multiple Choice Quiz result size: " + result.getAnsweredQuestions().size());
                        loadQuestion();
                    }
                }
            });
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



        quizViewModel = new ViewModelProvider(requireActivity()).get(MultipleChoiceQuizViewModel.class);

        loadQuestion();

        btnNext.setOnClickListener(v -> navigateToQuestion(questionIndex + 1));
        btnPrevious.setOnClickListener(v -> navigateToQuestion(questionIndex - 1));

        return view;
    }

    private void loadQuestion() {
        if (multipleChoiceQuizResult == null) {
            return;
        }
        if (questionIndex < 0 || questionIndex >= multipleChoiceQuestions.size()) return;

        MultipleChoiceQuestion currentMultipleChoiceQuestion = multipleChoiceQuestions.get(questionIndex);
        questionName.setText(currentMultipleChoiceQuestion.getQuestionName());


        questionPoint.setText(multipleChoiceQuizResult.getScore() + "/" + currentMultipleChoiceQuestion.getPoint() + " points");
        questionOrder.setText("Question " + (questionIndex + 1) + "/" + multipleChoiceQuestions.size());

        answerGroup.removeAllViews();

//        Log.d("Option size", "Option size: " + currentMultipleChoiceQuestion.getOptions().size());
        for (int i = 0; i < currentMultipleChoiceQuestion.getOptions().size(); i++) {
            View optionView = LayoutInflater.from(getContext()).inflate(R.layout.item_answer_option, answerGroup, false);
            LinearLayout layout = optionView.findViewById(R.id.answerOptionLayout);
            RadioButton radioButton = optionView.findViewById(R.id.answerRadioButton);

            radioButton.setText(currentMultipleChoiceQuestion.getOptions().get(i).getAnswer());
            radioButton.setClickable(false);

            String selectedAnswer = multipleChoiceQuizResult.getAnsweredQuestions().get(questionIndex).getSelectedAnswer();
            boolean isCorrect = multipleChoiceQuizResult.getAnsweredQuestions().get(questionIndex).isCorrect();

            if (Objects.equals(selectedAnswer, currentMultipleChoiceQuestion.getOptions().get(i).getAnswer()))
            {
                radioButton.setChecked(true);
                if (isCorrect) {
                    radioButton.setButtonTintList(ContextCompat.getColorStateList(requireContext(), R.color.radio_button_true));
                    layout.setBackgroundResource(R.drawable.radio_option_selected_true);
                }
                else {
                    radioButton.setButtonTintList(ContextCompat.getColorStateList(requireContext(), R.color.radio_button_false));
                    layout.setBackgroundResource(R.drawable.radio_option_selected_false);
                }
            }
            else layout.setBackgroundResource(R.drawable.radio_option_unselected);

            answerGroup.addView(optionView);
        }

        btnPrevious.setVisibility(questionIndex == 0 ? View.INVISIBLE : View.VISIBLE);
        btnNext.setVisibility(questionIndex == multipleChoiceQuestions.size() - 1 ? View.INVISIBLE : View.VISIBLE);

    }

    private void navigateToQuestion(int newIndex) {
        if (newIndex >= 0 && newIndex < multipleChoiceQuestions.size()) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.quizAnswerFragmentContainer, QuestionMultipleChoiceAnswerFragment.newInstance(newIndex, multipleChoiceQuestions,quizId, passPoint))
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
