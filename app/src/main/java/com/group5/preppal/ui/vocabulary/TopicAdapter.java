package com.group5.preppal.ui.vocabulary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.TopicWithProgress;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<TopicWithProgress> topics = new ArrayList<>();
    private final OnTopicClickListener clickListener;

    public interface OnTopicClickListener {
        void onClick(TopicWithProgress topic);
    }

    public TopicAdapter(OnTopicClickListener listener) {
        this.clickListener = listener;
    }

    public void setTopics(List<TopicWithProgress> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        TopicWithProgress topic = topics.get(position);
        holder.bind(topic);
        holder.itemView.setOnClickListener(v -> clickListener.onClick(topic));
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {

        TextView name, progressCount;
        ProgressBar progressBar;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.topic_name);
            progressCount = itemView.findViewById(R.id.topic_progress_count);
            progressBar = itemView.findViewById(R.id.topic_progress_bar);
        }

        public void bind(TopicWithProgress topic) {
            name.setText(topic.getName());
            String progressText = topic.getLearnedCount() + "/" + topic.getTotalCount();
            progressCount.setText(progressText);

            if (topic.getTotalCount() == 0) {
                progressBar.setProgress(0);
            } else {
                int percent = (int) ((float) topic.getLearnedCount() / topic.getTotalCount() * 100);
                progressBar.setProgress(percent);
            }
        }
    }
}

