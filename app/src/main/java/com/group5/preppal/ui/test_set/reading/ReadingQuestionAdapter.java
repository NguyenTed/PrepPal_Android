package com.group5.preppal.ui.test_set.reading;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.reading.ReadingQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReadingQuestionAdapter extends RecyclerView.Adapter<ReadingQuestionAdapter.QuestionViewHolder> {

    private List<ReadingQuestion> questions = new ArrayList<>();
    private List<String> groupCorrectAnswers = new ArrayList<>();
    private String groupType = "";
    private List<String> groupOptions = new ArrayList<>();
    private final Map<Integer, String> userAnswers;;
    private boolean isTimeUp = false;
    private final boolean isReviewMode;

    public ReadingQuestionAdapter(Map<Integer, String> userAnswers, boolean isReviewMode) {
        this.userAnswers = userAnswers;
        this.isReviewMode = isReviewMode;
    }

    public void setData(List<ReadingQuestion> questions, String groupType, List<String> groupOptions, List<String> groupCorrectAnswers, boolean isTimeUp) {
        this.questions = questions != null ? questions : new ArrayList<>();
        this.groupType = groupType != null ? groupType : "";
        this.groupOptions = groupOptions != null ? groupOptions : new ArrayList<>();
        this.groupCorrectAnswers = groupCorrectAnswers != null ? groupCorrectAnswers : new ArrayList<>();
        this.isTimeUp = isTimeUp;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listening_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        ReadingQuestion question = questions.get(position);
        int qNum = question.getNumber();
        String savedAnswer = userAnswers.get(qNum);

        boolean isUnanswered = (savedAnswer == null || savedAnswer.trim().isEmpty());
        if (isReviewMode && isUnanswered) {
            holder.unansweredWarning.setVisibility(View.VISIBLE);
        } else {
            holder.unansweredWarning.setVisibility(View.GONE);
        }

        holder.questionNumber.setText("Question " + qNum);

        holder.answerEditText.setVisibility(View.GONE);
        holder.optionsGroup.setVisibility(View.GONE);
        holder.matchSpinner.setVisibility(View.GONE);

        if (isTimeUp) {
            holder.answerEditText.setEnabled(false);
            holder.matchSpinner.setEnabled(false);
        }

        switch (groupType.toUpperCase()) {
            case "TEXT_FILL":
            case "SHORT_ANSWER":
                holder.answerEditText.setVisibility(isReviewMode ? View.GONE : View.VISIBLE);
                holder.matchSpinner.setVisibility(View.GONE);
                holder.layoutCheckboxOptions.setVisibility(isReviewMode ? View.VISIBLE : View.GONE);
                holder.layoutCheckboxOptions.removeAllViews();

                if (!isReviewMode) {
                    holder.answerEditText.setText(savedAnswer != null ? savedAnswer : "");
                    holder.answerEditText.setEnabled(true);

                    holder.answerEditText.addTextChangedListener(new TextWatcher() {
                        @Override public void afterTextChanged(Editable s) {
                            userAnswers.put(qNum, s.toString());
                        }
                        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    });
                } else {
                    Context context = holder.itemView.getContext();
                    List<String> correctAnswers = question.getCorrectAnswers();

                    if (savedAnswer == null || savedAnswer.trim().isEmpty()) {
                        for (String correct : correctAnswers) {
                            holder.layoutCheckboxOptions.addView(makeBoxText(context, correct, R.drawable.bg_correct_unselected));
                        }
                    } else if (correctAnswers != null && correctAnswers.stream().anyMatch(ans -> ans.equalsIgnoreCase(savedAnswer.trim()))) {
                        // ✅ Correct
                        holder.layoutCheckboxOptions.addView(makeBoxText(context, savedAnswer, R.drawable.bg_correct_option));
                    } else {
                        // ❌ Wrong → red strikethrough box + green correct(s)
                        holder.layoutCheckboxOptions.addView(makeBoxTextWithStrike(context, savedAnswer.trim(), R.drawable.bg_wrong_option));
                        for (String correct : correctAnswers) {
                            holder.layoutCheckboxOptions.addView(makeBoxText(context, correct, R.drawable.bg_correct_unselected));
                        }
                    }
                }
                break;

            case "MCQ_SINGLE":
                holder.optionsGroup.setVisibility(View.VISIBLE);
                holder.optionsGroup.removeAllViews();
                String selected = userAnswers.get(qNum);
                List<String> correctAnswers = question.getCorrectAnswers();

                for (String opt : question.getOptions()) {
                    RadioButton radio = (RadioButton) LayoutInflater.from(holder.itemView.getContext())
                            .inflate(R.layout.item_option_radio, holder.optionsGroup, false);
                    radio.setText(opt);
                    radio.setId(View.generateViewId());
                    radio.setChecked(opt.equalsIgnoreCase(selected));

                    if (isReviewMode) {
                        radio.setEnabled(false);

                        boolean isSelected = opt.equalsIgnoreCase(selected);
                        boolean isCorrect = correctAnswers != null && correctAnswers.contains(opt);

                        if (isSelected && isCorrect) {
                            // ✅ Correct selected → green bg
                            radio.setBackgroundResource(R.drawable.bg_correct_option);
                        } else if (isSelected && !isCorrect) {
                            // ❌ Wrong selected → red bg
                            radio.setBackgroundResource(R.drawable.bg_wrong_option);
                        } else if (!isSelected && isCorrect) {
                            // ✅ Correct not selected → outline or green border
                            radio.setBackgroundResource(R.drawable.bg_correct_unselected);
                        } else {
                            // neutral
                            radio.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }

                    holder.optionsGroup.addView(radio);
                }

                if (!isReviewMode) {
                    holder.optionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        RadioButton selectedBtn = group.findViewById(checkedId);
                        if (selectedBtn != null) {
                            String value = selectedBtn.getText().toString();
                            userAnswers.put(qNum, value);
                        }
                    });
                }
                break;

            case "MCQ_MULTIPLE":
                // Render this group only ONCE
                if (position != 0) {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    return;
                }

                holder.optionsGroup.setVisibility(View.VISIBLE);
                holder.optionsGroup.removeAllViews();
                holder.answerEditText.setVisibility(View.GONE);
                holder.matchSpinner.setVisibility(View.GONE);

                int firstQ = questions.get(0).getNumber();
                int lastQ = questions.get(questions.size() - 1).getNumber();
                holder.questionNumber.setText("Questions " + firstQ + "–" + lastQ);

                final int maxSelections = questions.size();

                LinkedHashSet<String> selectedAnswers = new LinkedHashSet<>();
                for (ReadingQuestion q : questions) {
                    String saved = userAnswers.get(q.getNumber());
                    if (saved != null) selectedAnswers.add(saved);
                }

                Set<String> correctSet = new HashSet<>(groupCorrectAnswers); // already passed in

                for (String opt : groupOptions) {
                    CheckBox cb = (CheckBox) LayoutInflater.from(holder.itemView.getContext())
                            .inflate(R.layout.item_option_checkbox, holder.optionsGroup, false);
<<<<<<< HEAD
=======

>>>>>>> fe2aedfd2e940c88a8abee7eea8b67b5dee92d36
                    cb.setText(opt);
                    cb.setChecked(selectedAnswers.contains(opt));
                    cb.setEnabled(!isReviewMode);

                    if (isReviewMode) {
                        boolean isSelected = selectedAnswers.contains(opt);
                        boolean isCorrect = correctSet.contains(opt);

                        if (isSelected && isCorrect) {
                            cb.setBackgroundResource(R.drawable.bg_correct_option);
                        } else if (isSelected && !isCorrect) {
                            cb.setBackgroundResource(R.drawable.bg_wrong_option);
                        } else if (!isSelected && isCorrect) {
                            cb.setBackgroundResource(R.drawable.bg_correct_unselected);
                        } else {
                            cb.setBackgroundResource(R.drawable.bg_neutral_option);
                        }
                    }

                    if (!isReviewMode) {
                        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            String value = cb.getText().toString();

                            if (isChecked) {
                                if (!selectedAnswers.contains(value)) {
                                    if (selectedAnswers.size() >= maxSelections) {
                                        String first = selectedAnswers.iterator().next();
                                        selectedAnswers.remove(first);

                                        // Uncheck oldest
                                        for (int i = 0; i < holder.optionsGroup.getChildCount(); i++) {
                                            View child = holder.optionsGroup.getChildAt(i);
                                            if (child instanceof CheckBox other &&
                                                    other.getText().toString().equals(first)) {
                                                other.setChecked(false);
                                                break;
                                            }
                                        }
                                    }
                                    selectedAnswers.add(value);
                                }
                            } else {
                                selectedAnswers.remove(value);
                            }

                            int i = 0;
                            for (ReadingQuestion q : questions) {
                                if (i < selectedAnswers.size()) {
                                    String ans = (String) selectedAnswers.toArray()[i];
                                    userAnswers.put(q.getNumber(), ans);
                                } else {
                                    userAnswers.remove(q.getNumber());
                                }
                                i++;
                            }
                        });
                    }

                    holder.optionsGroup.addView(cb);
                }
                break;

            case "MATCHING":
                // Hide unrelated views
                holder.answerEditText.setVisibility(View.GONE);
                holder.layoutCheckboxOptions.setVisibility(View.GONE);
                holder.matchSpinner.setVisibility(View.GONE);

                List<String> correctAnswersMatching = question.getCorrectAnswers();
                String answer = userAnswers.get(qNum);

                if (!isReviewMode) {
                    // 🟡 Attempt Mode: Use spinner
                    holder.matchSpinner.setVisibility(View.VISIBLE);

                    List<String> spinnerOptions = new ArrayList<>();
                    spinnerOptions.add("Select an answer");
                    spinnerOptions.addAll(groupOptions);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(),
                            android.R.layout.simple_spinner_item, spinnerOptions) {

                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                            return view;
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.matchSpinner.setAdapter(adapter);

                    int selectedIndex = 0;
                    if (answer != null && spinnerOptions.contains(answer)) {
                        selectedIndex = spinnerOptions.indexOf(answer);
                    }
                    holder.matchSpinner.setSelection(selectedIndex);
                    holder.matchSpinner.setEnabled(true);

                    holder.matchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            if (pos == 0) {
                                userAnswers.remove(qNum);
                            } else {
                                userAnswers.put(qNum, spinnerOptions.get(pos));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                } else {
                    // ✅ Review Mode: Box-style layout
                    holder.layoutCheckboxOptions.setVisibility(View.VISIBLE);
                    holder.layoutCheckboxOptions.removeAllViews();

                    Context context = holder.itemView.getContext();

                    if (answer == null || answer.trim().isEmpty()) {
                        for (String correct : correctAnswersMatching) {
                            holder.layoutCheckboxOptions.addView(makeBoxText(context, correct, R.drawable.bg_correct_unselected));
                        }
                    } else if (correctAnswersMatching != null && correctAnswersMatching.contains(answer)) {
                        holder.layoutCheckboxOptions.addView(makeBoxText(context, answer, R.drawable.bg_correct_option));
                    } else {
                        holder.layoutCheckboxOptions.addView(makeBoxText(context, answer, R.drawable.bg_wrong_option));
                        for (String correct : correctAnswersMatching) {
                            holder.layoutCheckboxOptions.addView(makeBoxText(context, correct, R.drawable.bg_correct_unselected));
                        }
                    }
                }
                break;

            case "TRUE_FALSE_NG":
            case "YES_NO_NG":
                holder.answerEditText.setVisibility(View.GONE);
                holder.matchSpinner.setVisibility(View.GONE);
                holder.layoutCheckboxOptions.setVisibility(View.GONE);
                holder.optionsGroup.setVisibility(View.VISIBLE);
                holder.optionsGroup.removeAllViews();

                List<String> boolOptions = groupType.equals("TRUE_FALSE_NG")
                        ? Arrays.asList("True", "False", "Not Given")
                        : Arrays.asList("Yes", "No", "Not Given");

                List<String> correctAnswersTrueFalse = question.getCorrectAnswers();
                String selectedTrueFalse = userAnswers.get(qNum);

                for (String opt : boolOptions) {
                    RadioButton radio = (RadioButton) LayoutInflater.from(holder.itemView.getContext())
                            .inflate(R.layout.item_option_radio, holder.optionsGroup, false);
                    radio.setText(opt);
<<<<<<< HEAD
                    radio.setEnabled(!isTimeUp);
                    radio.setId(View.generateViewId()); // Rất quan trọng để tránh id bị trùng!
                    if (opt.equals(savedAnswer)) {
                        radio.setChecked(true);
                    }
                    holder.optionsGroup.addView(radio);
                }

                // Set listener cho cả nhóm
                holder.optionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    if (checkedId != -1) {
                        RadioButton isselected = group.findViewById(checkedId);
                        if (isselected != null) {
                            userAnswers.put(qNum, isselected.getText().toString());
                        }
                    }
                });
