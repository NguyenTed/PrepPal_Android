package com.group5.preppal.ui.test_set.listening;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.group5.preppal.data.model.test.listening.ListeningQuestion;

import java.util.ArrayList;
import java.util.List;

public class ListeningQuestionAdapter extends RecyclerView.Adapter<ListeningQuestionAdapter.QuestionViewHolder> {

    private List<ListeningQuestion> questions = new ArrayList<>();
    private String groupType = "";
    private List<String> groupOptions = new ArrayList<>();

    public void setData(List<ListeningQuestion> questions, String groupType, List<String> groupOptions) {
        this.questions = questions != null ? questions : new ArrayList<>();
        this.groupType = groupType != null ? groupType : "";
        this.groupOptions = groupOptions != null ? groupOptions : new ArrayList<>();
        Log.d("QUESTION_ADAPTER", "setData called with " + questions.size() + " questions, groupType: " + groupType);
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
        ListeningQuestion question = questions.get(position);
        holder.questionNumber.setText("Q" + question.getNumber());

        holder.answerEditText.setVisibility(View.GONE);
        holder.optionsGroup.setVisibility(View.GONE);
        holder.matchSpinner.setVisibility(View.GONE);

        Log.d("QUESTION_ADAPTER", "Binding Q" + question.getNumber() + " type: " + groupType);

        switch (groupType.toUpperCase()) {
            case "TEXT_FILL":
            case "SHORT_ANSWER":
                holder.answerEditText.setVisibility(View.VISIBLE);
                break;
            case "MCQ_SINGLE":
                holder.optionsGroup.setVisibility(View.VISIBLE);
                holder.optionsGroup.removeAllViews();
                for (String opt : question.getOptions()) {
                    RadioButton radio = new RadioButton(holder.itemView.getContext());
                    radio.setText(opt);
                    holder.optionsGroup.addView(radio);
                }
                break;
            case "MCQ_MULTIPLE":
                holder.optionsGroup.setVisibility(View.VISIBLE);
                holder.optionsGroup.removeAllViews();
                for (String opt : groupOptions) {
                    CheckBox cb = new CheckBox(holder.itemView.getContext());
                    cb.setText(opt);
                    holder.optionsGroup.addView(cb);
                }
                break;
            case "MATCHING":
                holder.matchSpinner.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        holder.itemView.getContext(),
                        android.R.layout.simple_spinner_item,
                        groupOptions
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.matchSpinner.setAdapter(adapter);
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