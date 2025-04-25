package com.group5.preppal.ui.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.group5.preppal.R;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.ui.course.CourseListActivity;
import com.group5.preppal.ui.dictionary.DictionaryActivity;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.ui.test_set.TestSetListActivity;
import com.group5.preppal.viewmodel.TopicViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TopicActivity extends AppCompatActivity {

    private TopicViewModel viewModel;
    private TopicAdapter adapter;
    private String userId;
    private ImageView btnDictionary;
    private BottomNavigationView bottomNav;

    private ActivityResultLauncher<Intent> flashcardLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        bottomNav = findViewById(R.id.bottom_nav);
        btnDictionary = findViewById(R.id.btnDictionary);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_topics);
        adapter = new TopicAdapter(topic -> {
            // Launch FlashcardActivity and wait for result
            Intent intent = new Intent(this, FlashcardActivity.class);
            intent.putExtra("topicId", topic.getTopicId());
            intent.putExtra("topicName", topic.getName());
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

        btnDictionary.setOnClickListener(v -> {
            Intent intent = new Intent(this, DictionaryActivity.class);
            startActivity(intent);
        });

        bottomNav.setSelectedItemId(R.id.nav_vocab);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_vocab) {
                return true;
            } else if (itemId == R.id.nav_courses) {
                startActivity(new Intent(this, CourseListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_test_set) {
                startActivity(new Intent(this, TestSetListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_vocab);
    }
}

