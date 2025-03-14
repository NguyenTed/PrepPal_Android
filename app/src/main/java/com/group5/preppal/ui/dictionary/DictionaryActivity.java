package com.group5.preppal.ui.dictionary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.DictionaryResponse;
import com.group5.preppal.viewmodel.DictionaryViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DictionaryActivity extends AppCompatActivity {
    private DictionaryViewModel dictionaryViewModel;
    private MeaningAdapter meaningAdapter;
    private PhoneticAdapter phoneticAdapter;
    private EditText etWord;
    private Button btnSearch;
    private TextView tvWord;
    private RecyclerView rvMeanings, rvPhonetics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        // Initialize Views
        etWord = findViewById(R.id.etWord);
        btnSearch = findViewById(R.id.btnSearch);
        tvWord = findViewById(R.id.tvWord);
        rvMeanings = findViewById(R.id.rvMeanings);
        rvPhonetics = findViewById(R.id.rvPhonetics);

        // Setup RecyclerView
        rvMeanings.setLayoutManager(new LinearLayoutManager(this));
        meaningAdapter = new MeaningAdapter(null); // Initially empty
        rvMeanings.setAdapter(meaningAdapter);

        rvPhonetics.setLayoutManager(new LinearLayoutManager(this));
        phoneticAdapter = new PhoneticAdapter(this, null);
        rvPhonetics.setAdapter(phoneticAdapter);

        // Initialize ViewModel
        dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);

        // Observe LiveData
        dictionaryViewModel.getWordDefinition().observe(this, response -> {
            DictionaryResponse wordData = response.get(0);
            tvWord.setText(wordData.getWord());
            tvWord.setVisibility(View.VISIBLE);
//            rvMeanings.setVisibility(View.VISIBLE);
            rvPhonetics.setVisibility(View.VISIBLE);
            phoneticAdapter.updateData(wordData.getPhonetics());
            meaningAdapter.updateData(wordData.getMeanings());
        });

        dictionaryViewModel.getErrorMessage().observe(this, error ->
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show()
        );

        // Search Button Click
        btnSearch.setOnClickListener(view -> {
            String word = etWord.getText().toString().trim();
            if (!word.isEmpty()) {
                dictionaryViewModel.searchWord(word);
            } else {
                Toast.makeText(this, "Please enter a word!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
