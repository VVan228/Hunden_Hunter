package com.example.hund_hunter.log_in_activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.hund_hunter.main_activities.OrderCreationActivity;
import com.example.hund_hunter.main_activities.SeekerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.hund_hunter.R;

public class FirstActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnLogin, btnSignup, btnReset;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_EMAIL = "email";
    public static final String APP_PREFERENCES_PASS = "******";

    SharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mySharedPreferences.contains(FirstActivity.APP_PREFERENCES_EMAIL)){
            Toast.makeText(FirstActivity.this, "logged as "+mySharedPreferences.getString(FirstActivity.APP_PREFERENCES_EMAIL,""), Toast.LENGTH_LONG).show();
        }

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent reg_act = new Intent(FirstActivity.this, ChoiceActivity.class);
            startActivity(reg_act);
            finish();
        }

        inputEmail = (EditText) findViewById(R.id.first_email);
        inputPassword = (EditText) findViewById(R.id.first_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.first_btn_signup);
        btnLogin = (Button) findViewById(R.id.first_btn_login);
        btnReset = (Button) findViewById(R.id.first_btn_reset_password);

        btnSignup.setOnClickListener(v -> startActivity(new Intent(FirstActivity.this, SignupActivity.class)));

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
                            Toast.makeText(FirstActivity.this, "Проверьте логин и пароль!", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(FirstActivity.this, ChoiceActivity.class);
                            startActivity(intent);
                            finish();
                            }
                        });
        });
    }
}

