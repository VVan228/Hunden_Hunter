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

    private Button ChangeEmail, ChangePassword, RemoveUser, SignOut,
            changeEmail, changePassword, sendEmail, remove;

    private EditText oldEmail, newEmail, oldPassword, newPassword;
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
        ChangeEmail = (Button) findViewById(R.id.acc_bth_change_email);
        ChangePassword = (Button) findViewById(R.id.acc_bth_change_password);
        RemoveUser = (Button) findViewById(R.id.acc_bth_remove_user);
        SignOut = (Button) findViewById(R.id.acc_bth_sign_out);
        // buttons for agree
        changeEmail = (Button) findViewById(R.id.acc_ChangeEmail);
        changePassword = (Button) findViewById(R.id.acc_ChangePass);
        sendEmail = (Button) findViewById(R.id.acc_Send);
        remove = (Button) findViewById(R.id.acc_Remove);

        oldEmail = (EditText) findViewById(R.id.acc_old_email);
        newEmail = (EditText) findViewById(R.id.acc_new_email);
        oldPassword = (EditText) findViewById(R.id.acc_old_password);
        newPassword = (EditText) findViewById(R.id.acc_new_password);

        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        oldPassword.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        ChangeEmail.setOnClickListener(v -> {
            oldEmail.setVisibility(View.GONE);
            newEmail.setVisibility(View.VISIBLE);
            oldPassword.setVisibility(View.GONE);
            newPassword.setVisibility(View.GONE);
            changeEmail.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.GONE);
            sendEmail.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
        });

        changeEmail.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null && !str(newEmail).equals("")) {
                user.updateEmail(str(newEmail))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserAccountActivity.this, "Email изменен!", Toast.LENGTH_LONG).show();
                                signOut();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(UserAccountActivity.this, "Некорректный email!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            } else if (str(newEmail).equals("")) {
                newEmail.setError("Введите email!");
                progressBar.setVisibility(View.GONE);
            }
        });

        ChangePassword.setOnClickListener(v -> {
            oldEmail.setVisibility(View.GONE);
            newEmail.setVisibility(View.GONE);
            oldPassword.setVisibility(View.GONE);
            newPassword.setVisibility(View.VISIBLE);
            changeEmail.setVisibility(View.GONE);
            changePassword.setVisibility(View.VISIBLE);
            sendEmail.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
        });

        changePassword.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null && !str(newPassword).equals("")) {
                if (str(newPassword).length() < 6) {
                    newPassword.setError("Слишком короткий пароль!");
                    progressBar.setVisibility(View.GONE);
                } else {
                    user.updatePassword(str(newPassword))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserAccountActivity.this, "Пароль изменен!", Toast.LENGTH_SHORT).show();
                                    signOut();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(UserAccountActivity.this, "Некорректный пароль!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }
            } else if (str(newPassword).equals("")) {
                newPassword.setError("Введите пароль");
                progressBar.setVisibility(View.GONE);
            }
        });

        sendEmail.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (!str(oldEmail).equals("")) {
                auth.sendPasswordResetEmail(str(oldEmail))
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

        RemoveUser.setOnClickListener(v -> {
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

        SignOut.setOnClickListener(v -> signOut());
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

    String str(EditText t){
        return t.getText().toString().trim();
    }
}