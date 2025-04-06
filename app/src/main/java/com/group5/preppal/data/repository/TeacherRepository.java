package com.group5.preppal.data.repository;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherRepository {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public interface OnTeachersFetched {
        void onResult(List<Teacher> teachers);
    }

    public void fetchAllTeachers(OnTeachersFetched callback) {
        firestore.collection("teachers").get()
                .addOnSuccessListener(snapshot -> {
                    List<Teacher> list = new ArrayList<>();
                    snapshot.forEach(doc -> {
                        Teacher teacher = doc.toObject(Teacher.class);
                        list.add(teacher);
                    });
                    callback.onResult(list);
                })
                .addOnFailureListener(e -> {
                    Log.e("TeacherRepo", "Failed to fetch teachers", e);
                    callback.onResult(new ArrayList<>());
                });
    }
}

