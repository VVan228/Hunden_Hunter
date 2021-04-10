package com.example.hund_hunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }

    public void findButtonOnClick(View view){
        regisr("find");
    }

    public void lostButtonClick(View view){
        regisr("lost");

    }

    public void regisr(String extra){
        // пользователь не авторизован
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent reg_act = new Intent(StartActivity.this, RegistrationActivity.class);
            reg_act.putExtra(extra, true);
            startActivity(reg_act);
        }
        else {
            /*if (extra == "lost") {
                Snackbar.make(findViewById(R.id.ll_lostForm), "Вы авторизованы", Snackbar.LENGTH_SHORT).show();
            }
            else {
                Snackbar.make(findViewById(R.id.act_with_map), "Вы авторизованы", Snackbar.LENGTH_SHORT).show();
            }*/
            Toast.makeText(this, "Карта", Toast.LENGTH_LONG).show();
        }
    }


}