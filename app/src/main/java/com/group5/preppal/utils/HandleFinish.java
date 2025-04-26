package com.group5.preppal.utils;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.StudentBookedSpeaking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HandleFinish {

    public static void HandleFinishSpeakingTest(Student student) {
        if (student == null || student.getStudentBookedSpeaking() == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference studentRef = db.collection("students").document(student.getUid());

        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    // Bước 1: Lấy bản student mới nhất từ Firestore
                    Student freshStudent = transaction.get(studentRef).toObject(Student.class);
                    if (freshStudent == null) return null;

                    // Bước 2: Clone bookedSpeaking list ra mới
                    List<StudentBookedSpeaking> bookedSpeakings = new ArrayList<>();
                    if (freshStudent.getStudentBookedSpeaking() != null) {
                        bookedSpeakings.addAll(freshStudent.getStudentBookedSpeaking());
                    }

                    List<String> finishedTests = freshStudent.getFinishedSpeakingTests();
                    if (finishedTests == null) {
                        finishedTests = new ArrayList<>();
                    }

                    List<String> newlyFinished = new ArrayList<>();
                    Date now = new Date();

                    // Bước 3: Kiểm tra speaking test đã quá hạn
                    for (StudentBookedSpeaking booking : bookedSpeakings) {
                        if (booking.getBookedDate() != null && booking.getBookedDate().before(now)) {
                            String testId = booking.getSpeakingTestId();
                            if (testId != null && !finishedTests.contains(testId)) {
                                newlyFinished.add(testId);
                            }
                        }
                    }

                    // Bước 4: Nếu có speaking test mới hoàn thành, update field
                    if (!newlyFinished.isEmpty()) {
                        finishedTests.addAll(newlyFinished);
                        transaction.update(studentRef, "finishedSpeakingTests", finishedTests);
                        Log.d("HandleFinish", "Transaction: Updated finished speaking tests.");
                    }

                    return null;
                }).addOnSuccessListener(aVoid -> Log.d("HandleFinish", "Transaction success"))
                .addOnFailureListener(e -> Log.e("HandleFinish", "Transaction failure", e));
    }
}
