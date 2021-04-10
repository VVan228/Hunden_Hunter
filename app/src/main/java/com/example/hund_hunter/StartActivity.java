package com.example.hund_hunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        Toast.makeText(this, "Зачем вы нажали?", Toast.LENGTH_SHORT).show();
        Intent reg_act = new Intent(StartActivity.this, RegistrationActivity.class);
        reg_act.putExtra(extra, true);
        startActivity(reg_act);

    }


}