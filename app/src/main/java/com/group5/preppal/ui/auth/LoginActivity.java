package com.group5.preppal.ui.auth;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button emailSignInButton, googleSignInButton;
    private TextView signUpTextView;

    private AuthViewModel authViewModel;

    @Inject
    GoogleSignInClient googleSignInClient;

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        authViewModel.signInWithGoogle(account);
                    } catch (ApiException e) {
                        Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class); // ✅ Fixed ViewModel

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

        authViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                goToMainActivity(user);
            }
        });

        authViewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void goToMainActivity(FirebaseUser user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSignUpActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
