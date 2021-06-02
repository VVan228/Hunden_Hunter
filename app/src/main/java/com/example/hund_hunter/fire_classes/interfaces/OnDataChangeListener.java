package com.example.hund_hunter.fire_classes.interfaces;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

public interface OnDataChangeListener {
    public void onDataChange(@NonNull DataSnapshot snapshot);
}
