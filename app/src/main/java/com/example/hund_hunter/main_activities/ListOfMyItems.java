package com.example.hund_hunter.main_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.hund_hunter.R;
import com.example.hund_hunter.log_in_activities.UserAccountActivity;

// класс для списка своих объявлений
public class ListOfMyItems extends Activity {

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_items);

        back = (Button) findViewById(R.id.bth_back_from_list);
        back.setOnClickListener(v -> startActivity(new Intent(ListOfMyItems.this, UserAccountActivity.class)));
    }
}





