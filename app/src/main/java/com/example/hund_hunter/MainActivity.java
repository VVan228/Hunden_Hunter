package com.example.hund_hunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements ValueEventListener {

    private static int SIGN_IN_CODE = 1;
    private FloatingActionButton sendButton;
    private RelativeLayout activity_main;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // знаем, что получили авторизацию пользователя
        if (requestCode == SIGN_IN_CODE) {
            // успешная авторизация
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_main, "Вы авторизованы", Snackbar.LENGTH_SHORT).show();
                Intent next_act = new Intent(MainActivity.this, StartActivity.class);
                startActivity(next_act);
            }
            else {
                Snackbar.make(activity_main, "Ошибка. Вы не авторизованы", Snackbar.LENGTH_LONG).show();
                Intent next_act = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(next_act);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = findViewById(R.id.rl_activity_main);

        // пользователь еще не авторизован
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // вызов регистрационной формы
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        }
        else {
            Snackbar.make(activity_main, "Вы авторизованы", Snackbar.LENGTH_SHORT).show();
            Intent next_act = new Intent(MainActivity.this, StartActivity.class);
            startActivity(next_act);
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        // в каком виде хранятся данные
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
