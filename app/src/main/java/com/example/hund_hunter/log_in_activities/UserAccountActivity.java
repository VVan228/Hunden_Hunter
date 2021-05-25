package com.example.hund_hunter.log_in_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hund_hunter.main_activities.SeekerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.hund_hunter.R;

public class UserAccountActivity extends AppCompatActivity {

    private Button btnChangeEmail, btnChangePassword, btnRemoveUser,
            changeEmail, changePassword, sendEmail, remove, signOut;

    private EditText oldEmail, newEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account);

        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user1 = firebaseAuth.getCurrentUser();
                if (user1 == null) {
                    startActivity(new Intent(UserAccountActivity.this, FirstActivity.class));
                    finish();
                }
            }
        };

        // buttons for changing fields
        btnChangeEmail = (Button) findViewById(R.id.change_email_button);
        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);

        // buttons for agree
        changeEmail = (Button) findViewById(R.id.changeEmail);
        changePassword = (Button) findViewById(R.id.changePass);
        sendEmail = (Button) findViewById(R.id.send);
        remove = (Button) findViewById(R.id.remove);
        signOut = (Button) findViewById(R.id.sign_out);

        oldEmail = (EditText) findViewById(R.id.old_email);
        newEmail = (EditText) findViewById(R.id.new_email);
        password = (EditText) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.newPassword);

        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        btnChangeEmail.setOnClickListener(v -> {
            oldEmail.setVisibility(View.GONE);
            newEmail.setVisibility(View.VISIBLE);
            password.setVisibility(View.GONE);
            newPassword.setVisibility(View.GONE);
            changeEmail.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.GONE);
            sendEmail.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
        });

        changeEmail.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null && !newEmail.getText().toString().trim().equals("")) {
                user.updateEmail(newEmail.getText().toString().trim())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserAccountActivity.this, "Email изменен!", Toast.LENGTH_LONG).show();
                                signOut();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(UserAccountActivity.this, "Невозможно изменить email!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            } else if (newEmail.getText().toString().trim().equals("")) {
                newEmail.setError("Введите email!");
                progressBar.setVisibility(View.GONE);
            }
        });

        btnChangePassword.setOnClickListener(v -> {
            oldEmail.setVisibility(View.GONE);
            newEmail.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            newPassword.setVisibility(View.VISIBLE);
            changeEmail.setVisibility(View.GONE);
            changePassword.setVisibility(View.VISIBLE);
            sendEmail.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
        });

        changePassword.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null && !newPassword.getText().toString().trim().equals("")) {
                if (newPassword.getText().toString().trim().length() < 6) {
                    newPassword.setError("Слишком короткий пароль!");
                    progressBar.setVisibility(View.GONE);
                } else {
                    user.updatePassword(newPassword.getText().toString().trim())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserAccountActivity.this, "Пароль изменен!", Toast.LENGTH_SHORT).show();
                                    signOut();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(UserAccountActivity.this, "Невозможно изменить пароль!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }
            } else if (newPassword.getText().toString().trim().equals("")) {
                newPassword.setError("Введите пароль");
                progressBar.setVisibility(View.GONE);
            }
        });

        sendEmail.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (!oldEmail.getText().toString().trim().equals("")) {
                auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserAccountActivity.this, "Письмо с восстановлением пароля выслано!", Toast.LENGTH_SHORT).show();
                                signOut();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(UserAccountActivity.this, "Проверьте email!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            } else {
                oldEmail.setError("Введите email");
                progressBar.setVisibility(View.GONE);
            }
        });

        btnRemoveUser.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null) {
                user.delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserAccountActivity.this, "Профиль удален... Создайте новый!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserAccountActivity.this, FirstActivity.class));
                        finish();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(UserAccountActivity.this, "Невозможно удалить аккаунт!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        signOut.setOnClickListener(v -> signOut());
    }

    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener); }
    }
}