package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.repository.WritingTestRepository;

import java.util.List;
import java.util.Map;

public class WritingTestViewModel extends ViewModel {
    private final WritingTestRepository repository;
    private final MutableLiveData<Map<String, List<Map<String, String>>>> writingTests = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public WritingTestViewModel() {
        this.repository = new WritingTestRepository();
    }

    public WritingTestViewModel(WritingTestRepository repository) {
        this.repository = repository;
    }

    public LiveData<Map<String, List<Map<String, String>>>> getWritingTests() {
        return writingTests;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchWritingTests() {
        repository.getAllWritingTests(new WritingTestRepository.FirestoreCallback() {
            @Override
            public void onSuccess(Map<String, List<Map<String, String>>> tests) {
                writingTests.postValue(tests);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }
}
