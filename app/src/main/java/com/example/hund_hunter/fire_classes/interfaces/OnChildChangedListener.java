package com.example.hund_hunter.fire_classes.interfaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;

public interface OnChildChangedListener {
    void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName);
}
