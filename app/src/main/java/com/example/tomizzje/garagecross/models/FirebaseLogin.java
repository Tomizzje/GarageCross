package com.example.tomizzje.garagecross.models;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class FirebaseLogin {

    private static final int RC_SIGN_IN =123 ;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    public FirebaseLogin() {
        firebaseAuth = FirebaseAuth.getInstance();
    }


    /**
     * check if the user is logged in. If not, start intent to login
     * @param caller activity calling the method
     */
    public void checkLogin(final Activity caller) {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    signIn(caller);
                }
            }
        };
    }

    /**
     * sign in builder
     * @param caller activity
     */
    private void signIn(Activity caller) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }


    public void attachListener() {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void detachListener() {
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    public String getCurrentUser() {
        if(isSignedIn()){
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return null;
    }

    public String getName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public boolean isSignedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

}
