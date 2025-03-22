package com.group5.preppal.ui.grading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.WritingAnswerItem;
import java.util.ArrayList;
import java.util.List;

public class WritingAnswerListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptyView;
    private WritingAnswerAdapter adapter;
    private ImageButton backButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_answer_list);

        recyclerView = findViewById(R.id.recyclerViewTestList);
        emptyView = findViewById(R.id.emptyView);
        backButton = findViewById(R.id.backButton);  // Lấy nút Back từ layout

        // Xử lý khi nhấn nút Back
        backButton.setOnClickListener(v -> finish());
        List<WritingAnswerItem> answerList = getWritingAnswers();

        if (answerList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // ✅ Khởi tạo adapter với sự kiện click
            adapter = new WritingAnswerAdapter(answerList, new WritingAnswerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(WritingAnswerItem item) {
                    Intent intent = new Intent(WritingAnswerListActivity.this, GradingWritingTestActivity.class);
                    intent.putExtra("TOPIC_TITLE", item.getTopicTitle());
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private List<WritingAnswerItem> getWritingAnswers() {
        List<WritingAnswerItem> list = new ArrayList<>();
        list.add(new WritingAnswerItem("Student 1 - Environment"));
        list.add(new WritingAnswerItem("Student 2 - Technology"));
        list.add(new WritingAnswerItem("Student 3 - Education"));
        return list;
    }
}
