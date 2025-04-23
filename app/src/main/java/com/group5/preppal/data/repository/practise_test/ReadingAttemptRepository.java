package com.group5.preppal.data.repository.practise_test;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.group5.preppal.data.model.test.reading.ReadingAttempt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ReadingAttemptRepository {

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    @Inject
    public ReadingAttemptRepository(FirebaseFirestore db, FirebaseAuth auth) {
        this.db = db;
        this.auth = auth;
    }

    public void submitReadingAttempt(
            @NonNull ReadingAttempt attempt,
            @NonNull OnSuccessListener<Void> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        String userId = auth.getCurrentUser().getUid();
        CollectionReference attemptsRef = db
                .collection("students")
                .document(userId)
                .collection("reading_attempts");

        attemptsRef
                .add(attempt)
                .addOnSuccessListener(doc -> onSuccess.onSuccess(null))
                .addOnFailureListener(onFailure);
    }

    public void getReadingAttempts(
            @NonNull String testId,
            @NonNull Consumer<List<ReadingAttempt>> onSuccess,
            @NonNull Consumer<Exception> onFailure
    ) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("students")
                .document(userId)
                .collection("reading_attempts")
                .whereEqualTo("testId", testId)
                .orderBy("submittedAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<ReadingAttempt> list = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        ReadingAttempt attempt = doc.toObject(ReadingAttempt.class);
                        list.add(attempt);
                    }
                    onSuccess.accept(list);
                })
                .addOnFailureListener(onFailure::accept);
    }
}

