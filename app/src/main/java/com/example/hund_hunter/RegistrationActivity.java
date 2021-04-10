package com.example.hund_hunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import android.text.format.DateFormat;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);//TODO: сделать чтьобы съехжало при появлении клавиатуры


    }

    public void findButton(View view){
        Intent reg_act = new Intent(RegistrationActivity.this, SeekerActivity.class);
        startActivity(reg_act);
    }
}