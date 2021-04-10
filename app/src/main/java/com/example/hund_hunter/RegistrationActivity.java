package com.example.hund_hunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;

import android.text.format.DateFormat;

public class RegistrationActivity extends AppCompatActivity {

    private static int SIGN_IN_CODE = 1;
    private RelativeLayout activity_main;
    // адаптация объектов из БД в обычные объекты
    private FirebaseListAdapter<Message> adapter;
    private FloatingActionButton sendButton;
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // знаем, что получили авторизацию пользователя
        if (requestCode == SIGN_IN_CODE) {
            // успешная авторизация
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_main, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
                displayAllMessages();
            }
            else {
                Snackbar.make(activity_main, "Ошибка. Вы не авторизованы", Snackbar.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);//TODO: сделать чтьобы съехжало при появлении клавиатуры

        // авторизирован ли пользователь
        activity_main = findViewById(R.id.rl_activity_main);

        sendButton = findViewById(R.id.bth_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField = findViewById(R.id.ed_message_field);

                if (textField.getText().toString() == "") {
                    return;
                }
                FirebaseDatabase.getInstance().getReference().push().setValue(
                        new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                textField.getText().toString())
                );
                textField.setText("");
            }
        });

        // пользователь не авторизован
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // авторизация пользователя( вызов ф-ии, код)
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        }
        else {
            // выбрать окно, где будет сообщение
            Snackbar.make(activity_main, "Вы авторизованы", Snackbar.LENGTH_SHORT).show();
            displayAllMessages();
        }
    }

    private void displayAllMessages() {
        // ссылка на список с сообщениями
        ListView list_of_messages = findViewById(R.id.lv_list_messages);
        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setLayout(R.layout.list_item)
                .setLifecycleOwner(this)
                .setQuery(FirebaseDatabase.getInstance().getReference(), Message.class)
                .build();
        //this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()
        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {
                TextView mess_user, mess_time, mess_text;
                mess_user = v.findViewById(R.id.tv_user);
                //
                //mess_time = v.findViewById(R.id.tv_time);
                mess_text = v.findViewById(R.id.tv_text);

                // ф-ии класса Message
                mess_user.setText(model.getUserName());
                mess_text.setText(model.getText_message());
                mess_time.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessage_time()));
            }
        };
        list_of_messages.setAdapter(adapter);
    }
}