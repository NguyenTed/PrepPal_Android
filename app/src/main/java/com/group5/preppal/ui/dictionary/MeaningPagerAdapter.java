package com.group5.preppal.ui.dictionary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.group5.preppal.data.model.DictionaryResponse;

import java.util.ArrayList;
import java.util.List;

public class MeaningPagerAdapter extends FragmentStateAdapter {
    private List<DictionaryResponse.Meaning> meanings;

    public MeaningPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<DictionaryResponse.Meaning> meanings) {
        super(fragmentActivity);
        this.meanings = meanings;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ArrayList<DictionaryResponse.Meaning.Definition> definitions = new ArrayList<>(meanings.get(position).getDefinitions());
        return MeaningFragment.newInstance(definitions);
    }


    @Override
    public int getItemCount() {
        return meanings.size();
    }
}

