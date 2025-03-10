package com.group5.preppal.ui.test;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.group5.preppal.R;
import com.group5.preppal.data.model.WritingTest;
import java.util.ArrayList;
import java.util.List;

public class WritingTopicsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WritingTestAdapter adapter;
    private List<WritingTest> topicList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_topics);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        fetchTopics();

        // Xử lý nút back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void fetchTopics() {
        db.collection("writing_tests").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                topicList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    WritingTest topic = document.toObject(WritingTest.class);
                    topicList.add(topic);
                }
                adapter = new WritingTestAdapter(this, topicList);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
