package com.example.hund_hunter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FireDB {
    FirebaseDatabase database;
    DatabaseReference ref;

    FireDB(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
    }
    FireDB(String[] childSeq){
        //FireDB db = new FireDB(new String[]{"orders"});
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        for(String child: childSeq){
            ref = ref.child(child);
        }
    }

    void pushValue(Object data){
        ref.push().setValue(data);
    }

    void getData(ChildEventListener listener){
        Query query = ref;
        query.addChildEventListener(listener);
    }
}
