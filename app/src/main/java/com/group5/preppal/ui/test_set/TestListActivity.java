package com.group5.preppal.ui.test_set;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.Test;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.data.model.test.reading.ReadingSection;
import com.group5.preppal.data.model.test.speaking.SpeakingSection;
import com.group5.preppal.data.model.test.writing.WritingSection;
import com.group5.preppal.data.model.test.writing.WritingTask;
import com.group5.preppal.ui.test_set.listening.ListeningActivity;
import com.group5.preppal.ui.test_set.reading.ReadingActivity;
import com.group5.preppal.ui.test_set.speaking.SpeakingActivity;
import com.group5.preppal.ui.test_set.writing.WritingActivity;
import com.group5.preppal.viewmodel.TestListViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TestListActivity extends AppCompatActivity {

    private TestListViewModel viewModel;
    private TestListAdapter adapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.testRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Adapter setup: each item opens TestDetailActivity
        adapter = new TestListAdapter(test -> {
            Intent intent = new Intent(TestListActivity.this, TestDetailActivity.class);
            intent.putExtra("testId", test.getId());
            intent.putExtra("testSetId", test.getTestSetId());
            intent.putExtra("testName", test.getName());
            intent.putExtra("listeningSection", test.getListeningSection());
            intent.putExtra("readingSection", test.getReadingSection());
            intent.putExtra("writingSection", test.getWritingSection());
            intent.putExtra("speakingSection", test.getSpeakingSection());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(TestListViewModel.class);

        // Observe tests and attempts
        viewModel.getTests().observe(this, adapter::submitList);

        viewModel.getTestAttempts().observe(this, map -> {
            adapter.setTestAttemptsMap(map); // make sure adapter has this method
            adapter.notifyDataSetChanged();
        });

        // Load tests
        String testSetId = getIntent().getStringExtra("testSetId");
        viewModel.fetchTests(testSetId);
        viewModel.fetchTestAttempts();
    }
}

