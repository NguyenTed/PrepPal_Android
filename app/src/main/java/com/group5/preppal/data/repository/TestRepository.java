package com.group5.preppal.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.group5.preppal.data.model.test.Test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestRepository {

    private final FirebaseFirestore db;

    @Inject
    public TestRepository(FirebaseFirestore db) {
        this.db = db;
    }

    // ✅ Load all tests for a testSetId
    public void getTestsByTestSetId(
            @NonNull String testSetId,
            @NonNull OnSuccessListener<List<Test>> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        db.collection("tests")
                .whereEqualTo("test_set_id", testSetId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Test> tests = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Test test = doc.toObject(Test.class);
                        test.setId(doc.getId()); // optional if you need document ID
                        tests.add(test);
                    }
                    onSuccess.onSuccess(tests);
                })
                .addOnFailureListener(onFailure);
    }

    // ✅ Optional: Load by list of test IDs (used for previews)
    public void getTestsByIds(
            @NonNull List<String> ids,
            @NonNull OnSuccessListener<List<Test>> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        if (ids.isEmpty()) {
            onSuccess.onSuccess(new ArrayList<>());
            return;
        }

        db.collection("tests")
                .whereIn(FieldPath.documentId(), ids)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Test> tests = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Test test = doc.toObject(Test.class);
                        test.setId(doc.getId());
                        tests.add(test);
                    }
                    onSuccess.onSuccess(tests);
                })
                .addOnFailureListener(onFailure);
    }
}