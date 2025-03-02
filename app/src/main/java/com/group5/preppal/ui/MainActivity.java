package com.group5.preppal.ui;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.R;
import com.group5.preppal.data.model.User;
import com.group5.preppal.viewmodel.AuthViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private TextView userInfoTextView;
    private Button signOutButton;

    @Inject
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        userInfoTextView = findViewById(R.id.userInfoTextView);
        signOutButton = findViewById(R.id.signOutButton);

        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Log.d("MainActivity", "User logged in: " + firebaseUser.getEmail());
                fetchUserFromFirestore(firebaseUser);
            } else {
                Log.e("MainActivity", "FirebaseUser is NULL, redirecting to login...");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        signOutButton.setOnClickListener(v -> signOut());
    }

    private void fetchUserFromFirestore(FirebaseUser firebaseUser) {
        Log.d("Firestore", "Fetching user data for: " + firebaseUser.getUid());

        DocumentReference userRef = firestore.collection("users").document(firebaseUser.getUid());

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    Log.d("Firestore", "User data retrieved: " + user.getEmail());
                    String userInfo = "UID: " + user.getUid() + "\n" +
                            "Email: " + user.getEmail() + "\n" +
                            "Name: " + user.getName() + "\n" +
                            "DOB: " + user.getDateOfBirth() + "\n" +
                            "Gender: " + user.getGender();
                    userInfoTextView.setText(userInfo);
                } else {
                    Log.e("Firestore", "User object is null!");
                }
            } else {
                Log.e("Firestore", "No document found for user: " + firebaseUser.getUid());
            }
        }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching user from Firestore", e));
    }

    private void signOut() {
        authViewModel.signOut();
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}