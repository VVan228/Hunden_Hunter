package com.example.hund_hunter.fire_classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

//пример создания запроса
//new myQuery(db.getRef()).orderBy("email").equalTo("vvang");
public class myQuery{
    public Query ref;

    public myQuery(DatabaseReference _ref){
        ref = _ref;
    }

    public myQuery orderBy(String child){
        ref = ref.orderByChild(child);
        return this;
    }

    public myQuery limitToFirst(int n){
        ref = ref.limitToFirst(n);
        return this;
    }

    public myQuery equalTo(String s){
        ref = ref.equalTo(s);
        return this;
    }
}
