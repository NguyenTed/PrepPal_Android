package com.group5.preppal.ui.vocabulary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
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
        holder.bind(vocabList.get(position));
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        private final View frontView, backView;
        private boolean isFlipped = false;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            frontView = itemView.findViewById(R.id.card_front);
            backView = itemView.findViewById(R.id.card_back);

            frontView.setOnClickListener(v -> flipCard());
            backView.setOnClickListener(v -> flipCard());
        }

        private void flipCard() {
            final View visible = isFlipped ? backView : frontView;
            final View hidden = isFlipped ? frontView : backView;

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

        public void bind(Vocabulary vocab) {
            TextView txtWord = itemView.findViewById(R.id.front_word);
            TextView phonetic = itemView.findViewById(R.id.front_phonetic);
            TextView wordText = itemView.findViewById(R.id.back_word);

            ImageButton btnPlayAudio = itemView.findViewById(R.id.btn_play_audio);

            txtWord.setText(vocab.getWord());
            wordText.setText(vocab.getWord());
            phonetic.setText(vocab.getPhonetic());

            btnPlayAudio.setOnClickListener(v -> {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(vocab.getAudio());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            RecyclerView backRecycler = itemView.findViewById(R.id.back_recycler);
            backRecycler.setLayoutManager(new LinearLayoutManager(context));
            backRecycler.setAdapter(new MeaningAdapter(vocab.getMeanings()));
            View backFlipOverlay = itemView.findViewById(R.id.back_flip_overlay);

            // ✅ Add flip interaction for both sides
            frontView.setOnClickListener(v -> flipCard());
            backView.setOnClickListener(v -> flipCard());
            backFlipOverlay.setOnClickListener(v -> flipCard());

            // ✅ Reset flip state
            frontView.setVisibility(View.VISIBLE);
            backView.setVisibility(View.GONE);
            frontView.setRotationY(0f);
            backView.setRotationY(0f);
            isFlipped = false;
        }
    }
}
