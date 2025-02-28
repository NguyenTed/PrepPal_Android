package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.data.model.User;
import com.group5.preppal.data.repository.AuthRepository;

import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final LiveData<FirebaseUser> userLiveData;
    private final LiveData<User> userDetailsLiveData;
    private final LiveData<Boolean> isLoggedOutLiveData;

    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
        this.userLiveData = authRepository.getUserLiveData();
        this.userDetailsLiveData = authRepository.getUserDetailsLiveData();
        this.isLoggedOutLiveData = authRepository.getIsLoggedOutLiveData();
    }

    public void register(String email, String password, String name, Date dateOfBirth) {
        authRepository.registerUser(email, password, name, dateOfBirth);
    }

    public void login(String email, String password) {
        authRepository.loginUser(email, password);
    }

    public void logout() {
        authRepository.logoutUser();
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<User> getUserDetailsLiveData() {
        return userDetailsLiveData;
    }

    public LiveData<Boolean> getIsLoggedOutLiveData() {
        return isLoggedOutLiveData;
    }
}
