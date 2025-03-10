package com.group5.preppal.ui.test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.WritingTest;
import java.util.List;

public class WritingTestAdapter extends RecyclerView.Adapter<WritingTestAdapter.ViewHolder> {
    private Context context;
    private List<WritingTest> writingTests;

    public WritingTestAdapter(Context context, List<WritingTest> writingTests) {
        this.context = context;
        this.writingTests = writingTests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_writing_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WritingTest test = writingTests.get(position);
        holder.tvTitle.setText(test.getTitle());
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WritingTestActivity.class);
            intent.putExtra("topic_id", test.getId());
            intent.putExtra("title", test.getTitle());
            intent.putExtra("description", test.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return writingTests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTopicTitle);
            cardView = (CardView) itemView;
        }
    }
}
