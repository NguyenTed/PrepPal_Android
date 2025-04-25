package com.group5.preppal.ui.test_set.speaking;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
    private View createSectionView(String title, String content) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_part2_section, null);

        TextView titleView = view.findViewById(R.id.sectionTitle);
        TextView contentView = view.findViewById(R.id.sectionContent);

        titleView.setText(title);
        contentView.setText(content);

        return view;
    }
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

        // Tạo CardView
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 24); // margin bottom nếu cần
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(12f);
        cardView.setCardElevation(6f);
        cardView.setUseCompatPadding(true);

        // Container bên trong CardView
        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(24, 24, 24, 24); // padding trong card
        cardView.addView(contentLayout);

        // Thêm Task
        contentLayout.addView(createSectionView("Task", part2.getTask()));

        // Thêm các Hint
        if (part2.getHints() != null && !part2.getHints().isEmpty()) {
            // In tiêu đề Hint 1 lần
            TextView hintTitle = new TextView(this);
            hintTitle.setText("Hint");
            hintTitle.setTextSize(16f);
            hintTitle.setTypeface(null, Typeface.BOLD);
            hintTitle.setTextColor(Color.BLACK);
            hintTitle.setPadding(0, 16, 0, 8); // khoảng cách với phần trên
            contentLayout.addView(hintTitle);

            // In từng hint không có tiêu đề
            for (String hint : part2.getHints()) {
                TextView hintContent = new TextView(this);
                hintContent.setText(hint);
                hintContent.setTextSize(14f);
                hintContent.setTextColor(Color.DKGRAY);
                hintContent.setPadding(0, 0, 0, 8); // khoảng cách giữa các hint
                contentLayout.addView(hintContent);
            }
        }

        // Thêm Follow-up
        if (part2.getFollowUp() != null && !part2.getFollowUp().isEmpty()) {
            contentLayout.addView(createSectionView("Follow-up", part2.getFollowUp()));
        }

        // Thêm CardView vào container
        questionsContainer.addView(cardView);
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

