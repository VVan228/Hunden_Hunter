package com.example.hund_hunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);//TODO: сделать чтьобы съехжало при появлении клавиатуры
        //TODO: принять extra2
    }
    public void findButton(View view){
        Intent Ord_act = new Intent(RegistrationActivity.this, OrderCreationActivity.class);
        startActivity(Ord_act);
    }

}