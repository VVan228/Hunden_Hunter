package com.example.hund_hunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent start_act = new Intent(MainActivity.this, StartActivity.class);
        //child_act_intent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(start_act);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("kjuby","its Work!!!!");
        Log.d("rdashk","its Work too!!!!");
    }
}