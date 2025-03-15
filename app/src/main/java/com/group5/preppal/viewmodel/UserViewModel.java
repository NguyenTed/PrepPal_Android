package com.group5.preppal.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.User;
import com.group5.preppal.data.repository.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final LiveData<User> currentUserLiveData;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUserLiveData = userRepository.getCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return currentUserLiveData;
    }

    //  Update user currentBand & aimBand
    public LiveData<Boolean> updateUserBand(double currentBand, double aimBand) {
        return userRepository.updateUserBand(currentBand, aimBand);
    }
}

