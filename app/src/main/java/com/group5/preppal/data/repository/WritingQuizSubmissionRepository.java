package com.group5.preppal.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.group5.preppal.data.model.WritingQuizSubmission;

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