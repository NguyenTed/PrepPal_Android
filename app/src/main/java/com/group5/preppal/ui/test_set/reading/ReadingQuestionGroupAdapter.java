package com.group5.preppal.ui.test_set.reading;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group5.preppal.R;
import com.group5.preppal.data.model.test.reading.ReadingQuestionGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadingQuestionGroupAdapter extends RecyclerView.Adapter<ReadingQuestionGroupAdapter.GroupViewHolder> {

    private final List<ReadingQuestionGroup> groupList = new ArrayList<>();
    private Map<Integer, String> userAnswers = new HashMap<>();
    private boolean isTimeUp = false;

    public void submitList(List<ReadingQuestionGroup> newList) {
        groupList.clear();
        groupList.addAll(newList);
        notifyDataSetChanged();
    }

    public void setUserAnswers(Map<Integer, String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public void setTimeUp(boolean isTimeUp) {
        this.isTimeUp = isTimeUp;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reading_question_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        ReadingQuestionGroup group = groupList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(group.getImageUrl())
                .into(holder.groupImage);

        Log.d("ReadingGroupAdapter", "Binding group of type: " + group.getType());

        ReadingQuestionAdapter adapter = new ReadingQuestionAdapter(userAnswers);
        holder.questionRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.questionRecyclerView.setAdapter(adapter);

        adapter.setData(group.getQuestions(), group.getType(), group.getOptions(), isTimeUp);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView groupImage;
        RecyclerView questionRecyclerView;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.groupImageView);
            questionRecyclerView = itemView.findViewById(R.id.questionRecyclerView);
        }
    }
}

