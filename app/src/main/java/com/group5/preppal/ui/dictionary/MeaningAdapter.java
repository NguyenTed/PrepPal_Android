package com.group5.preppal.ui.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView; // Change to your actual package

import com.group5.preppal.R;
import com.group5.preppal.data.model.DictionaryResponse;

import java.util.List;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.MeaningViewHolder> {
    private List<DictionaryResponse.Meaning> meaningList;

    public MeaningAdapter(List<DictionaryResponse.Meaning> meaningList) {
        this.meaningList = meaningList;
    }

    @NonNull
    @Override
    public MeaningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meaning, parent, false);
        return new MeaningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeaningViewHolder holder, int position) {
        DictionaryResponse.Meaning meaning = meaningList.get(position);
        holder.tvPartOfSpeech.setText(meaning.getPartOfSpeech());

        // Combine all definitions into one string
        StringBuilder definitionsText = new StringBuilder();
        for (DictionaryResponse.Meaning.Definition def : meaning.getDefinitions()) {
            definitionsText.append("• ").append(def.getDefinition()).append("\n");
            if (def.getExample() != null && !def.getExample().isEmpty()) {
                definitionsText.append("  Example: ").append(def.getExample()).append("\n");
            }
        }
        holder.tvDefinitions.setText(definitionsText.toString());
    }

    @Override
    public int getItemCount() {
        return meaningList != null ? meaningList.size() : 0;
    }

    public void updateData(List<DictionaryResponse.Meaning> newMeanings) {
        this.meaningList = newMeanings;
        notifyDataSetChanged();
    }

    static class MeaningViewHolder extends RecyclerView.ViewHolder {
        TextView tvPartOfSpeech, tvDefinitions;

        public MeaningViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPartOfSpeech = itemView.findViewById(R.id.tvPartOfSpeech);
            tvDefinitions = itemView.findViewById(R.id.tvDefinitions);
        }
    }
}

