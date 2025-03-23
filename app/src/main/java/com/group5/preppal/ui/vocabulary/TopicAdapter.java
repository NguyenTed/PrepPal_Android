package com.group5.preppal.ui.vocabulary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topics = new ArrayList<>();
    private final OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onClick(Topic topic);
    }

    public TopicAdapter(OnTopicClickListener listener) {
        this.listener = listener;
    }

    public void setTopics(List<Topic> topics) {
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
        Topic topic = topics.get(position);
        holder.name.setText(topic.getTopic());

        holder.itemView.setOnClickListener(v -> listener.onClick(topic));
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.topic_name);
        }
    }
}

