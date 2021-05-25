package com.example.hund_hunter.fire_classes;

import androidx.annotation.NonNull;

import com.example.hund_hunter.fire_classes.interfaces.AuthStateListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireAuth {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    FirebaseUser user;

    public FireAuth(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public FireAuth(FirebaseAuth _auth){
        auth = _auth;
        user = auth.getCurrentUser();
    }

    public void setAuthListener(AuthStateListener _authListener){
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                _authListener.onAuthStateChanged(new FireAuth(firebaseAuth));
            }
        };
        auth.addAuthStateListener(authListener);
    }

    public void removeAuthListener(){
        auth.removeAuthStateListener(authListener);
    }

    public FirebaseUser getCurrentUser(){
        return user;
    }

    public Task<AuthResult> signIn(String email, String pass){
        return auth.signInWithEmailAndPassword(email, pass);
    }

    public Task<Void> sendPassword(String email){
        return auth.sendPasswordResetEmail(email);
    }

    public Task<AuthResult> createUser(String email, String pass){
        return auth.createUserWithEmailAndPassword(email, pass);
    }

    public Task<Void> updateEmail(String email){
        return user.updateEmail(email);
    }

    public Task<Void> updatePassword(String pass){
        return user.updatePassword(pass);
    }

    public Task<Void> deleteCurrentUser(){
        return user.delete();
    }

    public void signOut(){
        auth.signOut();
    }
}
