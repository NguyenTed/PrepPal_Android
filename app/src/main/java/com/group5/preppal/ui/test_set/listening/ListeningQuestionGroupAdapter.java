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
import com.group5.preppal.data.model.test.listening.ListeningQuestionGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeningQuestionGroupAdapter extends RecyclerView.Adapter<ListeningQuestionGroupAdapter.GroupViewHolder> {

    private final List<ListeningQuestionGroup> groupList = new ArrayList<>();
    private final List<ListeningQuestionAdapter> questionAdapters = new ArrayList<>();
    private Map<Integer, String> userAnswers = new HashMap<>();
    private boolean isTimeUp;

    public void submitList(List<ListeningQuestionGroup> newList) {
        groupList.clear();
        groupList.addAll(newList);
        notifyDataSetChanged();
    }

    public void setTimeUp(boolean isTimeUp) {
        this.isTimeUp = isTimeUp;
    }

    public void setUserAnswers(Map<Integer, String> userAnswers) {
        this.userAnswers = userAnswers;
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
        ListeningQuestionGroup group = groupList.get(position);

        Log.d("ListeningQuestionGroupAdapter"," Group Type: " + group.getType());

        Glide.with(holder.itemView.getContext())
                .load(group.getImageUrl())
                .into(holder.groupImage);

        ListeningQuestionAdapter questionAdapter = new ListeningQuestionAdapter();
        questionAdapter.setTimeUp(isTimeUp);
        holder.questionRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.questionRecyclerView.setAdapter(questionAdapter);
        questionAdapter.setData(group.getQuestions(), group.getType(), group.getOptions(), userAnswers);

        if (!questionAdapters.contains(questionAdapter)) {
            questionAdapters.add(questionAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void disableAllInputs() {
        for (ListeningQuestionAdapter adapter : questionAdapters) {
            adapter.disableInputs();
        }
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
