package com.group5.preppal.viewmodel;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.data.model.User;
import com.group5.preppal.data.repository.AuthRepository;

import java.util.Date;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
        userLiveData.setValue(authRepository.getCurrentUser());
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void signInWithEmail(String email, String password) {
        authRepository.signInWithEmail(email, password)
                .addOnSuccessListener(user -> {
                    Log.d("AuthViewModel", "User signed in: " + (user != null ? user.getEmail() : "NULL"));
                    userLiveData.setValue(user);
                })
                .addOnFailureListener(e -> errorLiveData.setValue(e.getMessage()));
    }

    public void signUpWithEmail(String email, String password, String name, Date dateOfBirth, User.Gender gender) {
        authRepository.signUpWithEmail(email, password, name, dateOfBirth, gender)
                .addOnSuccessListener(user -> {
                    Log.d("AuthViewModel", "User registered: " + user.getEmail());
                    userLiveData.setValue(user);
                })
                .addOnFailureListener(e -> {
                    Log.e("AuthViewModel", "Registration failed", e);
                    errorLiveData.setValue(e.getMessage());
                });
    }


    public void signInWithGoogle(AuthCredential credential) {
        authRepository.signInWithGoogle(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        Log.d("AuthViewModel", "Google sign-in successful: " + user.getEmail());
                        userLiveData.setValue(user);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AuthViewModel", "Google sign-in failed", e);
                    errorLiveData.setValue(e.getMessage());
                });
    }


    public void signOut() {
        authRepository.signOut();
        Log.d("AuthViewModel", "User signed out");
        userLiveData.setValue(null);
    }
}

