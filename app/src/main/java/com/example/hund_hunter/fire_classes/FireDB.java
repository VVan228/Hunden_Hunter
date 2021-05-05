package com.example.hund_hunter.fire_classes;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireDB {
    FirebaseDatabase database;
    DatabaseReference ref;

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

    public void pushValue(Object data){
        ref.push().setValue(data);
    }

    public DatabaseReference getRef(){
        return ref;
    }

    //для получения данных нужно передать ChildEventListener и запрос,
    //который создается как объект и меняется при помощи методов из самого объекта как в примере.
    //кстати, в myQuery обязательно нужно передать ref при помощи getRef()
    //db.getData(listener, new myQuery(db.getRef()).orderBy("email").equalTo("vvang"));
    public void getData(ChildEventListener listener, myQuery query){
        query.ref.addChildEventListener(listener);
    }
}

