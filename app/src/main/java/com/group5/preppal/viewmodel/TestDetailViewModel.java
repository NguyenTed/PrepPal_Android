package com.group5.preppal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.test.listening.ListeningAttempt;
import com.group5.preppal.data.model.test.reading.ReadingAttempt;
import com.group5.preppal.data.repository.practise_test.ListeningAttemptRepository;
import com.group5.preppal.data.repository.practise_test.ReadingAttemptRepository;
import com.group5.preppal.data.repository.practise_test.TestAttemptRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TestDetailViewModel extends ViewModel {

    private final ListeningAttemptRepository listeningRepo;
    private final ReadingAttemptRepository readingRepo;

    private final MutableLiveData<List<ListeningAttempt>> listeningAttempts = new MutableLiveData<>();
    private final MutableLiveData<List<ReadingAttempt>> readingAttempts = new MutableLiveData<>();

    @Inject
    public TestDetailViewModel(
            ListeningAttemptRepository listeningRepo,
            ReadingAttemptRepository readingRepo
    ) {
        this.listeningRepo = listeningRepo;
        this.readingRepo = readingRepo;
    }

    public LiveData<List<ListeningAttempt>> getListeningAttempts() { return listeningAttempts; }
    public LiveData<List<ReadingAttempt>> getReadingAttempts() { return readingAttempts; }

    public void loadListeningAttempts(String testId) {
        listeningRepo.getListeningAttempts(testId, listeningAttempts::setValue,
                e -> Log.e("VM", "Listening failed", e));
    }

    public void loadReadingAttempts(String testId) {
        readingRepo.getReadingAttempts(testId, readingAttempts::setValue,
                e -> Log.e("VM", "Reading failed", e));
    }
}


