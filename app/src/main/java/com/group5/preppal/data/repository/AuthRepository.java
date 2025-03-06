package com.group5.preppal.data.repository;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Singleton
public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;
    private final GoogleSignInClient googleSignInClient;

    @Inject
    public AuthRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firestore, GoogleSignInClient googleSignInClient) {
        this.firebaseAuth = firebaseAuth;
        this.firestore = firestore;
        this.googleSignInClient = googleSignInClient;
    }

    public Task<FirebaseUser> signInWithEmail(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return firebaseAuth.getCurrentUser();
                    } else {
                        throw task.getException();
                    }
                });
    }

    public Task<FirebaseUser> signUpWithEmail(String email, String password, String name, Date dateOfBirth, User.Gender gender) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        checkAndSaveNativeUser(firebaseAuth.getUid(), email, name, dateOfBirth, gender); // ✅ Save user if new
                        return firebaseAuth.getCurrentUser();
                    } else {
                        throw task.getException();
                    }
                });
    }


    public Task<AuthCredential> firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        return firebaseAuth.signInWithCredential(credential)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        checkAndSaveFirebaseUser(firebaseAuth.getCurrentUser()); // ✅ Save user if new
                        return credential;
                    } else {
                        throw task.getException();
                    }
                });
    }

    // Check if user exists in Firestore and save if new
    private void checkAndSaveFirebaseUser (FirebaseUser firebaseUser) {
        if (firebaseUser == null) return;

        firestore.collection("students").document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Student student = new Student(
                                firebaseUser.getUid(),
                                firebaseUser.getEmail(),
                                firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Unknown",
                                null,
                                User.Gender.OTHER,
                                0.0f,
                                6.5f,
                                new ArrayList<>()
                        );

                        firestore.collection("students").document(student.getUid())
                                .set(student)
                                .addOnSuccessListener(aVoid -> {})
                                .addOnFailureListener(Throwable::printStackTrace);
                    }
                });
    }

    private void checkAndSaveNativeUser(String uid, String email, String name, Date dateOfBirth, User.Gender gender) {
        firestore.collection("students").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Student student = new Student(
                                uid,
                                email,
                                name,
                                dateOfBirth,
                                gender,
                                0.0f,
                                6.5f,
                                new ArrayList<>()
                        );

                        firestore.collection("students").document(student.getUid())
                                .set(student)
                                .addOnSuccessListener(aVoid -> {})
                                .addOnFailureListener(Throwable::printStackTrace);
                    }
                });
    }



    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void addCourseToStudent(String uid, String courseId) {
        firestore.collection("students").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Student student = documentSnapshot.toObject(Student.class);
                        if (student != null) {
                            List<String> courses = student.getCourses();
                            if (!courses.contains(courseId)) {
                                courses.add(courseId); // ✅ Thêm courseId mới vào danh sách

                                // ✅ Cập nhật Firestore
                                firestore.collection("students").document(uid)
                                        .update("courses", courses)
                                        .addOnSuccessListener(aVoid -> Log.d("AuthRepository", "Course added successfully"))
                                        .addOnFailureListener(Throwable::printStackTrace);
                            } else {
                                Log.d("AuthRepository", "Course already exists in student's list");
                            }
                        }
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }


    public void signOut() {
        firebaseAuth.signOut();
        googleSignInClient.signOut();
    }
}