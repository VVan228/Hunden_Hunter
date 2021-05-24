package com.example.hund_hunter.log_in_activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hund_hunter.main_activities.OrderCreationActivity;
import com.example.hund_hunter.R;
import com.example.hund_hunter.main_activities.SeekerActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void findButtonOnClick(View view){
        Intent reg_act = new Intent(StartActivity.this, SeekerActivity.class);
        startActivity(reg_act);
    }

    public void lostButtonClick(View view){
        Intent reg_act = new Intent(StartActivity.this, OrderCreationActivity.class);
        startActivity(reg_act);
    }
}