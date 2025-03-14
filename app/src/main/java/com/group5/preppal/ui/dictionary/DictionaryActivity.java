package com.group5.preppal.ui.dictionary;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.group5.preppal.R;
import com.group5.preppal.data.model.DictionaryResponse;
import com.group5.preppal.viewmodel.DictionaryViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DictionaryActivity extends AppCompatActivity {
    private DictionaryViewModel dictionaryViewModel;
    private PhoneticAdapter phoneticAdapter;
    private EditText etWord;
    private Button btnSearch;
    private TextView tvWord;
    private RecyclerView rvPhonetics;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        // Initialize Views
        etWord = findViewById(R.id.etWord);
        btnSearch = findViewById(R.id.btnSearch);
        tvWord = findViewById(R.id.tvWord);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        rvPhonetics = findViewById(R.id.rvPhonetics);

        // Setup RecyclerVie
        rvPhonetics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        phoneticAdapter = new PhoneticAdapter(this, null);
        rvPhonetics.setAdapter(phoneticAdapter);

        // Initialize ViewModel
        dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);

        // Observe LiveData
        dictionaryViewModel.getWordDefinition().observe(this, response -> {
            DictionaryResponse wordData = response.get(0);
            tvWord.setText(wordData.getWord());
            tvWord.setVisibility(View.VISIBLE);
            rvPhonetics.setVisibility(View.VISIBLE);
            List<DictionaryResponse.Meaning> meanings = wordData.getMeanings();
            if (!meanings.isEmpty()) {
                MeaningPagerAdapter adapter = new MeaningPagerAdapter(this, meanings);
                viewPager.setAdapter(adapter);

                new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                    tab.setText(meanings.get(position).getPartOfSpeech());
                    tabLayout.post(() -> {
                        View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(position);
                        if (tabView != null) {
                            if (position == 0) {
                                tabView.setBackgroundResource(R.drawable.tab_first);  // ✅ First tab
                            } else if (position == meanings.size() - 1) {
                                tabView.setBackgroundResource(R.drawable.tab_last);  // ✅ Last tab
                            } else {
                                tabView.setBackgroundResource(R.drawable.tab_middle);  // ✅ Middle tabs
                            }
                        }
                    });
                }
                ).attach();
            }
            phoneticAdapter.updateData(wordData.getPhonetics());
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
