package com.group5.preppal.ui.quiz.multiple_choice_quiz;

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

import com.group5.preppal.R;
import com.group5.preppal.data.model.Question;

import java.util.List;

public class QuestionMultipleChoiceFragment extends Fragment {
    private static final String ARG_QUESTION_INDEX = "questionIndex";
    private static final String ARG_QUESTIONS = "questions";

    private int questionIndex;
    private List<Question> questions;
    private TextView questionName;
    private RadioGroup answerGroup;
    private LinearLayout btnNext, btnPrevious;

    public static QuestionMultipleChoiceFragment newInstance(int questionIndex, List<Question> questions) {
        QuestionMultipleChoiceFragment fragment = new QuestionMultipleChoiceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_INDEX, questionIndex);
        args.putSerializable(ARG_QUESTIONS, (java.io.Serializable) questions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionIndex = getArguments().getInt(ARG_QUESTION_INDEX);
            questions = (List<Question>) getArguments().getSerializable(ARG_QUESTIONS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_multiple_choice, container, false);

        questionName = view.findViewById(R.id.questionName);
        answerGroup = view.findViewById(R.id.answerGroup);
        btnNext = getActivity().findViewById(R.id.btnNext);
        btnPrevious = getActivity().findViewById(R.id.btnPrevious);

        loadQuestion(inflater);

        btnNext.setOnClickListener(v -> navigateToQuestion(questionIndex + 1));
        btnPrevious.setOnClickListener(v -> navigateToQuestion(questionIndex - 1));

        return view;
    }

    private void loadQuestion(LayoutInflater inflater) {
        if (questionIndex < 0 || questionIndex >= questions.size()) return;

        Question currentQuestion = questions.get(questionIndex);
        questionName.setText(currentQuestion.getQuestionName());

        answerGroup.removeAllViews();

        for (int i = 0; i < currentQuestion.getOptions().size(); i++) {
            View optionView = inflater.inflate(R.layout.item_answer_option, answerGroup, false);
            LinearLayout layout = optionView.findViewById(R.id.answerOptionLayout);
            RadioButton radioButton = optionView.findViewById(R.id.answerRadioButton);

            radioButton.setText(currentQuestion.getOptions().get(i).getAnswer());

            // Cho phép click vào cả LinearLayout để chọn RadioButton
            layout.setOnClickListener(view -> {
                radioButton.setChecked(true);
                updateSelection(radioButton);
            });

            // Cho phép click vào RadioButton trực tiếp
            radioButton.setOnClickListener(view -> updateSelection(radioButton));

            answerGroup.addView(optionView);
        }

        btnPrevious.setVisibility(questionIndex == 0 ? View.INVISIBLE : View.VISIBLE);
        btnNext.setVisibility(questionIndex == questions.size() - 1 ? View.INVISIBLE : View.VISIBLE);
    }

    private void navigateToQuestion(int newIndex) {
        if (newIndex >= 0 && newIndex < questions.size()) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.quizFragmentContainer, QuestionMultipleChoiceFragment.newInstance(newIndex, questions))
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
