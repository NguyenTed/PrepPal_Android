package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.StudentBookedSpeaking;
import com.group5.preppal.data.model.BookedTime;
import com.group5.preppal.data.model.SpeakingTest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SpeakingTestRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference testCollection;

    @Inject
    public SpeakingTestRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
        this.testCollection = firestore.collection("speaking_test");
    }

    public LiveData<SpeakingTest> getSpeakingTestById(String speakingTestId) {
        MutableLiveData<SpeakingTest> speakingTestLiveData = new MutableLiveData<>();
        testCollection.document(speakingTestId).addSnapshotListener((documentSnapshot, error) -> {
            if (error != null || documentSnapshot == null || !documentSnapshot.exists()) {
                return;
            }

            SpeakingTest speakingTest = documentSnapshot.toObject(SpeakingTest.class);
            speakingTest.setId(documentSnapshot.getId());
            speakingTestLiveData.setValue(speakingTest);
        });
        return speakingTestLiveData;
    }

    public LiveData<SpeakingTest> getSpeakingTestByCourseId(String courseId) {
        MutableLiveData<SpeakingTest> speakingTestLiveData = new MutableLiveData<>();
        testCollection
                .whereEqualTo("courseId", courseId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        SpeakingTest test = doc.toObject(SpeakingTest.class);
                        if (test != null) {
                            test.setId(doc.getId());
                        }
                        speakingTestLiveData.setValue(test);
                    } else {
                        speakingTestLiveData.setValue(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting SpeakingTest", e);
                    speakingTestLiveData.setValue(null);
                });
        return speakingTestLiveData;
    }

    public void saveBookedTime(String studentId, StudentBookedSpeaking studentBookedSpeaking) {
        testCollection.document(studentBookedSpeaking.getSpeakingTestId()).get().addOnCompleteListener(test -> {
            if (test.isSuccessful() && test.getResult() != null && test.getResult().exists()) {
                DocumentSnapshot snapshot = test.getResult();

                List<BookedTime> bookedTimeList = snapshot.toObject(SpeakingTest.class).getBookedTime();
                if (bookedTimeList == null)  bookedTimeList = new ArrayList<>();

                boolean updated = false;
                for (BookedTime bt : bookedTimeList) {
                    if ((bt.getStudentId().equals(studentId))) {
                        bt.setDate(studentBookedSpeaking.getBookedDate());
                        updated = true;
                        break;
                    }
                }

                if (!updated) {
                    BookedTime newBookedTime = new BookedTime(studentBookedSpeaking.getBookedDate(), studentId);
                    bookedTimeList.add(newBookedTime);
                }
                boolean finalUpdated = updated;
                testCollection.document(studentBookedSpeaking.getSpeakingTestId())
                        .update("bookedTime", bookedTimeList)
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", finalUpdated ? "Updated booked time" : "Added new booking"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Failed to update booked time", e));
            }
            else {
                Log.e("Firestore", "Booked Speaking document not found", test.getException());
            }
        });
    }
}
