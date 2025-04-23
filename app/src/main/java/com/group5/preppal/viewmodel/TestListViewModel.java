package com.group5.preppal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.group5.preppal.data.model.test.Test;
import com.group5.preppal.data.model.test.TestAttempt;
import com.group5.preppal.data.repository.practise_test.TestAttemptRepository;
import com.group5.preppal.data.repository.practise_test.TestRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TestListViewModel extends ViewModel {

    private final TestRepository testRepository;
    private final TestAttemptRepository testAttemptRepository;

    private final MutableLiveData<List<Test>> tests = new MutableLiveData<>();
    private final MutableLiveData<Map<String, TestAttempt>> testAttempts = new MutableLiveData<>();

    @Inject
    public TestListViewModel(TestRepository testRepository, TestAttemptRepository testAttemptRepository) {
        this.testRepository = testRepository;
        this.testAttemptRepository = testAttemptRepository;
    }

    public LiveData<List<Test>> getTests() {
        return tests;
    }

    public LiveData<Map<String, TestAttempt>> getTestAttempts() {
        return testAttempts;
    }

    public void fetchTests(String testSetId) {
        testRepository.getTestsByTestSetId(testSetId,
                tests::setValue,
                e -> Log.e("TestListViewModel", "Failed to fetch tests", e));
    }

    public void fetchTestAttempts() {
        testAttemptRepository.getAllTestAttemptsForCurrentUser(
                testAttempts::setValue,
                e -> Log.e("TestListViewModel", "Failed to fetch attempts", e)
        );
    }
}


