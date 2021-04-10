package com.example.hund_hunter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class OrderCreationActivity extends AppCompatActivity {
    private EditText revard;
    int DIALOG_TIME = 1;
    int myHour = 14;
    int myMinute = 35;
    TextView tvTime;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_creation);
        tvTime = (TextView) findViewById(R.id.tvTime);

        revard = (EditText) findViewById(R.id.reward);


    }

    public void onclick(View view) {
        showDialog(DIALOG_TIME);


    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, myHour, myMinute, true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;
            String minNull = "";
            String hourNull = "";
            if(myHour <= 10 ){
                hourNull = "0";
            }

            if(myMinute <= 10 ){
                minNull = "0";
            }
            tvTime.setText(hourNull + myHour + " : " + minNull + myMinute );
        }
    };


}