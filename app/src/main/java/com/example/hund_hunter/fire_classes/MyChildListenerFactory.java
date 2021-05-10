package com.example.hund_hunter.fire_classes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hund_hunter.fire_classes.interfaces.OnCancelledListener;
import com.example.hund_hunter.fire_classes.interfaces.OnChildAddedListener;
import com.example.hund_hunter.fire_classes.interfaces.OnChildChangedListener;
import com.example.hund_hunter.fire_classes.interfaces.OnChildMovedListener;
import com.example.hund_hunter.fire_classes.interfaces.OnChildRemovedListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MyChildListenerFactory {

    //дефолтные пустые методы, остаются если не добавили свои
    OnChildAddedListener addedListener = new OnChildAddedListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}};
    OnChildChangedListener changedListener = new OnChildChangedListener() {
        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}};
    OnChildMovedListener movedListener = new OnChildMovedListener() {
        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}};
    OnChildRemovedListener removedListener = new OnChildRemovedListener() {
        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}};
    OnCancelledListener cancelledListener = new OnCancelledListener() {
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}};


    //пример использования
    /*new MyChildListenerFactory().addAddedListener(new OnChildAddedListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }
    }).create()*/
    public MyChildListenerFactory(){}


    //используем методы чтобы присвоить нужные листенеры, нe нужные будут пустыми по дефолту
    public MyChildListenerFactory addAddedListener(OnChildAddedListener _addedListener){
        addedListener = _addedListener;
        return this;
    }
    public MyChildListenerFactory addChangedListener(OnChildChangedListener _changedListener){
        changedListener = _changedListener;
        return this;
    }
    public MyChildListenerFactory addMovedListener(OnChildMovedListener _movedListener){
        movedListener = _movedListener;
        return this;
    }
    public MyChildListenerFactory addRemovedListener(OnChildRemovedListener _removedListener){
        removedListener = _removedListener;
        return this;
    }
    public MyChildListenerFactory addCancelledListener(OnCancelledListener _cancelledListener){
        cancelledListener = _cancelledListener;
        return this;
    }


    //после добавления нужных листенеров вызываем create который вернет готовый ChildEventListener
    public ChildEventListener create(){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                addedListener.onChildAdded(snapshot, previousChildName);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                changedListener.onChildChanged(snapshot, previousChildName);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                removedListener.onChildRemoved(snapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                movedListener.onChildMoved(snapshot, previousChildName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cancelledListener.onCancelled(error);
            }
        };
    }
}
