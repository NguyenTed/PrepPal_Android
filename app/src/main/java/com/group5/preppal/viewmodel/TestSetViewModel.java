package com.group5.preppal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.test.TestSet;
import com.group5.preppal.data.repository.TestRepository;
import com.group5.preppal.data.repository.TestSetRepository;
import com.group5.preppal.ui.test_set.TestSetAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TestSetViewModel extends ViewModel {

    private final TestSetRepository testSetRepository;
    private final MutableLiveData<List<TestSet>> testSets = new MutableLiveData<>();

    @Inject
    public TestSetViewModel(TestSetRepository testSetRepository) {
        this.testSetRepository = testSetRepository;
    }

    public LiveData<List<TestSet>> getTestSets() {
        return testSets;
    }

    public void fetchTestSets() {
        testSetRepository.getAllTestSets(
                sets -> testSets.setValue(sets),
                e -> Log.e("TestSetViewModel", "Error loading test sets", e)
        );
    }
}

