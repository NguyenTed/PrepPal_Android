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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.group5.preppal.data.model.test.TestAttempt;
import com.group5.preppal.data.model.test.listening.ListeningAttempt;
import com.group5.preppal.data.model.test.reading.ReadingAttempt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestAttemptRepository {

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    private final String STUDENT_COLLECTION_PATH = "students";
    private final String TEST_ATTEMPT_COLLECTION_PATH = "test_attempts";

    @Inject
    public TestAttemptRepository(FirebaseFirestore db, FirebaseAuth auth) {
        this.db = db;
        this.auth = auth;
    }

    public void updateSkillBandScore(String testAttemptId, String skill, float bandScore,
                                     OnSuccessListener<Void> onSuccess,
                                     OnFailureListener onFailure) {
        String userId = auth.getCurrentUser().getUid();

        // Map skill names to Firestore field names
        Map<String, String> skillFieldMap = new HashMap<>();
        skillFieldMap.put("listening", "listening_band_score");
        skillFieldMap.put("reading", "reading_band_score");
        skillFieldMap.put("writing", "writing_band_score");
        skillFieldMap.put("speaking", "speaking_band_score");

        String fieldName = skillFieldMap.get(skill.toLowerCase());

        if (fieldName == null) {
            onFailure.onFailure(new IllegalArgumentException("Unknown skill: " + skill));
            return;
        }

        DocumentReference docRef = db.collection(STUDENT_COLLECTION_PATH)
                .document(userId)
                .collection(TEST_ATTEMPT_COLLECTION_PATH)
                .document(testAttemptId);

        docRef.get().addOnSuccessListener(snapshot -> {
            Map<String, Object> data = new HashMap<>();
            data.put(fieldName, bandScore);

            if (snapshot.exists()) {
                // Document exists → safe to update
                docRef.update(data)
                        .addOnSuccessListener(onSuccess)
                        .addOnFailureListener(onFailure);
            } else {
                // Document does not exist → create it with the field
                docRef.set(data, SetOptions.merge())
                        .addOnSuccessListener(onSuccess)
                        .addOnFailureListener(onFailure);
            }
        }).addOnFailureListener(onFailure);
    }

    public void getAllTestAttemptsForCurrentUser(Consumer<Map<String, TestAttempt>> onSuccess,
                                                 Consumer<Exception> onFailure) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("students")
                .document(userId)
                .collection("test_attempts")
                .get()
                .addOnSuccessListener(snapshot -> {
                    Map<String, TestAttempt> map = new HashMap<>();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        TestAttempt attempt = doc.toObject(TestAttempt.class);
                        if (attempt != null) {
                            attempt.setTestId(doc.getId()); // document ID = testId
                            map.put(doc.getId(), attempt);
                        }
                    }
                    onSuccess.accept(map);
                })
                .addOnFailureListener(onFailure::accept);
    }
}