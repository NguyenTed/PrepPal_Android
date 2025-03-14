package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.DictionaryResponse;
import com.group5.preppal.data.repository.DictionaryCallback;
import com.group5.preppal.data.repository.DictionaryRepository;

import java.util.List;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DictionaryViewModel extends ViewModel {
    private final DictionaryRepository repository;
    private final MutableLiveData<List<DictionaryResponse>> wordDefinition = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public DictionaryViewModel(DictionaryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<DictionaryResponse>> getWordDefinition() {
        return wordDefinition;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void searchWord(String word) {
        repository.fetchWordDefinition(word, new DictionaryCallback() {
            @Override
            public void onSuccess(List<DictionaryResponse> response) {
                wordDefinition.postValue(response);
            }

            @Override
            public void onFailure(String error) {
                errorMessage.postValue(error);
            }
        });
    }
}

