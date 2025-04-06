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

import java.util.ArrayList;
import java.util.List;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestViewHolder> {

    private final List<Test> testList = new ArrayList<>();
    private final OnTestClickListener listener;

    public interface OnTestClickListener {
        void onWritingClick(Test test);
        void onSpeakingClick(Test test);
    }

    public TestListAdapter(OnTestClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Test> newTests) {
        testList.clear();
        testList.addAll(newTests);
        notifyDataSetChanged();
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
        holder.btnWriting.setOnClickListener(v -> listener.onWritingClick(test));
        holder.btnSpeaking.setOnClickListener(v -> listener.onSpeakingClick(test));
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView testTitle;
        Button btnWriting, btnSpeaking;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            testTitle = itemView.findViewById(R.id.testTitleTextView);
            btnWriting = itemView.findViewById(R.id.btnWriting);
            btnSpeaking = itemView.findViewById(R.id.btnSpeaking);
        }
    }
}