=======
                    radio.setId(View.generateViewId());

                    radio.setChecked(opt.equalsIgnoreCase(selectedTrueFalse));
                    radio.setEnabled(!isReviewMode);

                    if (isReviewMode) {
                        boolean isSelected = opt.equalsIgnoreCase(selectedTrueFalse);
                        boolean isCorrect = correctAnswersTrueFalse != null && correctAnswersTrueFalse.contains(opt);

                        if (isSelected && isCorrect) {
                            radio.setBackgroundResource(R.drawable.bg_correct_option);
                        } else if (isSelected && !isCorrect) {
                            radio.setBackgroundResource(R.drawable.bg_wrong_option);
                        } else if (!isSelected && isCorrect) {
                            radio.setBackgroundResource(R.drawable.bg_correct_unselected);
                        } else {
                            radio.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }

                    holder.optionsGroup.addView(radio);
                }

                if (!isReviewMode) {
                    holder.optionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        if (checkedId != -1) {
                            RadioButton selectedBtn = group.findViewById(checkedId);
                            if (selectedBtn != null) {
                                userAnswers.put(qNum, selectedBtn.getText().toString());
                            }
                        }
                    });
                }
>>>>>>> fe2aedfd2e940c88a8abee7eea8b67b5dee92d36
                break;
        }
    }

    private TextView makeBoxText(Context context, String text, int backgroundResId) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(16);
        tv.setPadding(20, 32, 20, 32); // Bigger touch target
        tv.setBackgroundResource(backgroundResId);
        tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL); // 👈 Align left, center vertically

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 8);
        tv.setLayoutParams(params);

        return tv;
    }

    private TextView makeBoxTextWithStrike(Context context, String text, int backgroundResId) {
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new StrikethroughSpan(), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tv = new TextView(context);
        tv.setText(spannable);
        tv.setTextSize(16);
        tv.setPadding(20, 32, 20, 32);
        tv.setBackgroundResource(backgroundResId);
        tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 8);
        tv.setLayoutParams(params);

        return tv;
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumber;
        TextView unansweredWarning;
        EditText answerEditText;
        RadioGroup optionsGroup;
        Spinner matchSpinner;
        LinearLayout layoutCheckboxOptions;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            unansweredWarning = itemView.findViewById(R.id.tvUnansweredWarning);
            answerEditText = itemView.findViewById(R.id.etAnswer);
            optionsGroup = itemView.findViewById(R.id.radioGroupOptions);
            matchSpinner = itemView.findViewById(R.id.spinnerMatch);
            layoutCheckboxOptions = itemView.findViewById(R.id.layoutCheckboxOptions);
        }
    }
}

