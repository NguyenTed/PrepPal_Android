package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.group5.preppal.data.model.WritingTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WritingTestRepository {
    private static final String COLLECTION_NAME = "writing_tests";
    private static final String SUBCOLLECTION_NAME = "tasks"; // Lấy subcollection "tasks"
    private final FirebaseFirestore db;

    public WritingTestRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface FirestoreCallback {
        void onSuccess(Map<String, List<Map<String, String>>> writingTests);
        void onFailure(Exception e);
    }

    public void getAllWritingTests(FirestoreCallback callback) {
        CollectionReference writingTestsRef = db.collection(COLLECTION_NAME);
        Map<String, List<Map<String, String>>> topicMap = new HashMap<>();

        writingTestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DocumentSnapshot doc : task.getResult()) {
                    String topicName = doc.getString("name"); // Tên chủ đề
                    if (topicName == null) continue;

                    List<Map<String, String>> tasksList = new ArrayList<>();

                    // Lấy danh sách tasks trong subcollection
                    DocumentReference topicRef = writingTestsRef.document(doc.getId());
                    topicRef.collection(SUBCOLLECTION_NAME).get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful() && task1.getResult() != null) {
                                    for (DocumentSnapshot taskDoc : task1.getResult()) {
                                        Map<String, String> taskData = new HashMap<>();
                                        taskData.put("id", taskDoc.getString("id"));
                                        taskData.put("title", taskDoc.getString("title"));
                                        taskData.put("description", taskDoc.getString("description"));
                                        taskData.put("imgUrl", taskDoc.contains("imgUrl") ? taskDoc.getString("imgUrl") : "");
                                        tasksList.add(taskData);
                                    }
                                    topicMap.put(topicName, tasksList);
                                    if (topicMap.size() == task.getResult().size()) {
                                        callback.onSuccess(topicMap);
                                    }
                                } else {
                                    callback.onFailure(task1.getException());
                                }
                            });
                }
            } else {
                callback.onFailure(task.getException());
                Log.e("Firestore", "Lỗi khi lấy dữ liệu", task.getException());
            }
        });
    }
}
