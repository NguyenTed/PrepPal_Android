package com.group5.preppal.ui.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.DictionaryResponse;

import java.util.List;

public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder> {
    private List<DictionaryResponse.Meaning.Definition> definitions;

    public DefinitionAdapter(List<DictionaryResponse.Meaning.Definition> definitions) {
        this.definitions = definitions;
    }

    @NonNull
    @Override
    public DefinitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_definition, parent, false);
        return new DefinitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DefinitionViewHolder holder, int position) {
        DictionaryResponse.Meaning.Definition definition = definitions.get(position);

        holder.tvIndex.setText((position + 1) + ". ");
        holder.tvDefinition.setText(definition.getDefinition());

        // Show example if available
        if (definition.getExample() != null && !definition.getExample().isEmpty()) {
            holder.tvExample.setVisibility(View.VISIBLE);
            holder.tvExample.setText(definition.getExample());
        } else {
            holder.tvExample.setVisibility(View.GONE);
        }

        // Show synonyms if available
        if (definition.getSynonyms() != null && !definition.getSynonyms().isEmpty()) {
            holder.tvSynonyms.setVisibility(View.VISIBLE);
            holder.tvSynonyms.setText("Synonyms: " + String.join(", ", definition.getSynonyms()));
        } else {
            holder.tvSynonyms.setVisibility(View.GONE);
        }

        // Show antonyms if available
        if (definition.getAntonyms() != null && !definition.getAntonyms().isEmpty()) {
            holder.tvAntonyms.setVisibility(View.VISIBLE);
            holder.tvAntonyms.setText("Antonyms: " + String.join(", ", definition.getAntonyms()));
        } else {
            holder.tvAntonyms.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return definitions.size();
    }

    static class DefinitionViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex, tvDefinition, tvExample, tvSynonyms, tvAntonyms;

        public DefinitionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tvIndex);
            tvDefinition = itemView.findViewById(R.id.tvDefinition);
            tvExample = itemView.findViewById(R.id.tvExample);
            tvSynonyms = itemView.findViewById(R.id.tvSynonyms);
            tvAntonyms = itemView.findViewById(R.id.tvAntonyms);
        }
    }
}

