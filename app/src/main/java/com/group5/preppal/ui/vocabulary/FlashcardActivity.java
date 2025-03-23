package com.group5.preppal.ui.vocabulary;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Vocabulary;
import com.group5.preppal.viewmodel.FlashcardViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class FlashcardActivity extends AppCompatActivity {

    private FlashcardViewModel viewModel;
    private FlashcardAdapter adapter;
    private List<Vocabulary> vocabList = new ArrayList<>();
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        viewPager = findViewById(R.id.viewPager);
        adapter = new FlashcardAdapter(vocabList, this);
        viewPager.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);

        String topicId = getIntent().getStringExtra("topicId");
        if (topicId != null) {
            viewModel.loadVocabularies(topicId);
        }

        viewModel.getVocabularies().observe(this, vocabularies -> {
            if (vocabularies != null) {
                vocabList.clear();
                vocabList.addAll(vocabularies);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
