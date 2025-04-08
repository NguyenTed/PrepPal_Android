package com.group5.preppal.ui.test_set.speaking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.speaking.SpeakingPartTwo;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SpeakingActivity extends AppCompatActivity {

    private TextView tvTestTitle;
    private LinearLayout questionsContainer;
    private Button btnPart1, btnPart2, btnPart3;

    private List<String> part1, part3;
    private SpeakingPartTwo part2;
    private String testName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking);

        tvTestTitle = findViewById(R.id.tvTestTitle);
        questionsContainer = findViewById(R.id.questionsContainer);
        btnPart1 = findViewById(R.id.btnPart1);
        btnPart2 = findViewById(R.id.btnPart2);
        btnPart3 = findViewById(R.id.btnPart3);

        testName = getIntent().getStringExtra("testName");
        part1 = getIntent().getStringArrayListExtra("part1");
        part3 = getIntent().getStringArrayListExtra("part3");
        part2 = getIntent().getParcelableExtra("part2");

        tvTestTitle.setText(testName);

        btnPart1.setOnClickListener(v -> showPart1());
        btnPart2.setOnClickListener(v -> showPart2());
        btnPart3.setOnClickListener(v -> showPart3());

        showPart1(); // default
    }

    private void showPart1() {
        questionsContainer.removeAllViews();
        for (int i = 0; i < part1.size(); i++) {
            addQuestionCard("Question " + (i + 1), part1.get(i));
        }
    }

    private void showPart2() {
        questionsContainer.removeAllViews();
        addQuestionCard("Task", part2.getTask());

        if (part2.getHints() != null && !part2.getHints().isEmpty()) {
            for (String hint : part2.getHints()) {
                addQuestionCard("Hint", hint);
            }
        }

        if (part2.getFollowUp() != null && !part2.getFollowUp().isEmpty()) {
            addQuestionCard("Follow-up", part2.getFollowUp());
        }
    }

    private void showPart3() {
        questionsContainer.removeAllViews();
        for (int i = 0; i < part3.size(); i++) {
            addQuestionCard("Question " + (i + 1), part3.get(i));
        }
    }

    private void addQuestionCard(String title, String content) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_speaking_question, questionsContainer, false);

        TextView tvTitle = view.findViewById(R.id.tvQuestionTitle);
        TextView tvContent = view.findViewById(R.id.tvQuestionContent);

        tvTitle.setText(title);
        tvContent.setText(content);

        questionsContainer.addView(view);
    }

}

