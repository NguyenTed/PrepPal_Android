package com.group5.preppal.ui.vocabulary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Vocabulary;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.CardViewHolder> {

    private final List<Vocabulary> vocabList;
    private final Context context;
    private final OnLearnedClickListener learnedClickListener;

    public interface OnLearnedClickListener {
        void onClick(String word);
    }

    public FlashcardAdapter(List<Vocabulary> vocabList, Context context, OnLearnedClickListener learnedClickListener) {
        this.vocabList = vocabList;
        this.context = context;
        this.learnedClickListener = learnedClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flashcard, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(vocabList.get(position));
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        private final View frontView, backView;
        private final TextView txtWord, txtPhonetic, txtMeanings, txtExamples;
        private final ImageButton btnAudio;
        private final Button btnLearned;

        private MediaPlayer mediaPlayer;
        private boolean isFlipped = false;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            frontView = itemView.findViewById(R.id.card_front);
            backView = itemView.findViewById(R.id.card_back);

            txtWord = itemView.findViewById(R.id.front_word);
            txtPhonetic = itemView.findViewById(R.id.front_phonetic);
            btnAudio = itemView.findViewById(R.id.btn_play_audio);

            txtMeanings = itemView.findViewById(R.id.back_meanings);
            txtExamples = itemView.findViewById(R.id.back_examples);
            btnLearned = itemView.findViewById(R.id.btn_learned);

            frontView.setOnClickListener(v -> flip());
            backView.setOnClickListener(v -> flip());
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

            btnLearned.setEnabled(true);
            btnLearned.setText("I have learned this word!");

            btnLearned.setOnClickListener(v -> {
                btnLearned.setEnabled(false);
                btnLearned.setText("✓ Learned!");
                learnedClickListener.onClick(vocab.getWord());
            });

            // Reset state
            frontView.setVisibility(View.VISIBLE);
            backView.setVisibility(View.GONE);
            frontView.setRotationY(0f);
            backView.setRotationY(0f);
            isFlipped = false;
        }

        private void flip() {
            View visible = isFlipped ? backView : frontView;
            View hidden = isFlipped ? frontView : backView;

            visible.animate()
                    .rotationY(90f)
                    .setDuration(200)
                    .withEndAction(() -> {
                        visible.setVisibility(View.GONE);
                        visible.setRotationY(0f);
                        hidden.setVisibility(View.VISIBLE);
                        hidden.setRotationY(-90f);
                        hidden.animate()
                                .rotationY(0f)
                                .setDuration(200)
                                .start();
                    }).start();

            isFlipped = !isFlipped;
        }
    }
}
