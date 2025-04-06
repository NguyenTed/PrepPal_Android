package com.group5.preppal.ui.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group5.preppal.R;
import com.group5.preppal.viewmodel.ManageTeachersViewModel;

public class ManageTeachersActivity extends AppCompatActivity {

    private ManageTeachersViewModel viewModel;
    private TeacherAdapter adapter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FloatingActionButton addTeacherBtn;
    private ActivityResultLauncher<Intent> addTeacherLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teachers);

        viewModel = new ViewModelProvider(this).get(ManageTeachersViewModel.class);

        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.teachers_recycler_view);
        addTeacherBtn = findViewById(R.id.add_teacher_button);

        adapter = new TeacherAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel.getFilteredTeachers().observe(this, adapter::submitList);

        searchView.setOnClickListener(v -> {
            searchView.setIconified(false); // expand it if needed
            searchView.requestFocus();

            // show keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(searchView.findFocus(), InputMethodManager.SHOW_IMPLICIT);
            }
        });

        addTeacherBtn.setOnClickListener(v ->
                startActivity(new Intent(this, AddNewTeacherActivity.class))
        );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.filter(newText);
                return true;
            }
        });

        addTeacherLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Reload teacher list
                        viewModel.reload();
                    }
                }
        );

        addTeacherBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNewTeacherActivity.class);
            addTeacherLauncher.launch(intent);
        });
    }
}


