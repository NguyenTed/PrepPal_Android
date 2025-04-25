package com.group5.preppal.ui.test_set;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group5.preppal.R;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.ui.course.CourseListActivity;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.ui.vocabulary.TopicActivity;
import com.group5.preppal.viewmodel.TestSetViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TestSetListActivity extends AppCompatActivity {

    private TestSetViewModel viewModel;
    private TestSetAdapter adapter;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_set_list);

        RecyclerView recyclerView = findViewById(R.id.testSetRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottomNav = findViewById(R.id.bottom_nav);

        adapter = new TestSetAdapter(set -> {
            Intent intent = new Intent(this, TestListActivity.class);
            intent.putExtra("testSetId", set.getId());
            intent.putExtra("testSetName", set.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TestSetViewModel.class);
        viewModel.getTestSets().observe(this, adapter::submitList);
        viewModel.fetchTestSets();

        bottomNav.setSelectedItemId(R.id.nav_test_set);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_test_set) {
                return true;
            } else if (itemId == R.id.nav_courses) {
                startActivity(new Intent(this, CourseListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_vocab) {
                startActivity(new Intent(this, TopicActivity.class));
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
        bottomNav.setSelectedItemId(R.id.nav_test_set);
    }
}

