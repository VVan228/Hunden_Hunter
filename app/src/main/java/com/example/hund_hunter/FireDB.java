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
        //пример заполнения childSeq
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

    DatabaseReference getRef(){
        return ref;
    }

    //для получения данных нужно передать ChildEventListener и запрос, который создается как объект и меняется при помощи методов из самого объекта как в примере
    //db.getData(listener, new myQuery(db.getRef()).orderBy("email").equalTo("vvang"));
    void getData(ChildEventListener listener, myQuery query){
        query.ref.addChildEventListener(listener);
    }
}

//пример создания запроса
//new myQuery(db.getRef()).orderBy("email").equalTo("vvang");
class myQuery{
    public Query ref;

    myQuery(DatabaseReference _ref){
        ref = _ref;
    }

    myQuery orderBy(String child){
        ref = ref.orderByChild(child);
        return this;
    }

    myQuery limitToFirst(int n){
        ref = ref.limitToFirst(n);
        return this;
    }

    myQuery equalTo(String s){
        ref = ref.equalTo(s);
        return this;
    }
}
