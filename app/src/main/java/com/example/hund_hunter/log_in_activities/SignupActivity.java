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

import com.example.hund_hunter.data_classes.User;
import com.example.hund_hunter.fire_classes.FireDB;
import com.google.firebase.auth.FirebaseAuth;
import com.example.hund_hunter.R;

public class SignupActivity extends AppCompatActivity {

    private EditText surname, name, tel, inputEmail, inputPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FireDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        auth = FirebaseAuth.getInstance();
        db = new FireDB(new String[]{"users"});

        surname = (EditText) findViewById(R.id.sign_surname);
        name = (EditText) findViewById(R.id.sign_name);
        tel = (EditText) findViewById(R.id.sign_tel);
        inputEmail = (EditText) findViewById(R.id.sign_email);
        inputPassword = (EditText) findViewById(R.id.sign_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button btnOk = (Button) findViewById(R.id.sign_ok);
        Button btnBack = (Button) findViewById(R.id.sign_back);

        btnBack.setOnClickListener(v -> finish());
        btnOk.setOnClickListener(v -> {

            String email = str(inputEmail);
            String password = str(inputPassword);

            if (TextUtils.isEmpty(str(name)) || TextUtils.isEmpty(str(surname)) || TextUtils.isEmpty(str(tel))) {
                Toast.makeText(getApplicationContext(), "Заполните пустые поля!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Введите email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Введите пароль!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Пароль слишком короткий! Минимум 6 знаков!", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Ошибка. Пользователь не зарегистрирован!" + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            db.pushValue(new User(str(name), str(surname), email, str(tel)));
                            Toast.makeText(SignupActivity.this, "Регистрация прошла успешно.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, ChoiceActivity.class));
                        }
                    });
        });
    }

    String str(EditText t){
        return t.getText().toString().trim();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}