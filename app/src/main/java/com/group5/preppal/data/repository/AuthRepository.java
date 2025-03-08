package com.group5.preppal.data.repository;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.User;

import java.util.Date;

@Singleton
public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;
    private final GoogleSignInClient googleSignInClient;
    private final SignInClient signInClient;

    @Inject
    public AuthRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firestore, GoogleSignInClient googleSignInClient, SignInClient signInClient) {
        this.firebaseAuth = firebaseAuth;
        this.firestore = firestore;
        this.googleSignInClient = googleSignInClient;
        this.signInClient = signInClient;
    }

    /** ✅ Sign in with Email & Password */
    public Task<FirebaseUser> signInWithEmail(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d("AuthRepository", "Email sign-in successful: " + email);
                        return firebaseAuth.getCurrentUser();
                    } else {
                        throw task.getException();
                    }
                });
    }

    /** ✅ Sign up with Email & Password and save user in Firestore */
    public Task<FirebaseUser> signUpWithEmail(String email, String password, String name, Date dateOfBirth, User.Gender gender) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d("AuthRepository", "New user created: " + email);
                        checkAndSaveNativeUser(firebaseAuth.getUid(), email, name, dateOfBirth, gender);
                        return firebaseAuth.getCurrentUser();
                    } else {
                        throw task.getException();
                    }
                });
    }

    public Task<AuthResult> signInWithGoogle(AuthCredential credential) {
        return firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        checkAndSaveFirebaseUser(user); // ✅ Save user data to Firestore
                    }
                })
                .addOnFailureListener(e -> Log.e("AuthRepository", "Google sign-in failed", e));
    }


    /** ✅ Sign in with Google using new SignInClient */
    public Task<AuthResult> firebaseAuthWithGoogle(Intent data) {
        try {
            // ✅ Retrieve Google Sign-In credentials from the intent
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();

            if (idToken != null) {
                AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);

                // ✅ Sign in with Firebase using the Google credential
                return firebaseAuth.signInWithCredential(authCredential)
                        .addOnSuccessListener(authResult -> {
                            FirebaseUser user = authResult.getUser();
                            if (user != null) {
                                checkAndSaveFirebaseUser(user); // ✅ Save user info in Firestore
                            }
                        });
            } else {
                throw new ApiException(Status.RESULT_CANCELED);
            }
        } catch (ApiException e) {
            return Tasks.forException(e);
        }
    }

    /** ✅ Check if user exists in Firestore, save if new (Google Sign-In) */
    private void checkAndSaveFirebaseUser(FirebaseUser firebaseUser) {
        if (firebaseUser == null) {
            Log.e("Firestore", "FirebaseUser is null, cannot save.");
            return;
        }

        // ✅ Retrieve Firestore user document asynchronously
        firestore.collection("students").document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Log.d("Firestore", "User does not exist, saving new user...");

                        Student student = new Student(
                                firebaseUser.getUid(),
                                firebaseUser.getEmail(),
                                firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Unknown",
                                null,
                                User.Gender.OTHER
                        );

                        firestore.collection("students").document(student.getUid())
                                .set(student)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User saved successfully: " + student.getUid()))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error saving user", e));
                    } else {
                        Log.d("Firestore", "User already exists in Firestore: " + firebaseUser.getUid());
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user document", e));
    }


    /** ✅ Check if user exists in Firestore, save if new (Email Sign-Up) */
    private void checkAndSaveNativeUser(String uid, String email, String name, Date dateOfBirth, User.Gender gender) {
        DocumentSnapshot documentSnapshot = firestore.collection("students").document(uid).get().getResult();
        if (documentSnapshot != null && !documentSnapshot.exists()) {
            Student student = new Student(
                    uid,
                    email,
                    name,
                    dateOfBirth,
                    gender
            );

            firestore.collection("students").document(student.getUid())
                    .set(student)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Native user saved: " + email))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error saving native user", e));
        }
    }

    /** ✅ Get currently signed-in user */
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    /** ✅ Sign out user */
    public void signOut() {
        firebaseAuth.signOut();
        googleSignInClient.signOut();
        Log.d("AuthRepository", "User signed out");
    }
}