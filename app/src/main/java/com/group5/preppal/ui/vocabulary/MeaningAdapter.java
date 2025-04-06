package com.group5.preppal.ui.vocabulary;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.Vocabulary;

import java.util.List;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.ViewHolder> {

    private final List<Vocabulary.Meaning> meanings;

    public MeaningAdapter(List<Vocabulary.Meaning> meanings) {
        this.meanings = meanings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meaning, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vocabulary.Meaning meaning = meanings.get(position);
        holder.definition.setText(meaning.getDefinition());

        holder.examples.removeAllViews();
        for (String ex : meaning.getExamples()) {
            TextView exView = new TextView(holder.itemView.getContext());
            exView.setText("• " + ex);
            exView.setTypeface(exView.getTypeface(), Typeface.ITALIC);
            exView.setTextSize(16f);
            exView.setTextColor(Color.DKGRAY);
            exView.setPadding(16, 4, 0, 8);
            holder.examples.addView(exView);
        }
    }

    @Override
    public int getItemCount() {
        return meanings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView definition;
        LinearLayout examples;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            definition = itemView.findViewById(R.id.item_definition);
            examples = itemView.findViewById(R.id.item_examples_container);
        }
    }
}
