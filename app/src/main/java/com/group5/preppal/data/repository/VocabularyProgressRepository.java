package com.group5.preppal.data.repository;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VocabularyProgressRepository {

    private final FirebaseFirestore db;

    @Inject
    public VocabularyProgressRepository(FirebaseFirestore db) {
        this.db = db;
    }

    public void markAsLearned(String userId, String topicId, String word,
                              Runnable onSuccess, Consumer<Exception> onError) {

        DocumentReference ref = db.collection("students")
                .document(userId)
                .collection("vocabularyProgress")
                .document(topicId);

        Map<String, Object> data = new HashMap<>();
        data.put("learnedVocabs", FieldValue.arrayUnion(word));

        ref.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> onSuccess.run())
                .addOnFailureListener(onError::accept);
    }
}

