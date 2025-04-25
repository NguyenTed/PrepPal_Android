package com.group5.preppal.ui.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Vocabulary;
import com.group5.preppal.viewmodel.FlashcardViewModel;

import java.util.ArrayList;
import java.util.List;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FlashcardActivity extends AppCompatActivity {
    private FlashcardViewModel viewModel;
    private FlashcardAdapter adapter;
    private List<Vocabulary> vocabList = new ArrayList<>();
    private ViewPager2 viewPager;
    private ProgressBar progressBar;
    private TextView topicNameText, progressText;
    private Button markLearnedButton;
    private ImageView btnBack;
    private int currentIndex = 0;

    private int learnedCount = 0;
    private int totalCount = 0;

    private String topicId;
    private String topicName;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // Views
        progressBar = findViewById(R.id.flashcard_progress_bar);
        progressText = findViewById(R.id.flashcard_progress_text);
        topicNameText = findViewById(R.id.topicNameTextView);
        markLearnedButton = findViewById(R.id.btn_mark_learned);
        viewPager = findViewById(R.id.viewPager);
        btnBack = findViewById(R.id.btnBack);

        // Intent data
        topicId = getIntent().getStringExtra("topicId");
        topicName = getIntent().getStringExtra("topicName");
        learnedCount = getIntent().getIntExtra("learnedCount", 0);
        totalCount = getIntent().getIntExtra("totalCount", 0);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        topicNameText.setText(topicName);

        if (topicId == null) {
            Toast.makeText(this, "Missing topic ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);
        adapter = new FlashcardAdapter(vocabList, this);
        viewPager.setAdapter(adapter);

        // Swipe to update current index
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                updateProgressUI();
            }
        });

        // Mark as learned button
        markLearnedButton.setOnClickListener(v -> {
            if (currentIndex < vocabList.size()) {
                Vocabulary vocab = vocabList.get(currentIndex);

                viewModel.markWordAsLearned(userId, topicId, vocab.getWord(),
                        () -> runOnUiThread(() -> {
                            Toast.makeText(this, "Marked as learned!", Toast.LENGTH_SHORT).show();

                            vocabList.remove(currentIndex);
                            adapter.notifyItemRemoved(currentIndex);
                            learnedCount++; // ⬅️ Update learned count!

                            if (vocabList.isEmpty()) {
                                markLearnedButton.setEnabled(false);
                                markLearnedButton.setText("All learned!");
                                updateProgressUI();
                            } else {
                                if (currentIndex >= vocabList.size()) {
                                    currentIndex = vocabList.size() - 1;
                                    viewPager.setCurrentItem(currentIndex, true);
                                }
                                updateProgressUI();
                            }
                        }),
                        error -> runOnUiThread(() ->
                                Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show())
                );
            }
        });

        btnBack.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        // Load unlearned vocab
        viewModel.loadUnlearnedVocabularies(topicId, userId);
        viewModel.getVocabularies().observe(this, vocabularies -> {
            vocabList.clear();
            if (vocabularies != null) {
                vocabList.addAll(vocabularies);
            }
            adapter.notifyDataSetChanged();
            updateProgressUI();
        });
    }

    private void updateProgressUI() {
        progressText.setText(learnedCount + "/" + totalCount);
        int percent = (totalCount == 0) ? 0 : (int) (((float) learnedCount / totalCount) * 100);
        progressBar.setProgress(percent);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}

