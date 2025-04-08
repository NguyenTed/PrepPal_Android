package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.test.listening.ListeningPart;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ListeningViewModel extends ViewModel {

    private final MutableLiveData<ListeningPart> currentPart = new MutableLiveData<>();

    @Inject
    public ListeningViewModel() {
        // Hilt requires a constructor even if it's empty
    }

    public void setPart(ListeningPart part) {
        currentPart.setValue(part);
    }

    public LiveData<ListeningPart> getCurrentPart() {
        return currentPart;
    }
}
