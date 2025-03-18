package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.group5.preppal.data.model.Task;
import com.group5.preppal.data.model.WritingTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WritingTestRepository {
    private static final String COLLECTION_NAME = "writing_tests";
    private static final String SUBCOLLECTION_NAME = "tasks"; // Lấy subcollection "tasks"
    private final FirebaseFirestore db;

    @Inject
    public WritingTestRepository(FirebaseFirestore firestore) {
        this.db = firestore;
    }

    public interface FirestoreCallback {
        void onSuccess(List<WritingTest> writingTests);
        void onFailure(Exception e);
    }

    public void getAllWritingTests(FirestoreCallback callback) {
        CollectionReference writingTestsRef = db.collection(COLLECTION_NAME);
        List<WritingTest> writingTestsList = new ArrayList<>();

        writingTestsRef.get().addOnCompleteListener(test -> {
            if (test.isSuccessful() && test.getResult() != null) {
                for (DocumentSnapshot doc : test.getResult()) {
                    String writingTestId = doc.getId();
                    String topicName = doc.getString("name"); // Tên chủ đề
                    if (topicName == null) continue;

                    WritingTest writingTest = new WritingTest(writingTestId, topicName, new ArrayList<>());

                    // Lấy danh sách tasks trong subcollection
                    DocumentReference topicRef = writingTestsRef.document(writingTestId);
                    topicRef.collection(SUBCOLLECTION_NAME).get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    List<Task> taskList = new ArrayList<>();
                                    for (DocumentSnapshot taskDoc : task.getResult()) {
                                        String taskId = taskDoc.getId();
                                        String title = taskDoc.getString("title");
                                        String description = taskDoc.getString("description");
                                        String imgUrl = taskDoc.contains("imgUrl") ? taskDoc.getString("imgUrl") : "";
                                        String taskType = taskDoc.getString("taskType");

                                        taskList.add(new Task(taskId, description, imgUrl, taskType, title));
                                    }

                                    writingTest.setTasks(taskList);
                                    writingTestsList.add(writingTest);

                                    if (writingTestsList.size() == test.getResult().size()) {
                                        callback.onSuccess(writingTestsList);
                                    }
                                } else {
                                    callback.onFailure(task.getException());
                                }
                            });
                }
            } else {
                callback.onFailure(test.getException());
                Log.e("Firestore", "Lỗi khi lấy dữ liệu", test.getException());
            }
        });
    }
}
