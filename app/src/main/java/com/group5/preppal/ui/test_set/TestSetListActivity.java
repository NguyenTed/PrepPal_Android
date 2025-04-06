package com.group5.preppal.ui.test_set;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.Test;
import com.group5.preppal.data.model.test.TestSet;
import com.group5.preppal.viewmodel.TestSetViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TestSetListActivity extends AppCompatActivity {

    private TestSetViewModel viewModel;
    private TestSetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_set_list);

        RecyclerView recyclerView = findViewById(R.id.testSetRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TestSetAdapter(set -> {
            Log.d("CLICK", "Opening test set with ID: " + set.getId());

            Intent intent = new Intent(this, TestListActivity.class);
            intent.putExtra("testSetId", set.getId());
            intent.putExtra("testSetName", set.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TestSetViewModel.class);
        viewModel.getTestSets().observe(this, adapter::submitList);
        viewModel.fetchTestSets();
    }
}

