package com.group5.preppal.ui.test_set;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.Test;
import com.group5.preppal.data.model.test.TestAttempt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestViewHolder> {

    private final List<Test> testList = new ArrayList<>();
    private final OnTestClickListener listener;
    private Map<String, TestAttempt> testAttemptsMap = new HashMap<>();

    public interface OnTestClickListener {
        void onTestClick(Test test);
    }

    public TestListAdapter(OnTestClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Test> newTests) {
        testList.clear();
        testList.addAll(newTests);
        notifyDataSetChanged();
    }

    public void setTestAttemptsMap(Map<String, TestAttempt> map) {
        this.testAttemptsMap = map != null ? map : new HashMap<>();
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_2, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test test = testList.get(position);
        holder.testTitle.setText(test.getName());

        TestAttempt attempt = testAttemptsMap.get(test.getId());

        // Display scores with fallback if not present
        String lScore = (attempt != null && attempt.getListeningBandScore() != null)
                ? String.format(Locale.US, "%.1f", attempt.getListeningBandScore())
                : "-";

        String rScore = (attempt != null && attempt.getReadingBandScore() != null)
                ? String.format(Locale.US, "%.1f", attempt.getReadingBandScore())
                : "-";

        String bandText = "L: " + lScore + " | R: " + rScore;
        holder.bandScores.setText(bandText);

        // Open detail activity on item click
        holder.itemView.setOnClickListener(v -> listener.onTestClick(test));
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView testTitle, bandScores;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            testTitle = itemView.findViewById(R.id.testTitleTextView);
            bandScores = itemView.findViewById(R.id.bandScoresTextView);
        }
    }
}
