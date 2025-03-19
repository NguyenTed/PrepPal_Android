package com.group5.preppal.data.repository;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Task;

public class TaskRepository {
    private static final String COLLECTION_NAME = "writing_tests"; // Collection chứa WritingTest
    private static final String SUBCOLLECTION_NAME = "tasks"; // Subcollection chứa các task
    private final FirebaseFirestore db;

    public TaskRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface FirestoreTaskCallback {
        void onSuccess(Task task);
        void onFailure(Exception e);
    }

    public void findTaskById(String taskId, FirestoreTaskCallback callback) {
        CollectionReference writingTestsRef = db.collection(COLLECTION_NAME);

        writingTestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DocumentSnapshot doc : task.getResult()) {
                    String writingTestId = doc.getId();

                    writingTestsRef.document(writingTestId)
                            .collection(SUBCOLLECTION_NAME)
                            .document(taskId)
                            .get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful() && task1.getResult() != null && task1.getResult().exists()) {
                                    DocumentSnapshot taskDoc = task1.getResult();
                                    String title = taskDoc.getString("title");
                                    String description = taskDoc.getString("description");
                                    String imgUrl = taskDoc.contains("imgUrl") ? taskDoc.getString("imgUrl") : "";
                                    String taskType = taskDoc.getString("taskType");

                                    Task foundTask = new Task(taskId, description, imgUrl, taskType, title);
                                    callback.onSuccess(foundTask);
                                    return;
                                }
                            });
                }
                callback.onFailure(new Exception("Không tìm thấy Task với ID: " + taskId));
            } else {
                callback.onFailure(task.getException());
                Log.e("TaskRepository", "Lỗi khi lấy dữ liệu", task.getException());
            }
        });
    }
}
