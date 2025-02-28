package com.group5.preppal.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.User;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<User> userDetailsLiveData;
    private final MutableLiveData<Boolean> isLoggedOutLiveData;

    @Inject
    public AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.userDetailsLiveData = new MutableLiveData<>();
        this.isLoggedOutLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            fetchUserDetails(firebaseAuth.getCurrentUser().getUid());
        }
    }

    public void registerUser(String email, String password, String name, Date dateOfBirth) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User newUser = new User(user.getUid(), email, name, new Date());
                            storeUserInFirestore(newUser);
                            userLiveData.postValue(user);
                            userDetailsLiveData.postValue(newUser);
                        }
                    }
                });
    }

    private void storeUserInFirestore(User user) {
        firestore.collection("students").document(user.getUid()).set(user);
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            userLiveData.postValue(user);
                            fetchUserDetails(user.getUid());
                        }
                    }
                });
    }

    private void fetchUserDetails(String uid) {
        firestore.collection("students").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        userDetailsLiveData.postValue(user);
                    }
                });
    }

    public void logoutUser() {
        firebaseAuth.signOut();
        isLoggedOutLiveData.postValue(true);
        userLiveData.postValue(null);  // Clears user data
        userDetailsLiveData.postValue(null); // Prevents triggering login observer
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

