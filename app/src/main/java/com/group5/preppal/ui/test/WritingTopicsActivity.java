package com.group5.preppal.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.data.model.Task;
import com.group5.preppal.data.model.WritingTest;
import com.group5.preppal.viewmodel.WritingTestViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WritingTopicsActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private HashMap<String, List<Map<String, String>>> topicMap;
    private List<String> topicList;
    private WritingTopicsAdapter adapter;


    private WritingTestViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_topics);

        expandableListView = findViewById(R.id.expandableListView);
        topicMap = new HashMap<>();
        topicList = new ArrayList<>();

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(WritingTestViewModel.class);

        // Quan sát dữ liệu từ ViewModel
        viewModel.getWritingTests().observe(this, tests -> {
            if (tests != null) {
                topicList.clear();
                topicMap.clear();

                for (WritingTest writingTest : tests) {
                    List<Map<String, String>> tasksList = new ArrayList<>();

                    for (Task task : writingTest.getTasks()) {
                        Map<String, String> taskData = new HashMap<>();
                        taskData.put("id", task.getId());
                        tasksList.add(taskData);
                    }

                    topicList.add(writingTest.getName());
                    topicMap.put(writingTest.getName(), tasksList);
                }

                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Log.e("WritingTopicsActivity", "Lỗi khi tải dữ liệu: " + error);
            }
        });

        // Gọi hàm fetch dữ liệu từ Firestore
        viewModel.fetchWritingTests();

        // Khởi tạo Adapter
        adapter = new WritingTopicsAdapter(this, topicList, topicMap);
        expandableListView.setAdapter(adapter);


        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (expandableListView.isGroupExpanded(groupPosition)) {
                expandableListView.collapseGroup(groupPosition);
            } else {
                expandableListView.expandGroup(groupPosition, true);
            }
            return true;
        });


        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String topic = topicList.get(groupPosition);
            List<Map<String, String>> tasks = topicMap.get(topic);

            if (tasks == null || tasks.isEmpty()) {
                Log.e("WritingTopicsActivity", "Không có task nào cho topic: " + topic);
                return true;
            }

            if (childPosition < 0 || childPosition >= tasks.size()) {
                Log.e("WritingTopicsActivity", "childPosition không hợp lệ!");
                return true;
            }

            Map<String, String> task = tasks.get(childPosition);

            Log.d("WritingTopicsActivity", "Title: " + task.get("title"));
            Log.d("WritingTopicsActivity", "Description: " + task.get("description"));
            Log.d("WritingTopicsActivity", "ImgUrl: " + task.get("imgUrl"));

            Intent intent = new Intent(WritingTopicsActivity.this, WritingTestActivity.class);
            intent.putExtra("id", task.get("id"));
            startActivity(intent);

            return true;
        });
    }
}
