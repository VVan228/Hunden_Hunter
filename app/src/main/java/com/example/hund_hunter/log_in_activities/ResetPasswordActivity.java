package com.example.hund_hunter.log_in_activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.example.hund_hunter.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);

        inputEmail = (EditText) findViewById(R.id.email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        Button btnReset = (Button) findViewById(R.id.btn_reset_password);
        Button btnBack = (Button) findViewById(R.id.btn_back);


        btnBack.setOnClickListener(v -> finish());
        btnReset.setOnClickListener(v -> {

            String email = inputEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Введите email!", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Инструкции для восстановления высланы! Проверьте почту.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Введена некорректная почта!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            });
        });
    }

}
