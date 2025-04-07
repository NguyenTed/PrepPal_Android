package com.group5.preppal.data.repository.practise_test;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.group5.preppal.data.model.test.listening.ListeningAttempt;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ListeningAttemptRepository {

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    @Inject
    public ListeningAttemptRepository(FirebaseFirestore db, FirebaseAuth auth) {
        this.db = db;
        this.auth = auth;
    }

    public void submitListeningAttempt(
            @NonNull ListeningAttempt attempt,
            @NonNull OnSuccessListener<Void> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        String userId = auth.getCurrentUser().getUid();
        CollectionReference attemptsRef = db
                .collection("students")
                .document(userId)
                .collection("listening_attempts");

        attemptsRef
                .add(attempt)
                .addOnSuccessListener(doc -> onSuccess.onSuccess(null))
                .addOnFailureListener(onFailure);
    }

    // Optional: Fetch latest attempt by testId
    public void getLatestAttemptByTestId(
            String testId,
            @NonNull OnSuccessListener<ListeningAttempt> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("students")
                .document(userId)
                .collection("listening_attempts")
                .whereEqualTo("testId", testId)
                .orderBy("submittedAt", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        ListeningAttempt attempt = querySnapshot.getDocuments().get(0).toObject(ListeningAttempt.class);
                        onSuccess.onSuccess(attempt);
                    } else {
                        onSuccess.onSuccess(null);
                    }
                })
                .addOnFailureListener(onFailure);
    }
}
