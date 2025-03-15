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
import com.google.firebase.firestore.DocumentReference;
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
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("AuthRepository", "New user created: " + user.getEmail());
                            return checkAndSaveNativeUser(user, name, dateOfBirth, gender).continueWith(t -> user);
                        }
                    }
                    throw task.getException();
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
    private Task<Void> checkAndSaveNativeUser(FirebaseUser user, String name, Date dateOfBirth, User.Gender gender) {
        if (user == null) {
            return Tasks.forException(new Exception("FirebaseUser is null, cannot save profile."));
        }

        DocumentReference userRef = firestore.collection("students").document(user.getUid());

        return userRef.get().continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            if (!task.getResult().exists()) {
                Log.d("Firestore", "User does not exist, saving new user...");

                Student student = new Student(
                        user.getUid(),
                        user.getEmail(),
                        name,
                        dateOfBirth,
                        gender
                );

                return userRef.set(student)
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "User profile saved successfully: " + user.getUid()))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error saving user profile", e));
            } else {
                Log.d("Firestore", "User already exists: " + user.getUid());
                return Tasks.forResult(null);
            }
        });
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