package com.group5.preppal.data.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Admin;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.Teacher;
import com.group5.preppal.data.model.User;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class UserRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    @Inject
    public UserRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firestore) {
        this.firebaseAuth = firebaseAuth;
        this.firestore = firestore;
    }

    public LiveData<User> getCurrentUser() {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        String uid = firebaseAuth.getCurrentUser().getUid();

        // Try fetching from each collection
        fetchUserFromCollection("admins", Admin.class, uid, userLiveData, () ->
                fetchUserFromCollection("teachers", Teacher.class, uid, userLiveData, () ->
                        fetchUserFromCollection("students", Student.class, uid, userLiveData, () ->
                                userLiveData.setValue(null) // User not found in any collection
                        )
                )
        );

        return userLiveData;
    }

    private <T extends User> void fetchUserFromCollection(
            String collection, Class<T> clazz, String uid,
            MutableLiveData<User> userLiveData, Runnable nextAttempt) {

        firestore.collection(collection).document(uid).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        userLiveData.setValue(document.toObject(clazz));
                    } else {
                        nextAttempt.run();
                    }
                })
                .addOnFailureListener(e -> nextAttempt.run());
    }

    //  Update currentBand and aimBand to Firestore
    public LiveData<Boolean> updateUserBand(double currentBand, double aimBand) {
        MutableLiveData<Boolean> updateStatus = new MutableLiveData<>();
        String uid = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;

        if (uid == null) {
            updateStatus.setValue(false);
            return updateStatus;
        }

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("currentBand", currentBand);
        userUpdates.put("aimBand", aimBand);

        firestore.collection("students").document(uid)
                .update(userUpdates)
                .addOnSuccessListener(aVoid -> updateStatus.setValue(true))
                .addOnFailureListener(e -> updateStatus.setValue(false));

        return updateStatus;
    }

}

