package com.group5.preppal.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.group5.preppal.data.model.test.TestSet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestSetRepository {

    private final FirebaseFirestore db;

    @Inject
    public TestSetRepository(FirebaseFirestore db) {
        this.db = db;
    }

    public void getAllTestSets(
            @NonNull OnSuccessListener<List<TestSet>> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        db.collection("test_sets")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<TestSet> testSets = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        TestSet testSet = doc.toObject(TestSet.class);
                         testSet.setId(doc.getId());
                        testSets.add(testSet);
                    }
                    onSuccess.onSuccess(testSets);
                })
                .addOnFailureListener(onFailure);
    }
}
