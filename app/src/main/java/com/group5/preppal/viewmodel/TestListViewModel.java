package com.group5.preppal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.group5.preppal.data.model.test.Test;
import com.group5.preppal.data.repository.TestRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TestListViewModel extends ViewModel {

    private final TestRepository testRepository;
    private final MutableLiveData<List<Test>> tests = new MutableLiveData<>();

    @Inject
    public TestListViewModel(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public LiveData<List<Test>> getTests() {
        return tests;
    }

    public void fetchTests(String testSetId) {
        testRepository.getTestsByTestSetId(testSetId,
                tests::setValue,
                e -> Log.e("TestListViewModel", "Failed to load tests", e));
    }
}


