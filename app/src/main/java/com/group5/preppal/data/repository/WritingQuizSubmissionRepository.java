package com.group5.preppal.data.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group5.preppal.data.model.WritingQuizSubmission;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WritingQuizSubmissionRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference resultsCollection;

    @Inject
    public WritingQuizSubmissionRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
        this.resultsCollection = firestore.collection("writing_quiz_submissions");
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

        resultsCollection
                .whereEqualTo("state", "pending")
                .whereEqualTo("taskId", taskId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<WritingQuizSubmission> pendingList = new java.util.ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        WritingQuizSubmission submission = doc.toObject(WritingQuizSubmission.class);
                        submission.setId(doc.getId());
                        pendingList.add(submission);
                    }
                    pendingSubmissionsLiveData.setValue(pendingList);
                })
                .addOnFailureListener(e -> {
                    // Ghi log nếu cần
                    pendingSubmissionsLiveData.setValue(new java.util.ArrayList<>());
                });

        return pendingSubmissionsLiveData;
    }

    public LiveData<WritingQuizSubmission> getQuizSubmissionById(String submissionId) {
        MutableLiveData<WritingQuizSubmission> writingSubmissionLiveData = new MutableLiveData<>();

        resultsCollection.document(submissionId).addSnapshotListener((documentSnapshot, error) -> {
            if (error != null || documentSnapshot == null || !documentSnapshot.exists()) {
                writingSubmissionLiveData.setValue(null);
                return;
            }
            WritingQuizSubmission submission = documentSnapshot.toObject(WritingQuizSubmission.class);
            if (submission != null) {
                submission.setId(documentSnapshot.getId()); // Gán thêm ID nếu cần
                writingSubmissionLiveData.setValue(submission);
            }
        });
        return writingSubmissionLiveData;
    }


    public LiveData<WritingQuizSubmission> getQuizSubmissionByTaskId(String taskId, String userId) {
        MutableLiveData<WritingQuizSubmission> quizSubmissionLiveData = new MutableLiveData<>();

        resultsCollection
                .whereEqualTo("taskId", taskId)
                .whereEqualTo("userId", userId)
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
        resultsCollection
                .whereEqualTo("taskId", taskId)
                .whereEqualTo("userId", userId)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        String submissionId = document.getId();
                        submission.setId(submissionId);
                        resultsCollection.document(submissionId)
                                .set(submission)
                                .addOnSuccessListener(aVoid -> callback.onSuccess("The writing submission update successfully!"))
                                .addOnFailureListener(e -> callback.onFailure("Fail to update: " + e.getMessage()));

                    } else {
                        resultsCollection
                                .add(submission)
                                .addOnSuccessListener(documentReference -> callback.onSuccess("The writing is saved successfully"))
                                .addOnFailureListener(e -> callback.onFailure("Fail to save " + e.getMessage()));
                    }
                });
    }

    // 🔥 Interface để xử lý kết quả khi lưu submission
    public interface SubmissionCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}