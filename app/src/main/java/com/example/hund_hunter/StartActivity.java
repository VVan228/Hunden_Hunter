package com.example.hund_hunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_EMAIL = "email";


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
        //if (FirebaseAuth.getInstance().getCurrentUser() == null) {
        SharedPreferences mySharedPreferences = getSharedPreferences(StartActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mySharedPreferences.contains(StartActivity.APP_PREFERENCES_EMAIL)){
            Toast.makeText(StartActivity.this, "logged as "+mySharedPreferences.getString(StartActivity.APP_PREFERENCES_EMAIL,""), Toast.LENGTH_LONG).show();
            if(extra.equals("find")){
                Intent reg_act = new Intent(StartActivity.this, SeekerActivity.class);
                reg_act.putExtra(extra, true);
                startActivity(reg_act);
            }else if(extra.equals("lost")){
                Intent reg_act = new Intent(StartActivity.this, OrderCreationActivity.class);
                reg_act.putExtra(extra, true);
                startActivity(reg_act);
            }
        }else{
            Intent reg_act = new Intent(StartActivity.this, RegistrationActivity.class);
            reg_act.putExtra(extra, true);
            startActivity(reg_act);
        }


        //}
        /*else {
            if (extra == "lost") {
                Snackbar.make(findViewById(R.id.ll_lostForm), "Вы авторизованы", Snackbar.LENGTH_SHORT).show();
                Intent reg_act = new Intent(StartActivity.this, OrderCreationActivity.class);
                reg_act.putExtra(extra, true);
                startActivity(reg_act);
            }
            else {
                //Snackbar.make(findViewById(R.id.act_with_map), "Вы авторизованы", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(StartActivity.this, "Карта", Toast.LENGTH_LONG).show();
            }
        }*/
    }


}