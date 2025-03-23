package com.group5.preppal.ui.vocabulary;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.Vocabulary;

import java.io.IOException;
import java.util.List;
public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.CardViewHolder> {

    private List<Vocabulary> vocabList;
    private Context context;

    public FlashcardAdapter(List<Vocabulary> vocabList, Context context) {
        this.vocabList = vocabList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flashcard, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Vocabulary vocab = vocabList.get(position);
        holder.bind(vocab);
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        private View frontView, backView;
        private TextView txtWord, txtPhonetic, txtMeanings, txtExamples;
        private ImageButton btnAudio;
        private boolean isFlipped = false;
        private MediaPlayer mediaPlayer;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            frontView = itemView.findViewById(R.id.card_front);
            backView = itemView.findViewById(R.id.card_back);

            txtWord = itemView.findViewById(R.id.front_word);
            txtPhonetic = itemView.findViewById(R.id.front_phonetic);
            btnAudio = itemView.findViewById(R.id.btn_play_audio);

            txtMeanings = itemView.findViewById(R.id.back_meanings);
            txtExamples = itemView.findViewById(R.id.back_examples);

            itemView.setOnClickListener(v -> {
                isFlipped = !isFlipped;
                frontView.setVisibility(isFlipped ? View.GONE : View.VISIBLE);
                backView.setVisibility(isFlipped ? View.VISIBLE : View.GONE);
            });
        }

        public void bind(Vocabulary vocab) {
            txtWord.setText(vocab.getWord());
            txtPhonetic.setText(vocab.getPhonetic());

            txtMeanings.setText(String.join("\n", vocab.getMeanings()));
            txtExamples.setText("• " + String.join("\n• ", vocab.getExamples()));

            btnAudio.setOnClickListener(v -> {
                if (mediaPlayer != null) mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(vocab.getAudio());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show();
                }
            });

            // reset flip state
            frontView.setVisibility(View.VISIBLE);
            backView.setVisibility(View.GONE);
            isFlipped = false;
        }
    }
}
