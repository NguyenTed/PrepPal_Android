package com.group5.preppal.ui.test_set.listening;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.listening.ListeningPart;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.viewmodel.ListeningViewModel;

import java.io.IOException;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListeningActivity extends AppCompatActivity {

    private ListeningViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private TextView partLabel;
    private RecyclerView groupRecyclerView;
    private QuestionGroupAdapter adapter;

    private ListeningSection section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        partLabel = findViewById(R.id.partLabelTextView);
        groupRecyclerView = findViewById(R.id.questionGroupRecyclerView);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionGroupAdapter();
        groupRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ListeningViewModel.class);

        section = getIntent().getParcelableExtra("listeningSection");
        if (section != null) {
            switchToPart(section.getPart1(), 1);
        }

        viewModel.getCurrentPart().observe(this, part -> {
            if (part != null) {
                partLabel.setText("Part " + getCurrentPartNumber(part));
                Log.d("LISTENING_DEBUG", "questionGroups size = " + part.getQuestionGroups().size());
                adapter.submitList(part.getQuestionGroups());
                playAudio(part.getAudioUrl());
            }
        });

        findViewById(R.id.btnPart1).setOnClickListener(v -> switchToPart(section.getPart1(), 1));
        findViewById(R.id.btnPart2).setOnClickListener(v -> switchToPart(section.getPart2(), 2));
        findViewById(R.id.btnPart3).setOnClickListener(v -> switchToPart(section.getPart3(), 3));
        findViewById(R.id.btnPart4).setOnClickListener(v -> switchToPart(section.getPart4(), 4));
    }

    private void switchToPart(ListeningPart part, int partNumber) {
        viewModel.setPart(part);
    }

    private void playAudio(String audioUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getCurrentPartNumber(ListeningPart part) {
        if (part == section.getPart1()) return 1;
        if (part == section.getPart2()) return 2;
        if (part == section.getPart3()) return 3;
        if (part == section.getPart4()) return 4;
        return -1;
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}

