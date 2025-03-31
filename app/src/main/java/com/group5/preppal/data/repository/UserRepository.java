package com.group5.preppal.data.repository;

import android.content.Context;
import android.util.Log;
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
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
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
        String uid = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
        Log.d("uid:", "uid: " + uid);
        if (uid == null) {
            userLiveData.setValue(null);
            return userLiveData;
        }

        // Try fetching from each collection
        fetchUserFromCollection("students", Student.class, uid, userLiveData, () ->
                fetchUserFromCollection("teachers", Teacher.class, uid, userLiveData, () ->
                        fetchUserFromCollection("admins", Admin.class, uid, userLiveData, () ->
                                userLiveData.setValue(null) // Nếu không tìm thấy user
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
                    Log.d("UserRepo", "Checking " + collection + " for UID: " + uid);
                    if (document.exists()) {
                        Log.d("UserRepo", "User found in " + collection + ": " + document.getData());

                        try {
                            T user = document.toObject(clazz);
                            userLiveData.setValue(user);
                        } catch (Exception e) {
                            Log.e("UserRepo", "Failed to convert Firestore doc to " + clazz.getSimpleName(), e);
                            userLiveData.setValue(null);
                        }
                    } else {
                        Log.d("UserRepo", "User not found in collection: " + collection);
                        nextAttempt.run();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("UserRepo", "Failed to fetch from collection: " + collection, e);
                    nextAttempt.run();
                });
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
    public void addCourseToStudent(String uid, String courseId) {
        firestore.collection("students").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Student student = documentSnapshot.toObject(Student.class);
                        if (student != null) {
                            List<String> courses = student.getCourses();
                            if (!courses.contains(courseId)) {
                                courses.add(courseId); // ✅ Thêm courseId mới vào danh sách

                                // ✅ Cập nhật Firestore
                                firestore.collection("students").document(uid)
                                        .update("courses", courses)
                                        .addOnSuccessListener(aVoid -> Log.d("AuthRepository", "Course added successfully"))
                                        .addOnFailureListener(Throwable::printStackTrace);
                            } else {
                                Log.d("AuthRepository", "Course already exists in student's list");
                            }
                        }
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

}

