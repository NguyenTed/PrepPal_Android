package com.group5.preppal.ui.auth;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.group5.preppal.BuildConfig;
import com.group5.preppal.R;
import com.group5.preppal.data.model.User;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.ui.admin.AdminMainActivity;
import com.group5.preppal.ui.TeacherMainActivity;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;
import android.util.Log;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.group5.preppal.viewmodel.UserViewModel;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button emailSignInButton, googleSignInButton;
    private TextView signUpTextView;
    private UserViewModel userViewModel;
    private AuthViewModel authViewModel;
    @Inject
    AuthRepository authRepository;
    @Inject
    SignInClient signInClient; // ✅ Inject SignInClient for Google Identity Services

    // ✅ Google Sign-In Activity Result Launcher (Uses ActivityResult API)
    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    try {
                        // ✅ Try handling it as a Google One-Tap sign-in result
                        SignInCredential credential = signInClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();

                        if (idToken != null) {
                            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                            authViewModel.signInWithGoogle(firebaseCredential); // ✅ Authenticate with Firebase
                            return;
                        }
                    } catch (ApiException e) {
                        Log.e("LoginActivity", "One-Tap Sign-In failed: " + e.getStatusCode(), e);
                    }

                    try {
                        // ✅ Try handling it as a Google Account Picker result
                        GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                                .getResult(ApiException.class);

                        if (account != null) {
                            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                            authViewModel.signInWithGoogle(firebaseCredential); // ✅ Authenticate with Firebase
                        }
                    } catch (ApiException e) {
                        Log.e("LoginActivity", "Google Sign-In failed: " + e.getStatusCode(), e);
                        Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Google sign-in was cancelled", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailSignInButton = findViewById(R.id.loginButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        signUpTextView = findViewById(R.id.signUpTextView);

        emailSignInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            authViewModel.signInWithEmail(email, password);
        });

        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
        signUpTextView.setOnClickListener(v -> goToSignUpActivity());

        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Log.d("LoginActivity", "Firebase login success. UID: " + firebaseUser.getUid());

                // 👉 Gọi tách riêng ra để dễ debug
                LiveData<User> userData = userViewModel.getCurrentUser();

                userData.observe(this, user -> {
                    Log.d("LoginActivity", "User value: " + user);
                    if (user != null) {
                        String userRole = user.getRole();
                        Log.d("LoginActivity", "Role: " + user.getRole());
                        if (userRole.equals("student")) {
                            goToMainActivity(firebaseUser);
                        } else if (userRole.equals("teacher")) {
                            goToTeacherMainActivity();
                        } else if (userRole.equals("admin")) {
                            goToAdminMainActivity();
                            // Somewhere in your login logic
                            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
                            prefs.edit()
                                    .putString("admin_email", emailEditText.getText().toString())
                                    .putString("admin_password", passwordEditText.getText().toString())
                                    .apply();

                        }
                    } else {
                        Log.e("LoginActivity", "User info not found in Firestore");
                        Toast.makeText(this, "Tài khoản chưa được khởi tạo trong hệ thống.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        authViewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Uses IntentSenderRequest instead of deprecated startIntentSenderForResult()
    private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID) // ✅ Your Web Client ID
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        // ✅ Force open Google Account Picker
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void goToMainActivity(FirebaseUser user) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToTeacherMainActivity() {
        Intent intent = new Intent(this, TeacherMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToAdminMainActivity() {
        Intent intent = new Intent(this, AdminMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSignUpActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}

