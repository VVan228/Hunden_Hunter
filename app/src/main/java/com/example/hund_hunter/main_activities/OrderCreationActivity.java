package com.example.hund_hunter.main_activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.hund_hunter.R;
import com.example.hund_hunter.data_classes.Order;
import com.example.hund_hunter.fire_classes.FireDB;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OrderCreationActivity extends AppCompatActivity {
    FireDB db;
    private EditText revard;
    int DIALOG_TIME = 1;
    int myHour = 14;
    int myMinute = 35;
    TextView tvTime;
    String coords;
    String time;
    AppCompatButton add_photo;
    static final int GALLERY_REQUEST = 1;
    ImageView photo;
    Bitmap bitmap = null;
    String image = "";

    DatabaseReference ref;
    DatabaseReference usersRef;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_creation);

        db = new FireDB(new String[]{"orders"});
        tvTime = (TextView) findViewById(R.id.tvTime);
        revard = (EditText) findViewById(R.id.reward);
        add_photo = (AppCompatButton) findViewById(R.id.bth_add_photo);
        photo = (ImageView) findViewById(R.id.iv_pet_photo);


        add_photo.setOnClickListener(v -> {
            // отображение галереи всех изображений, хранящихся на телефоне, позволяя выбрать одно
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });
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

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Log.d("tag4me", "ye ");
                    image = bitmapToString(bitmap);
                    photo.setImageBitmap(bitmap);
                }
        }

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
        if(coords==null||time==null||commentTxt.equals("")||pricetTxt.equals("")){
            Toast.makeText(OrderCreationActivity.this, "заполните пустые поля", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("tag4me", "debug str");
        db.pushValue(new Order(email, pricetTxt, commentTxt, coords, time, image));


        Intent reg_act = new Intent(OrderCreationActivity.this, SeekerActivity.class);
        startActivity(reg_act);
    }

    String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

}