package com.group5.preppal.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Teacher;
import com.group5.preppal.data.model.User;

import java.util.Date;

public class AddNewTeacherViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface OnSuccess { void run(); }
    public interface OnFailure { void onError(String error); }

    public void addNewTeacher(Context context, String name, String email, String password, Date dob, User.Gender gender,
                              OnSuccess successCallback, OnFailure failureCallback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    Teacher teacher = new Teacher(uid, email, name, dob, gender, "teacher");

                    SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                    String adminEmail = prefs.getString("admin_email", null);
                    String adminPassword = prefs.getString("admin_password", null);

                    if (adminEmail != null && adminPassword != null) {
                        auth.signInWithEmailAndPassword(adminEmail, adminPassword)
                                .addOnSuccessListener(result -> successCallback.run())
                                .addOnFailureListener(e -> failureCallback.onError("Teacher added, but failed to re-login admin: " + e.getMessage()));
                    } else {
                        failureCallback.onError("Teacher added, but admin credentials not found.");
                    }

                    db.collection("teachers").document(uid).set(teacher)
                            .addOnSuccessListener(unused -> successCallback.run())
                            .addOnFailureListener(e -> failureCallback.onError(e.getMessage()));
                })
                .addOnFailureListener(e -> failureCallback.onError(e.getMessage()));
    }
}

