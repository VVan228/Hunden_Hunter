package com.example.hund_hunter.fire_classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

//пример создания запроса
//new myQuery(db.getRef()).orderBy("email").equalTo("vvang");
public class MyQuery {
    public Query ref;

    public MyQuery(DatabaseReference _ref){
        ref = _ref;
    }

    public MyQuery orderBy(String child){
        ref = ref.orderByChild(child);
        return this;
    }

    public MyQuery limitToFirst(int n){
        ref = ref.limitToFirst(n);
        return this;
    }

    public MyQuery equalTo(String s){
        ref = ref.equalTo(s);
        return this;
    }
}
