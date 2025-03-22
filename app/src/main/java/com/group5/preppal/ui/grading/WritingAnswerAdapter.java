package com.group5.preppal.ui.grading;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.WritingAnswerItem;
import java.util.List;

public class WritingAnswerAdapter extends RecyclerView.Adapter<WritingAnswerAdapter.ViewHolder> {
    private List<WritingAnswerItem> answerList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(WritingAnswerItem item);
    }

    public WritingAnswerAdapter(List<WritingAnswerItem> answerList, OnItemClickListener listener) {
        this.answerList = answerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_writng_answer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WritingAnswerItem item = answerList.get(position);
        holder.textView.setText(item.getTopicTitle());

        // ✅ Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvTopicTitle);
        }
    }
}
