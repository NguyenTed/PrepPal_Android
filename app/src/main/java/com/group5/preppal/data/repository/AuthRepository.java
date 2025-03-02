package com.group5.preppal.data.repository;
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
import com.group5.preppal.data.model.User;

import java.util.Date;

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
                        checkAndSaveUser(firebaseAuth.getCurrentUser()); // ✅ Save user if new
                        return firebaseAuth.getCurrentUser();
                    } else {
                        throw task.getException();
                    }
                });
    }


    public Task<FirebaseUser> signUpWithEmail(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        checkAndSaveUser(firebaseAuth.getCurrentUser()); // ✅ Save user if new
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
                        checkAndSaveUser(firebaseAuth.getCurrentUser()); // ✅ Save user if new
                        return credential;
                    } else {
                        throw task.getException();
                    }
                });
    }

    // Check if user exists in Firestore and save if new
    private void checkAndSaveUser(FirebaseUser firebaseUser) {
        if (firebaseUser == null) return;

        firestore.collection("users").document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        // User does not exist, save to Firestore
                        User user = new User(
                                firebaseUser.getUid(),
                                firebaseUser.getEmail(),
                                firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Unknown",
                                new Date(),
                                User.Gender.MALE,
                                "student"
                                );

                        firestore.collection("users").document(user.getUid())
                                .set(user)
                                .addOnSuccessListener(aVoid -> {})
                                .addOnFailureListener(Throwable::printStackTrace);
                    }
                });
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signOut() {
        firebaseAuth.signOut();
        googleSignInClient.signOut();
    }
}