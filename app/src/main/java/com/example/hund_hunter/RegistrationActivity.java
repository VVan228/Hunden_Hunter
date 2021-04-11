package com.example.hund_hunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import android.text.format.DateFormat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name, familia, email, password;
    EditText[] eds = new EditText[4];
    String[] names;
    boolean find, lost;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);//TODO: сделать чтьобы съехжало при появлении клавиатуры

        eds[0] = findViewById(R.id.editTextTextPersonName);
        eds[1] = findViewById(R.id.editTextTextPersonName2);
        eds[2] = findViewById(R.id.editTextTextEmailAddress2);
        eds[3] = findViewById(R.id.editTextTextPassword);
        names = new String[]{"name", "familia", "email", "password"};

        // Проверка на непустые поля


        Bundle extras = getIntent().getExtras();
        find = extras.getBoolean("find");
        lost = extras.getBoolean("lost");
    }

    public void findButton(View view){
        String[] edsTxt = new String[eds.length];
        boolean isNull = false;
        for(int i = 0; i<eds.length; i++){
            edsTxt[i] = eds[i].getText().toString();
            if(edsTxt[i].equals("")){
                isNull = true;
            }
        }


        if(!isNull){
            ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference usersRef = ref.child("users");
            //DatabaseReference newUsersRef = usersRef.push();
            usersRef.push().setValue(new User(edsTxt[0], edsTxt[1], edsTxt[2], edsTxt[3]));
        }else{
            Toast.makeText(RegistrationActivity.this, "заполните поля", Toast.LENGTH_LONG).show();
            return;
        }

        /*FirebaseDatabase.getInstance().getReference("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User newPost = new User();
                newPost = dataSnapshot.getValue(User.class);
                System.out.println("Author: " + newPost.name.toString());
                System.out.println("Title: " + newPost.email.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                //User changedPost = dataSnapshot.getValue(User.class);
                Log.d("type",dataSnapshot.getValue(User.class).getEmail());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User removedPost = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/

        Intent reg_act = null;
        if(find){
            reg_act = new Intent(RegistrationActivity.this, SeekerActivity.class);
        }else if(lost){
            reg_act = new Intent(RegistrationActivity.this, OrderCreationActivity.class);
        }
        startActivity(reg_act);
    }

    public String nullTest(EditText ed){
        String txt = ed.getText().toString();
        return txt;

    }
}