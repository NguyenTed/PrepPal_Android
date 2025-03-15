package com.group5.preppal.ui.dictionary;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.DictionaryResponse;

import java.util.ArrayList;

public class MeaningFragment extends Fragment {
    private static final String ARG_DEFINITIONS = "definitions";
    private ArrayList<DictionaryResponse.Meaning.Definition> definitions;

    public static MeaningFragment newInstance(ArrayList<DictionaryResponse.Meaning.Definition> definitions) {
        MeaningFragment fragment = new MeaningFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DEFINITIONS, definitions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            definitions = (ArrayList<DictionaryResponse.Meaning.Definition>) getArguments().getSerializable(ARG_DEFINITIONS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meaning, container, false);
        RecyclerView rvDefinitions = view.findViewById(R.id.rvDefinitions);
        TextView tvNoDefinitions = view.findViewById(R.id.tvNoDefinitions);

        if (definitions != null && !definitions.isEmpty()) {
            rvDefinitions.setLayoutManager(new LinearLayoutManager(getContext()));
            DefinitionAdapter adapter = new DefinitionAdapter(definitions);
            rvDefinitions.setAdapter(adapter);
            tvNoDefinitions.setVisibility(View.GONE);
        } else {
            tvNoDefinitions.setVisibility(View.VISIBLE);
        }

        return view;
    }
}


