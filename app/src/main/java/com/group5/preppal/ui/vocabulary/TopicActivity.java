package com.group5.preppal.ui.vocabulary;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.preppal.R;
import com.group5.preppal.viewmodel.TopicViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TopicActivity extends AppCompatActivity {

    private TopicViewModel viewModel;
    private TopicAdapter adapter;
    private String userId;

    private ActivityResultLauncher<Intent> flashcardLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_topics);
        adapter = new TopicAdapter(topic -> {
            // Launch FlashcardActivity and wait for result
            Intent intent = new Intent(this, FlashcardActivity.class);
            intent.putExtra("topicId", topic.getTopicId());
            intent.putExtra("learnedCount", topic.getLearnedCount());
            intent.putExtra("totalCount", topic.getTotalCount());
            flashcardLauncher.launch(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TopicViewModel.class);

        // Initial load
        viewModel.getTopics(userId).observe(this, adapter::setTopics);

        // Register launcher
        flashcardLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Reload progress when returning from flashcards
                        viewModel.getTopics(userId).observe(this, adapter::setTopics);
                    }
                });
    }
}

