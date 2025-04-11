package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group5.preppal.data.model.WritingQuizSubmission;
import com.group5.preppal.data.model.enums.SubmissionState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WritingQuizSubmissionRepository {
    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;
    private CollectionReference resultsCollection;

    @Inject
    public WritingQuizSubmissionRepository(FirebaseFirestore firestore, FirebaseAuth firebaseAuth) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
    }

    public void init() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        this.resultsCollection = firestore.collection("students").document(user.getUid()).collection("writing_quiz_submissions");
    }

    public LiveData<List<WritingQuizSubmission>> getAllQuizSubmission () {
        MutableLiveData <List<WritingQuizSubmission>> allQuizSubmissionLiveData = new MutableLiveData<>();

        resultsCollection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<WritingQuizSubmission> submissions = new java.util.ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            WritingQuizSubmission submission = doc.toObject(WritingQuizSubmission.class);
                            submission.setId(doc.getId());
                            submissions.add(submission);
                        }
                        allQuizSubmissionLiveData.setValue(submissions);
                    } else {
                        allQuizSubmissionLiveData.setValue(new java.util.ArrayList<>());
                    }
                })
                .addOnFailureListener(e -> {
                    allQuizSubmissionLiveData.setValue(new java.util.ArrayList<>());
                });
        return allQuizSubmissionLiveData;
    }

    public LiveData<List<WritingQuizSubmission>> getPendingSubmissionsWithTaskId(String taskId) {
        MutableLiveData<List<WritingQuizSubmission>> pendingSubmissionsLiveData = new MutableLiveData<>();
        FirebaseFirestore.getInstance()
                .collectionGroup("writing_quiz_submissions")
                .whereEqualTo("taskId", taskId)
                .whereEqualTo("state", SubmissionState.PENDING.getDisplayName())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<WritingQuizSubmission> pendingList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        WritingQuizSubmission submission = doc.toObject(WritingQuizSubmission.class);
                        submission.setId(doc.getId());
                        pendingList.add(submission);
                    }
                    pendingSubmissionsLiveData.setValue(pendingList);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Query failed: ", e);
                    pendingSubmissionsLiveData.setValue(new ArrayList<>());
                });

        return pendingSubmissionsLiveData;
    }


    public LiveData<WritingQuizSubmission> getQuizSubmissionById(String submissionId) {
        MutableLiveData<WritingQuizSubmission> submissionLiveData = new MutableLiveData<>();

        FirebaseFirestore.getInstance()
                .collectionGroup("writing_quiz_submissions")
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (QueryDocumentSnapshot doc : snapshot) {
                        if (doc.getId().equals(submissionId)) {
                            WritingQuizSubmission submission = doc.toObject(WritingQuizSubmission.class);
                            submission.setId(doc.getId());
                            submissionLiveData.setValue(submission);
                            return; // đã tìm thấy, không cần duyệt tiếp
                        }
                    }
                    // Nếu không tìm thấy
                    submissionLiveData.setValue(null);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    submissionLiveData.setValue(null);
                });

        return submissionLiveData;
    }


    public LiveData<WritingQuizSubmission> getQuizSubmissionByTaskId(String taskId) {
        MutableLiveData<WritingQuizSubmission> quizSubmissionLiveData = new MutableLiveData<>();

        resultsCollection
                .whereEqualTo("taskId", taskId)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        WritingQuizSubmission submission = document.toObject(WritingQuizSubmission.class);
                        submission.setId(document.getId());
                        quizSubmissionLiveData.setValue(submission);
                    } else {
                        quizSubmissionLiveData.setValue(null);
                    }
                });

        return quizSubmissionLiveData;
    }

    public void saveWritingQuizSubmission(WritingQuizSubmission submission, String taskId, String userId, SubmissionCallback callback) {
        CollectionReference Collection = firestore
                .collection("students")
                .document(userId)
                .collection("writing_quiz_submissions");

        Collection
                .whereEqualTo("taskId", taskId)
                .whereEqualTo("userId", userId)
                .limit(1)
                .get()
                .addOnSuccessListener(task -> {
                    if (!task.isEmpty()) {
                        // 🔁 Đã tồn tại → cập nhật
                        DocumentSnapshot document = task.getDocuments().get(0);
                        String submissionId = document.getId();
                        submission.setId(submissionId);

                        Collection.document(submissionId)
                                .set(submission)
                                .addOnSuccessListener(aVoid -> callback.onSuccess("The writing submission was updated successfully!"))
                                .addOnFailureListener(e -> callback.onFailure("Failed to update: " + e.getMessage()));
                    } else {
                        // 🆕 Chưa tồn tại → thêm mới
                        Collection
                                .add(submission)
                                .addOnSuccessListener(docRef -> callback.onSuccess("The writing submission was saved successfully!"))
                                .addOnFailureListener(e -> callback.onFailure("Failed to save: " + e.getMessage()));
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Query failed: " + e.getMessage());
                });
    }


    public interface SubmissionCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}