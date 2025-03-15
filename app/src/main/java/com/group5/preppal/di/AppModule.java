package com.group5.preppal.di;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    public GoogleSignInOptions provideGoogleSignInOptions(Application application) {
        String clientId = getGoogleClientId();
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build();
    }

    private String getGoogleClientId() {
        return BuildConfig.GOOGLE_CLIENT_ID;
    }

    @Provides
    @Singleton
    public GoogleSignInClient provideGoogleSignInClient(Application application, GoogleSignInOptions gso) {
        return GoogleSignIn.getClient(application, gso);
    }

    // ✅ Provide SignInClient for Identity Services API
    @Provides
    @Singleton
    public SignInClient provideSignInClient(Application application) {
        return Identity.getSignInClient(application);
    }
}
