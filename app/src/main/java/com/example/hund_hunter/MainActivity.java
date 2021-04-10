package com.example.hund_hunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = MainActivity.this;
        Class destinationActivity = StartActivity.class;
        Intent child_act_intent = new Intent(context, destinationActivity);
        child_act_intent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(child_act_intent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("rdashk","its Work too!!!!");
        Log.d("kjuby","its Work!!!!");

    }
}