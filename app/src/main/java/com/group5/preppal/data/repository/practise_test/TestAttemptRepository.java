package com.group5.preppal.data.repository.practise_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.test.TestAttempt;
import com.group5.preppal.data.model.test.reading.ReadingAttempt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestAttemptRepository {

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    @Inject
    public TestAttemptRepository(FirebaseFirestore db, FirebaseAuth auth) {
        this.db = db;
        this.auth = auth;
    }

    // Save a new test attempt
    public void saveTestAttempt(
            @NonNull TestAttempt attempt,
            @Nullable String attemptId, // null = auto-generated
            @NonNull OnSuccessListener<Void> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        String userId = auth.getCurrentUser().getUid();
        DocumentReference docRef;

        if (attemptId == null) {
            docRef = db.collection("students").document(userId)
                    .collection("test_attempts").document();
        } else {
            docRef = db.collection("students").document(userId)
                    .collection("test_attempts").document(attemptId);
        }

        docRef.set(attempt)
                .addOnSuccessListener(unused -> onSuccess.onSuccess(null))
                .addOnFailureListener(onFailure);
    }

    // Load all test attempts by current user
    public void getTestAttempts(
            @NonNull OnSuccessListener<List<TestAttempt>> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("students")
                .document(userId)
                .collection("test_attempts")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<TestAttempt> attempts = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        TestAttempt attempt = doc.toObject(TestAttempt.class);
                        if (attempt != null) {
                            attempts.add(attempt);
                        }
                    }
                    onSuccess.onSuccess(attempts);
                })
                .addOnFailureListener(onFailure);
    }

    // Optional: Get a specific attempt
    public void getTestAttemptById(
            @NonNull String attemptId,
            @NonNull OnSuccessListener<TestAttempt> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("students")
                .document(userId)
                .collection("test_attempts")
                .document(attemptId)
                .get()
                .addOnSuccessListener(doc -> {
                    TestAttempt attempt = doc.toObject(TestAttempt.class);
                    if (attempt != null) {
                        onSuccess.onSuccess(attempt);
                    } else {
                        onFailure.onFailure(new Exception("Attempt not found"));
                    }
                })
                .addOnFailureListener(onFailure);
    }

    public void submitReadingAttempt(
            String userId,
            String testSetId,
            String testId,
            ReadingAttempt readingAttempt,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("test_id", testId);
        data.put("test_set_id", testSetId);
        data.put("started_at", FieldValue.serverTimestamp());
        data.put("submitted_at", FieldValue.serverTimestamp());
        data.put("reading_attempt", readingAttempt);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("test_attempts")
                .add(data)
                .addOnSuccessListener(doc -> onSuccess.onSuccess(null))
                .addOnFailureListener(onFailure);
    }
}