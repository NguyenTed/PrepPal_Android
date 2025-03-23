package com.group5.preppal.ui.grading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.WritingAnswerItem;
import com.group5.preppal.data.model.WritingQuizSubmission;
import com.group5.preppal.data.repository.WritingQuizSubmissionRepository;
import com.group5.preppal.viewmodel.WritingTestViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WritingAnswerListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptyView;
    private WritingAnswerAdapter adapter;
    private ImageButton backButton;
    private WritingTestViewModel writingTestViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_answer_list);

        recyclerView = findViewById(R.id.recyclerViewTestList);
        emptyView = findViewById(R.id.emptyView);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
        String taskId = getIntent().getStringExtra("taskId");
        writingTestViewModel = new ViewModelProvider(this).get(WritingTestViewModel.class);
        writingTestViewModel.getAllPendingWritingQuizSubmissionWithTaskId(taskId).observe(this, submissions -> {
            if (submissions.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                adapter = new WritingAnswerAdapter(submissions, new WritingAnswerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(WritingQuizSubmission item) {
                        Intent intent = new Intent(WritingAnswerListActivity.this, GradingWritingTestActivity.class);
                        intent.putExtra("submissionId", item.getId());
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

//    private List<WritingAnswerItem> getWritingAnswers() {
//        List<WritingAnswerItem> list = new ArrayList<>();
//        list.add(new WritingAnswerItem("Student 1 - Environment"));
//        list.add(new WritingAnswerItem("Student 2 - Technology"));
//        list.add(new WritingAnswerItem("Student 3 - Education"));
//        return list;
//    }
}
