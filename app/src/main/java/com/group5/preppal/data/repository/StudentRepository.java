package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StudentRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference studentCollection;

    @Inject
    public StudentRepository (FirebaseFirestore firestore) {
        this.firestore = firestore;
        this.studentCollection = firestore.collection("students");
    }

    public LiveData<Student> getStudentById(String studentId) {
        MutableLiveData<Student> studentLiveData = new MutableLiveData<>();

        studentCollection.document(studentId).addSnapshotListener((documentSnapshot, error) -> {
            if (error != null || documentSnapshot == null || !documentSnapshot.exists()) {
                return;
            }

            try {
                String uid = documentSnapshot.getString("uid");
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String genderStr = documentSnapshot.getString("gender");
                User.Gender gender = User.Gender.valueOf(genderStr.toUpperCase()); // Assuming enum Gender has MALE, FEMALE
                Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
                float currentBand = documentSnapshot.getDouble("currentBand").floatValue();
                float aimBand = documentSnapshot.getDouble("aimBand").floatValue();
                List<String> courses = (List<String>) documentSnapshot.get("courses");
                List<String> finishedLessons = (List<String>) documentSnapshot.get("finishedLessons");
                Student student = new Student(
                        uid,
                        email,
                        name,
                        dateOfBirth,
                        gender,
                        "student",
                        currentBand,
                        aimBand,
                        courses,
                        finishedLessons
                );

                studentLiveData.setValue(student);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return studentLiveData;
    }

    public void saveFinishedLesson(String lessonId, String studentId) {
        studentCollection.document(studentId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                List<String> finishedLessons = (List<String>) task.getResult().get("finishedLessons");

                if (!finishedLessons.contains(lessonId)) {
                    finishedLessons.add(lessonId);

                    studentCollection.document(studentId)
                            .update("finishedLessons", finishedLessons)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "Lesson marked as finished");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore", "Failed to update finishedLessons", e);
                            });
                } else {
                    Log.d("Firestore", "Lesson already marked as finished");
                }

            } else {
                Log.e("Firestore", "Student document not found or error", task.getException());
            }
        });
    }
}
