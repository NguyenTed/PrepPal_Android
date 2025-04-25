package com.group5.preppal.ui.test_set;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group5.preppal.R;
import com.group5.preppal.data.model.test.Test;
import com.group5.preppal.data.model.test.TestSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestSetAdapter extends RecyclerView.Adapter<TestSetAdapter.TestSetViewHolder> {

    public interface OnTestSetClickListener {
        void onTestSetClick(TestSet set);
    }

    private final List<TestSet> testSets = new ArrayList<>();
    private final OnTestSetClickListener listener;

    public TestSetAdapter(OnTestSetClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<TestSet> sets) {
        testSets.clear();
        testSets.addAll(sets);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TestSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_set, parent, false);
        return new TestSetViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull TestSetViewHolder holder, int position) {
        TestSet set = testSets.get(position);
        holder.title.setText(set.getName());
        Glide.with(holder.itemView.getContext())
                .load(set.getImageUrl())
                .placeholder(R.drawable.ielts) // Ảnh mặc định khi chưa load xong
                .error(R.drawable.ielts)
                .into(holder.bookIcon);// Ảnh fallback khi load lỗi
        holder.itemView.setOnClickListener(v -> listener.onTestSetClick(set));
    }

    @Override
    public int getItemCount() {
        return testSets.size();
    }

    static class TestSetViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView bookIcon;
        public TestSetViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.testSetTitle);
            bookIcon = itemView.findViewById(R.id.bookIcon);
        }
    }
}
