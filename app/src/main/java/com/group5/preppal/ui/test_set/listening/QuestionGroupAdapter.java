package com.group5.preppal.ui.test_set.listening;

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
import com.group5.preppal.data.model.test.listening.QuestionGroup;

import java.util.ArrayList;
import java.util.List;

public class QuestionGroupAdapter extends RecyclerView.Adapter<QuestionGroupAdapter.GroupViewHolder> {

    private final List<QuestionGroup> groupList = new ArrayList<>();

    public void submitList(List<QuestionGroup> newList) {
        groupList.clear();
        groupList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        QuestionGroup group = groupList.get(position);

        Log.d("QuestionGroupAdapter"," Group Type: " + group.getType());

        Glide.with(holder.itemView.getContext())
                .load(group.getImageUrl())
                .into(holder.groupImage);

        ListeningQuestionAdapter questionAdapter = new ListeningQuestionAdapter();
        holder.questionRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.questionRecyclerView.setAdapter(questionAdapter);
        questionAdapter.setData(group.getQuestions(), group.getType(), group.getOptions());
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
