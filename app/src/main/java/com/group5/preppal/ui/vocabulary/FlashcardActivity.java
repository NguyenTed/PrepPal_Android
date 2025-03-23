package com.group5.preppal.ui.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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

    private String topicId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        topicId = getIntent().getStringExtra("topicId");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (topicId == null) {
            Toast.makeText(this, "Missing topic ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);

        viewPager = findViewById(R.id.viewPager);
        adapter = new FlashcardAdapter(vocabList, this, word -> {
            viewModel.markWordAsLearned(userId, topicId, word,
                    () -> runOnUiThread(() ->
                            Toast.makeText(this, "Marked as learned!", Toast.LENGTH_SHORT).show()),
                    error -> runOnUiThread(() ->
                            Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show())
            );
        });
        viewPager.setAdapter(adapter);

        // Load only unlearned vocab
        viewModel.loadUnlearnedVocabularies(topicId, userId);

        viewModel.getVocabularies().observe(this, vocabularies -> {
            vocabList.clear();
            if (vocabularies != null) {
                vocabList.addAll(vocabularies);
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}

