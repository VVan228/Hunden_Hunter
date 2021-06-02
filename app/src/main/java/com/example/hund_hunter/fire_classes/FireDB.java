package com.example.hund_hunter.fire_classes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hund_hunter.data_classes.Order;
import com.example.hund_hunter.fire_classes.interfaces.OnCancelledListener;
import com.example.hund_hunter.fire_classes.interfaces.OnChildAddedListener;
import com.example.hund_hunter.fire_classes.interfaces.OnChildChangedListener;
import com.example.hund_hunter.fire_classes.interfaces.OnChildMovedListener;
import com.example.hund_hunter.fire_classes.interfaces.OnChildRemovedListener;
import com.example.hund_hunter.fire_classes.interfaces.OnDataChangeListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FireDB {
    FirebaseDatabase database;
    DatabaseReference ref;
    boolean connected;

    public FireDB(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
    }
    public FireDB(String[] childSeq){
        //пример заполнения childSeq
        //FireDB db = new FireDB(new String[]{"orders"});
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        for(String child: childSeq){
            ref = ref.child(child);
        }
    }

    public String pushValue(Object data){
        DatabaseReference q = ref.push();
        q.keepSynced(true);
        q.setValue(data);
        String res = q.toString().substring(q.getRoot().toString().length());
        String url = null;
        try {
            url = URLDecoder.decode(res, "UTF-8");
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public DatabaseReference getRef(){
        return ref;
    }

    //для получения данных нужно передать ChildEventListener и запрос,
    //который создается как объект и меняется при помощи методов из самого объекта как в примере.
    //кстати, в myQuery обязательно нужно передать ref при помощи getRef()
    //db.getData(listener, new myQuery(db.getRef()).orderBy("email").equalTo("vvang"));
    public void getData(MyQuery query, ChildEventListener listener){
        query.ref.addChildEventListener(listener);
    }


    public void checkConnection(){
        DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d("tag4me", "connected");
                    connected = true;
                } else {
                    Log.d("tag4me", "not connected");
                    connected = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag4me", "Listener was cancelled");
            }
        });
    }

    public void getParticularChild(String path, OnDataChangeListener listener){
        DatabaseReference r = database.getReference(path);
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onDataChange(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeParticularChild(String path){
        DatabaseReference r = database.getReference(path);
        r.removeValue();
    }

    public boolean isConnected(){
        return connected;
    }
}

