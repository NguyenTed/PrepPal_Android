package com.group5.preppal.ui.grading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.TestItem;
import java.util.ArrayList;
import java.util.List;

public class GradingTestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private TextView emptyView;
    private ImageButton backButton;  // Nút Back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading_test);

        recyclerView = findViewById(R.id.recyclerViewTestList);
        emptyView = findViewById(R.id.emptyView);
        backButton = findViewById(R.id.backButton);  // Lấy nút Back từ layout

        // Xử lý khi nhấn nút Back
        backButton.setOnClickListener(v -> finish());

        List<TestItem> testList = getTestData();

        if (testList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TestAdapter(testList, this::onTestItemClick);
            recyclerView.setAdapter(adapter);
        }
    }

    private List<TestItem> getTestData() {
        List<TestItem> list = new ArrayList<>();
        list.add(new TestItem("Writing Test", "Topic: Environment"));
        list.add(new TestItem("Speaking Test", "Topic: Technology"));
        list.add(new TestItem("Writing Test", "Topic: Education"));
        list.add(new TestItem("Speaking Test", "Topic: Travel"));
        return list;
    }

    private void onTestItemClick(TestItem item) {
        Intent intent = new Intent(this, WritingAnswerListActivity.class);
        intent.putExtra("testType", item.getTestType());
        intent.putExtra("topic", item.getTopic());
        startActivity(intent);
    }
}
