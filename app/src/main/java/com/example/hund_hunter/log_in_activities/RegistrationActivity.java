package com.example.hund_hunter.log_in_activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hund_hunter.main_activities.OrderCreationActivity;
import com.example.hund_hunter.R;
import com.example.hund_hunter.main_activities.SeekerActivity;
import com.example.hund_hunter.data_classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name, familia, email, password;
    EditText[] eds = new EditText[5];
    String[] names;
    boolean find, lost;
    DatabaseReference ref;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);//TODO: сделать чтьобы съехжало при появлении клавиатуры

        eds[0] = findViewById(R.id.editTextTextPersonName);
        eds[1] = findViewById(R.id.editTextTextPersonName2);
        eds[2] = findViewById(R.id.editTextTextEmailAddress2);
        eds[3] = findViewById(R.id.editTextTextPassword);
        eds[4] = findViewById(R.id.tel);
        names = new String[]{"name", "familia", "email", "password", "tel"};

        // Проверка на непустые поля


        Bundle extras = getIntent().getExtras();
        find = extras.getBoolean("find");
        lost = extras.getBoolean("lost");


    }

    public void findButton(View view){
        System.out.println("hi");
        Log.d("yy", "base0");
        String[] edsTxt = new String[eds.length];
        boolean isNull = false;
        for(int i = 0; i<eds.length; i++){
            edsTxt[i] = eds[i].getText().toString();
            if(edsTxt[i].equals("")){
                isNull = true;
            }
        }


        if(isNull){
            Log.d("yy", "null");
            Toast.makeText(RegistrationActivity.this, "заполните поля", Toast.LENGTH_LONG).show();
            return;
        }


        FirebaseDatabase.getInstance().getReference().child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Map<String, Map<String,String>> map = (Map<String, Map<String,String>>)task.getResult().getValue();
                    Map<String, Map<String,String>> map2 = new HashMap<>();
                    for (Map.Entry<String, Map<String,String>> entry : map.entrySet()) {
                        map2.put(entry.getKey(), (Map<String, String>)entry.getValue());
                    }
                    boolean exists = false;
                    for (Map.Entry<String, Map<String,String>> entry : map2.entrySet()) {
                        Log.d("yy", entry.getValue().get("email"));
                        if(edsTxt[2].equals(entry.getValue().get("email"))){

                            exists = true;
                        }
                        //Log.d("yy", "Key = " + entry.getKey() + ", Value = " + entry.getValue().get("email"));
                    }
                    if(exists){
                        Toast.makeText(RegistrationActivity.this, "почта занята", Toast.LENGTH_LONG).show();
                        return;
                    }else{
                        SharedPreferences mySharedPreferences = getSharedPreferences(StartActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString(StartActivity.APP_PREFERENCES_EMAIL, edsTxt[2]);
                        editor.apply();

                        ref = FirebaseDatabase.getInstance().getReference();
                        usersRef = ref.child("users");
                        //DatabaseReference newUsersRef = usersRef.push();
                        usersRef.push().setValue(new User(edsTxt[0], edsTxt[1], edsTxt[2], edsTxt[3], edsTxt[4]));

                        Toast.makeText(RegistrationActivity.this, "успешно!)", Toast.LENGTH_LONG).show();
                    }

                }
                Intent reg_act = null;
                if(find){
                    reg_act = new Intent(RegistrationActivity.this, SeekerActivity.class);
                }else if(lost){
                    reg_act = new Intent(RegistrationActivity.this, OrderCreationActivity.class);
                }
                startActivity(reg_act);
            }
        });




    }

    public String nullTest(EditText ed){
        String txt = ed.getText().toString();
        return txt;

    }
}