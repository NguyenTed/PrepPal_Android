package com.group5.preppal.ui.test_set.listening;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.listening.ListeningAttempt;
import com.group5.preppal.data.model.test.listening.ListeningQuestionGroup;
import com.group5.preppal.data.model.test.listening.ListeningSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListeningReviewActivity extends AppCompatActivity {

    private TextView tvPartLabel;
    private RecyclerView groupRecyclerView;
    private ImageView btnBack;
    private ListeningQuestionGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_review);

        btnBack = findViewById(R.id.btnBack);
        tvPartLabel = findViewById(R.id.partLabelTextView);
        groupRecyclerView = findViewById(R.id.questionGroupRecyclerView);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListeningQuestionGroupAdapter();
        adapter.setReviewMode(true);
        groupRecyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        ListeningSection section = getIntent().getParcelableExtra("listeningSection");
        ListeningAttempt attempt = getIntent().getParcelableExtra("attempt");

        if (section == null || attempt == null) {
            Toast.makeText(this, "Missing review data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Map<String, String> userAnswers = attempt.getAnswers();
        Map<Integer, String> parsedAnswers = new HashMap<>();
        for (Map.Entry<String, String> entry : userAnswers.entrySet()) {
            try {
                parsedAnswers.put(Integer.parseInt(entry.getKey()), entry.getValue());
            } catch (NumberFormatException e) {
                Log.e("ListeningReview", "Invalid key: " + entry.getKey());
            }
        }

        adapter.setUserAnswers(parsedAnswers);

        List<ListeningQuestionGroup> allGroups = new ArrayList<>();
        allGroups.addAll(section.getPart1().getListeningQuestionGroups());
        allGroups.addAll(section.getPart2().getListeningQuestionGroups());
        allGroups.addAll(section.getPart3().getListeningQuestionGroups());
        allGroups.addAll(section.getPart4().getListeningQuestionGroups());

        adapter.submitList(allGroups);
        tvPartLabel.setText("Listening Review");
    }
}

