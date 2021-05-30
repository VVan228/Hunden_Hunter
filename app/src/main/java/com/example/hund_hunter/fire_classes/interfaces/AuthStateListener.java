package com.example.hund_hunter.fire_classes.interfaces;

import androidx.annotation.NonNull;

import com.example.hund_hunter.fire_classes.FireAuth;
import com.google.firebase.auth.FirebaseAuth;

public interface AuthStateListener {
    public void onAuthStateChanged(@NonNull FireAuth firebaseAuth);
}
