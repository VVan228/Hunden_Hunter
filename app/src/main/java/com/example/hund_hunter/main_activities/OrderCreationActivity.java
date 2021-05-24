package com.example.hund_hunter.main_activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hund_hunter.R;
import com.example.hund_hunter.data_classes.Order;
import com.example.hund_hunter.fire_classes.FireDB;
import com.example.hund_hunter.fire_classes.myQuery;
import com.example.hund_hunter.log_in_activities.StartActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class OrderCreationActivity extends AppCompatActivity {
    FireDB db;
    private EditText revard;
    int DIALOG_TIME = 1;
    int myHour = 14;
    int myMinute = 35;
    TextView tvTime;
    String coords;
    String time;

    DatabaseReference ref;
    DatabaseReference usersRef;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_creation);

        db = new FireDB(new String[]{"orders"});
        tvTime = (TextView) findViewById(R.id.tvTime);
        revard = (EditText) findViewById(R.id.reward);

        //пример запроса данных, игнорируйте
        db.getData(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("FireDB", snapshot.getValue(Order.class).getEmail());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }, new myQuery(db.getRef()).orderBy("email").equalTo("vvang"));
    }
    public void setLocation(View view){
        Intent set_act = new Intent(OrderCreationActivity.this, SetLocationActivity.class);
        startActivityForResult(set_act, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        coords = data.getStringExtra("coords");


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
            tvTime.setText(hourNull + myHour + " : " + minNull + myMinute);
            time = hourNull + myHour + " : " + minNull + myMinute;
        }
    };

    public void submit(View view){
        //берем почту из аккаунта
        EditText comment = findViewById(R.id.comment);
        EditText price = findViewById(R.id.reward);
        String email = "insert email";
        String commentTxt = comment.getText().toString();
        String pricetTxt = price.getText().toString();
        if(coords.equals("")||time.equals("")||commentTxt.equals("")||pricetTxt.equals("")){
            return;
        }
        db.pushValue(new Order(email, pricetTxt, commentTxt, coords, time));


        Intent reg_act = new Intent(OrderCreationActivity.this, SeekerActivity.class);
        startActivity(reg_act);
    }


}