package com.group5.preppal.ui.profile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.R;
import com.group5.preppal.data.model.User;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.ui.auth.LoginActivity;
import com.group5.preppal.viewmodel.AuthViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private ImageView profileImageView;
    private TextView nameTextView, emailTextView, logOutTextView;
    @Inject
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        profileImageView = findViewById(R.id.profileImageView);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        logOutTextView = findViewById(R.id.logOutTextView);

        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Log.d("MainActivity", "User logged in: " + firebaseUser.getEmail());
                fetchUserFromFirestore(firebaseUser);
            } else {
                Log.e("MainActivity", "FirebaseUser is NULL, redirecting to login...");
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        logOutTextView.setOnClickListener(v -> signOut());

        // Ánh xạ View
        setupClickListener(R.id.accountInformationRoute, AccountInformationActivity.class);
        setupClickListener(R.id.courseRoute, CourseInfoActivity.class);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }

            return false;
        });

    }

    private void setupClickListener(int layoutId, Class<?> destination) {
        LinearLayout layout = findViewById(layoutId);
        layout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, destination);

            // Tạo hiệu ứng mở rộng từ nhỏ ra lớn
            ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                    v,  // View bắt đầu hiệu ứng
                    v.getWidth() / 2, v.getHeight() / 2, // Tâm mở rộng
                    0, 0 // Kích thước ban đầu
            );
            startActivity(intent, options.toBundle());
        });
    }

    private void fetchUserFromFirestore(FirebaseUser firebaseUser) {
        Log.d("Firestore", "Fetching user data for: " + firebaseUser.getUid());

        DocumentReference userRef = firestore.collection("users").document(firebaseUser.getUid());

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    Log.d("Firestore", "User data retrieved: " + user.getEmail());
                    if (firebaseUser.getPhotoUrl() != null)
                        Glide.with(getApplicationContext()).load(firebaseUser.getPhotoUrl()).centerCrop().into(profileImageView);
//                    nameTextView.setText(user.getName());
                    emailTextView.setText(user.getEmail());
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
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }
}
