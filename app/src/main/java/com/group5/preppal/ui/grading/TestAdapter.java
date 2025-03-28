package com.group5.preppal.ui.grading;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.TestItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private final List<Map<String, Object>> testList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Map<String, Object> item);
    }

    public TestAdapter(List<Map<String, Object>> testList, OnItemClickListener listener) {
        this.testList = testList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.bind(testList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        private final TextView testTypeTextView;
        private final TextView topicTextView;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            testTypeTextView = itemView.findViewById(R.id.testTypeTextView);
            topicTextView = itemView.findViewById(R.id.topicTextView);
        }

        public void bind(final Map<String, Object> item, final OnItemClickListener listener) {
            testTypeTextView.setText(item.get("name").toString());
            topicTextView.setText(item.get("type").toString());
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
