package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.group5.preppal.data.model.TopicWithProgress;
import com.group5.preppal.data.repository.TopicRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TopicViewModel extends ViewModel {

    private final TopicRepository repository;

    @Inject
    public TopicViewModel(TopicRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<TopicWithProgress>> getTopics(String userId) {
        return repository.getTopicsWithProgress(userId); // ✅ with userId
    }

}
