package com.group5.preppal.ui.dictionary;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.DictionaryResponse;

import java.io.IOException;
import java.util.List;

public class PhoneticAdapter extends RecyclerView.Adapter<PhoneticAdapter.PhoneticViewHolder> {
    private List<DictionaryResponse.Phonetic> phoneticList;
    private Context context;

    public PhoneticAdapter(Context context, List<DictionaryResponse.Phonetic> phoneticList) {
        this.context = context;
        this.phoneticList = phoneticList;
    }

    @NonNull
    @Override
    public PhoneticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phonetic, parent, false);
        return new PhoneticViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneticViewHolder holder, int position) {
        DictionaryResponse.Phonetic phonetic = phoneticList.get(position);
        String phoneticText = phonetic.getText();
        if (!phoneticText.contains("[")) {
            holder.tvPhonetic.setText(phoneticText);
        } else {
            holder.tvPhonetic.setText(phoneticText);
        }
        holder.tvPhonetic.setTextColor(Color.parseColor("#0E6CF0"));

        if (phonetic.getAudio() != null && !phonetic.getAudio().isEmpty()) {
            holder.btnPlayAudio.setVisibility(View.VISIBLE);
            holder.btnPlayAudio.setColorFilter(Color.parseColor("#0E6CF0"));
            holder.btnPlayAudio.setOnClickListener(v -> playAudio(phonetic.getAudio()));
        } else {
            holder.btnPlayAudio.setVisibility(View.GONE);
        }
    }

    public void updateData(List<DictionaryResponse.Phonetic> newPhonetics) {
        this.phoneticList = newPhonetics;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return phoneticList != null ? phoneticList.size() : 0;
    }

    private void playAudio(String audioUrl) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class PhoneticViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhonetic;
        ImageButton btnPlayAudio;

        public PhoneticViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhonetic = itemView.findViewById(R.id.tvPhonetic);
            btnPlayAudio = itemView.findViewById(R.id.btnPlayAudio);
        }
    }
}

