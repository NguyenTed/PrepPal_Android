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

        RecyclerView recyclerView = findViewById(R.id.testRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        adapter = new TestListAdapter(new TestListAdapter.OnTestClickListener() {
            @Override
            public void onListeningClick(Test test) {
                ListeningSection listening = test.getListeningSection();
                if (listening == null) {
                    Log.e("TEST_DEBUG", "Listening section is NULL!");
                    return;
                }

                Intent intent = new Intent(TestListActivity.this, ListeningActivity.class);
                intent.putExtra("testId", test.getId());
                intent.putExtra("testSetId", test.getTestSetId());
                intent.putExtra("testName", test.getName());
                intent.putExtra("listeningSection", listening);
                startActivity(intent);
            }

            @Override
            public void onReadingClick(Test test) {
                ReadingSection reading = test.getReadingSection();
                if (reading == null) {
                    Log.e("TEST_DEBUG", "Reading section is NULL!");
                    return;
                }

                Intent intent = new Intent(TestListActivity.this, ReadingActivity.class);
                intent.putExtra("testId", test.getId());
                intent.putExtra("testSetId", test.getTestSetId());
                intent.putExtra("testName", test.getName());
                intent.putExtra("readingSection", reading);
                startActivity(intent);
            }

            @Override
            public void onWritingClick(Test test) {
                Log.d("TEST_DEBUG", "Test: " + test.getName());
                WritingSection writing = test.getWritingSection();
                if (writing == null) {
                    Log.e("TEST_DEBUG", "Writing section is NULL!");
                    return;
                }

                WritingTask task1 = writing.getTask1();
                WritingTask task2 = writing.getTask2();
                Log.d("TEST_DEBUG", "Task1 = " + (task1 != null ? task1.getTask() : "null"));

                Intent intent = new Intent(TestListActivity.this, WritingActivity.class);
                intent.putExtra("testName", test.getName());
                intent.putExtra("task1", task1);
                intent.putExtra("task2", task2);
                startActivity(intent);
            }


            @Override
            public void onSpeakingClick(Test test) {
                SpeakingSection speaking = test.getSpeakingSection();
                Intent intent = new Intent(TestListActivity.this, SpeakingActivity.class);
                intent.putExtra("testName", test.getName());
                intent.putStringArrayListExtra("part1", new ArrayList<>(speaking.getPart1()));
                intent.putStringArrayListExtra("part3", new ArrayList<>(speaking.getPart3()));
                intent.putExtra("part2", speaking.getPart2()); // must be Parcelable
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TestListViewModel.class);
        viewModel.getTests().observe(this, adapter::submitList);

        String testSetId = getIntent().getStringExtra("testSetId");
        viewModel.fetchTests(testSetId);
    }
}
