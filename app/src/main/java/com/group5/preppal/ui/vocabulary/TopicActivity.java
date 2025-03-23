package com.group5.preppal.ui.vocabulary;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.viewmodel.TopicViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TopicActivity extends AppCompatActivity {

    private TopicViewModel viewModel;
    private TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_topics);
        adapter = new TopicAdapter(topic -> {
            // When user taps a topic, you can open FlashcardActivity here
            Intent intent = new Intent(this, FlashcardActivity.class);
            intent.putExtra("topicId", topic.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TopicViewModel.class);
        viewModel.getTopics().observe(this, topics -> {
            if (topics != null) adapter.setTopics(topics);
        });
    }
}

