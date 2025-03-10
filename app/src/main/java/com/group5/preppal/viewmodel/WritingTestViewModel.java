package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.WritingTest;
import com.group5.preppal.data.repository.WritingTestRepository;

import java.util.List;

public class WritingTestViewModel extends ViewModel {
    private final WritingTestRepository repository = new WritingTestRepository();
    private final MutableLiveData<List<WritingTest>> writingTests = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<List<WritingTest>> getWritingTests() {
        return writingTests;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchWritingTests() {
        repository.getWritingTests(
                writingTests::setValue,
                error -> errorMessage.setValue(error.getMessage())
        );
    }
}
