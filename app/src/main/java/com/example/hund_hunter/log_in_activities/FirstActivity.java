package com.example.hund_hunter.log_in_activities;

import android.content.Intent;
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

public class FirstActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent reg_act = new Intent(FirstActivity.this, ChoiceActivity.class);
            startActivity(reg_act);
        }

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        btnSignup.setOnClickListener(v -> {
                    Intent intent = new Intent(FirstActivity.this, SignupActivity.class);
                    startActivity(intent);
                });

        btnReset.setOnClickListener(v -> startActivity(new Intent(FirstActivity.this, ResetPasswordActivity.class)));

        btnLogin.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Введите email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Введите пароль!", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(FirstActivity.this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                inputPassword.setError("Пароль слишком короткий! Минимум 6 знаков!");
                            } else {
                                Toast.makeText(FirstActivity.this, "Проверьте логин!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(FirstActivity.this, ChoiceActivity.class);
                            startActivity(intent);
                        }
                    });
        });
    }
}

