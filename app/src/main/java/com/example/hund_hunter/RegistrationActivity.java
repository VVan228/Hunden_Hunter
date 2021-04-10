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

    private EditText name, familia, email, password;
    boolean find, lost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);//TODO: сделать чтьобы съехжало при появлении клавиатуры
        name = findViewById(R.id.editTextTextPersonName);
        familia = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.editTextTextEmailAddress2);
        password = findViewById(R.id.editTextTextPassword);

        Bundle extras = getIntent().getExtras();
        find = extras.getBoolean("find");
        lost = extras.getBoolean("lost");
    }

    public void findButton(View view){
        Intent reg_act = null;
        if(find){
            reg_act = new Intent(RegistrationActivity.this, SeekerActivity.class);
        }else if(lost){
            reg_act = new Intent(RegistrationActivity.this, OrderCreationActivity.class);
        }
        startActivity(reg_act);
    }
}