package com.example.hund_hunter.log_in_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hund_hunter.main_activities.ListOfMyItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.hund_hunter.R;

public class UserAccountActivity extends AppCompatActivity {

    private Button changeEmail;
    private Button changePassword;
    private Button sendEmail;
    private Button remove;

    private EditText oldEmail, newEmail, oldPassword, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account_activity);

        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = firebaseAuth -> {
            FirebaseUser user1 = firebaseAuth.getCurrentUser();
            if (user1 == null) {
                startActivity(new Intent(UserAccountActivity.this, LoginActivity.class));
                finish();
            }
        };

        // buttons for changing fields
        Button changeEmail1 = (Button) findViewById(R.id.acc_bth_change_email);
        Button changePassword1 = (Button) findViewById(R.id.acc_bth_change_password);
        Button removeUser = (Button) findViewById(R.id.acc_bth_remove_user);
        Button signOut = (Button) findViewById(R.id.acc_bth_sign_out);
        Button back = (Button) findViewById(R.id.bth_back_from_user_acc);

        // buttons for next actions
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

        changeEmail1.setOnClickListener(v -> {
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
                user.updateEmail(str(newEmail)).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserAccountActivity.this, "Email ??????????????!", Toast.LENGTH_LONG).show();
                                signOut();
                            } else {
                                Toast.makeText(UserAccountActivity.this, "???????????????????????? email!", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        });
            } else if (str(newEmail).equals("")) {
                newEmail.setError("?????????????? email!");
                progressBar.setVisibility(View.GONE);
            }
        });

        changePassword1.setOnClickListener(v -> {
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
                    newPassword.setError("?????????????? ???????????????? ????????????!");
                    progressBar.setVisibility(View.GONE);
                } else {
                    user.updatePassword(str(newPassword))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserAccountActivity.this, "???????????? ??????????????!", Toast.LENGTH_SHORT).show();
                                    signOut();
                                } else {
                                    Toast.makeText(UserAccountActivity.this, "???????????????????????? ????????????!", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            });
                }
            } else if (str(newPassword).equals("")) {
                newPassword.setError("?????????????? ????????????");
                progressBar.setVisibility(View.GONE);
            }
        });

        sendEmail.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (!str(oldEmail).equals("")) {
                auth.sendPasswordResetEmail(str(oldEmail))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserAccountActivity.this, "???????????? ?? ?????????????????????????????? ???????????? ??????????????!", Toast.LENGTH_SHORT).show();
                                signOut();
                            } else {
                                Toast.makeText(UserAccountActivity.this, "?????????????????? email!", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        });
            } else {
                oldEmail.setError("?????????????? email");
                progressBar.setVisibility(View.GONE);
            }
        });

        removeUser.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null) {
                user.delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserAccountActivity.this, "?????????????? ????????????... ???????????????? ??????????!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserAccountActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(UserAccountActivity.this, "???????????????????? ?????????????? ??????????????!", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
            }
        });

        signOut.setOnClickListener(v -> signOut());

        back.setOnClickListener(v -> finish());
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