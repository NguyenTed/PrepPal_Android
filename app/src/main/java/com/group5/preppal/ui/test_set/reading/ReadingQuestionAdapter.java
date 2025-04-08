package com.group5.preppal.ui.test_set.reading;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class ReadingQuestionAdapter extends RecyclerView.Adapter<ReadingQuestionAdapter.QuestionViewHolder> {

    private List<ReadingQuestion> questions = new ArrayList<>();
    private String groupType = "";
    private List<String> groupOptions = new ArrayList<>();
    private final Map<Integer, String> userAnswers;;
    private boolean isTimeUp = false;

    public ReadingQuestionAdapter(Map<Integer, String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public void setData(List<ReadingQuestion> questions, String groupType, List<String> groupOptions, boolean isTimeUp) {
        this.questions = questions != null ? questions : new ArrayList<>();
        this.groupType = groupType != null ? groupType : "";
        this.groupOptions = groupOptions != null ? groupOptions : new ArrayList<>();
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
                holder.answerEditText.setVisibility(View.VISIBLE);
                holder.answerEditText.setText(savedAnswer != null ? savedAnswer : "");
                holder.answerEditText.addTextChangedListener(new TextWatcher() {
                    @Override public void afterTextChanged(Editable s) {
                        userAnswers.put(qNum, s.toString());
                    }
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                break;

            case "MCQ_SINGLE":
                holder.optionsGroup.setVisibility(View.VISIBLE);
                holder.optionsGroup.removeAllViews();

                RadioGroup radioGroup = holder.optionsGroup;
                String selected = userAnswers.get(question.getNumber());

                for (String opt : question.getOptions()) {
                    RadioButton radio = new RadioButton(holder.itemView.getContext());
                    radio.setText(opt);
                    radio.setId(View.generateViewId());
                    radio.setChecked(opt.equalsIgnoreCase(selected));
                    radioGroup.addView(radio);
                }

                // ✅ Set change listener AFTER adding all radios
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton selectedBtn = group.findViewById(checkedId);
                    if (selectedBtn != null) {
                        String value = selectedBtn.getText().toString();
                        userAnswers.put(question.getNumber(), value);
                        Log.d("MCQ_SINGLE_ANS", "Q" + question.getNumber() + " = " + value);
                    }
                });
                break;

            case "MCQ_MULTIPLE":
                // Render this group only ONCE for the entire group
                if (position != 0) {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    return;
                }

                holder.optionsGroup.setVisibility(View.VISIBLE);
                holder.optionsGroup.removeAllViews();
                holder.answerEditText.setVisibility(View.GONE);
                holder.matchSpinner.setVisibility(View.GONE);

                // Label it like "Questions 19–20"
                int firstQ = questions.get(0).getNumber();
                int lastQ = questions.get(questions.size() - 1).getNumber();
                holder.questionNumber.setText("Questions " + firstQ + "–" + lastQ);

                final int maxSelections = questions.size(); // e.g., 2

                LinkedHashSet<String> selectedAnswers = new LinkedHashSet<>();
                // Pull previous selections from ANY question in group
                for (ReadingQuestion q : questions) {
                    String saved = userAnswers.get(q.getNumber());
                    if (saved != null) selectedAnswers.addAll(Arrays.asList(saved.split(",")));
                }

                for (String opt : groupOptions) {
                    CheckBox cb = new CheckBox(holder.itemView.getContext());
                    cb.setText(opt);
                    cb.setChecked(selectedAnswers.contains(opt));

                    cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        String value = buttonView.getText().toString();

                        if (isChecked) {
                            if (!selectedAnswers.contains(value)) {
                                if (selectedAnswers.size() >= maxSelections) {
                                    // Pop oldest
                                    String first = selectedAnswers.iterator().next();
                                    selectedAnswers.remove(first);
                                    // Uncheck it
                                    for (int i = 0; i < holder.optionsGroup.getChildCount(); i++) {
                                        View child = holder.optionsGroup.getChildAt(i);
                                        if (child instanceof CheckBox) {
                                            CheckBox other = (CheckBox) child;
                                            if (other.getText().toString().equals(first)) {
                                                other.setChecked(false);
                                                break;
                                            }
                                        }
                                    }
                                }
                                selectedAnswers.add(value);
                            }
                        } else {
                            selectedAnswers.remove(value);
                        }

                        // Save to ALL questions in group
                        int i = 0;
                        for (ReadingQuestion q : questions) {
                            if (i < selectedAnswers.size()) {
                                String ans = (String) selectedAnswers.toArray()[i];
                                userAnswers.put(q.getNumber(), ans);
                                Log.d("MCQ_MULTIPLE_SAVE", "Q" + q.getNumber() + " = " + ans);
                            } else {
                                userAnswers.remove(q.getNumber());
                            }
                            i++;
                        }
                    });

                    holder.optionsGroup.addView(cb);
                }
                break;

            case "MATCHING":
                holder.matchSpinner.setVisibility(View.VISIBLE);

                List<String> spinnerOptions = new ArrayList<>();
                spinnerOptions.add("Select an answer"); // 👈 custom placeholder
                spinnerOptions.addAll(groupOptions);    // append the real options

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        holder.itemView.getContext(),
                        android.R.layout.simple_spinner_item,
                        spinnerOptions
                ) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0; // ❌ Disable "Select an answer"
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            tv.setTextColor(Color.GRAY); // visually dim
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.matchSpinner.setAdapter(adapter);

                // restore selection
                if (savedAnswer != null && spinnerOptions.contains(savedAnswer)) {
                    holder.matchSpinner.setSelection(spinnerOptions.indexOf(savedAnswer));
                } else {
                    holder.matchSpinner.setSelection(0); // default to 'Select an answer'
                }

                holder.matchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            userAnswers.remove(question.getNumber());
                        } else {
                            userAnswers.put(question.getNumber(), spinnerOptions.get(position));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                break;

            case "TRUE_FALSE_NG":
            case "YES_NO_NG":
                holder.optionsGroup.setVisibility(View.VISIBLE);
                holder.optionsGroup.removeAllViews();

                List<String> boolOptions = groupType.equals("TRUE_FALSE_NG")
                        ? Arrays.asList("True", "False", "Not Given")
                        : Arrays.asList("Yes", "No", "Not Given");

                for (String opt : boolOptions) {
                    RadioButton radio = new RadioButton(holder.itemView.getContext());
                    radio.setText(opt);
                    radio.setChecked(opt.equals(savedAnswer));
                    radio.setEnabled(!isTimeUp);
                    radio.setOnCheckedChangeListener((btn, checked) -> {
                        if (checked) userAnswers.put(qNum, opt);
                    });
                    holder.optionsGroup.addView(radio);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumber;
        EditText answerEditText;
        RadioGroup optionsGroup;
        Spinner matchSpinner;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            answerEditText = itemView.findViewById(R.id.etAnswer);
            optionsGroup = itemView.findViewById(R.id.radioGroupOptions);
            matchSpinner = itemView.findViewById(R.id.spinnerMatch);
        }
    }
}

