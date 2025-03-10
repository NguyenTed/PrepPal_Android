package com.group5.preppal.data.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.group5.preppal.data.model.WritingTest;

import java.util.ArrayList;
import java.util.List;

public class WritingTestRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference writingTestsRef = db.collection("writing_tests");

    @FunctionalInterface
    public interface FirestoreCallback {
        void onSuccess(List<WritingTest> writingTests);
    }

    public interface FirestoreErrorCallback {
        void onError(Exception e);
    }

    public void getWritingTests(FirestoreCallback successCallback, FirestoreErrorCallback errorCallback) {
        writingTestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<WritingTest> writingTestList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    WritingTest test = new WritingTest(
                            document.getId(),
                            document.getString("title"),
                            document.getString("description")
                    );
                    writingTestList.add(test);
                }
                successCallback.onSuccess(writingTestList);
            } else {
                errorCallback.onError(task.getException());
            }
        });
    }
}
