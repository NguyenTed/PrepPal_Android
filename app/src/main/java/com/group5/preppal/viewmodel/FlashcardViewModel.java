package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.Vocabulary;
import com.group5.preppal.data.repository.VocabularyRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FlashcardViewModel extends ViewModel {

    private final VocabularyRepository repository;
    private final MutableLiveData<List<Vocabulary>> vocabularies = new MutableLiveData<>();

    @Inject
    public FlashcardViewModel(VocabularyRepository repository) {
        this.repository = repository;
    }

    public void loadVocabularies(String topicId) {
        repository.getVocabularies(topicId).observeForever(vocabularies::setValue);
    }

    public LiveData<List<Vocabulary>> getVocabularies() {
        return vocabularies;
    }
}
