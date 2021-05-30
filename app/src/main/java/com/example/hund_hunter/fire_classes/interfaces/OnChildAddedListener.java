package com.example.hund_hunter.fire_classes.interfaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;

public interface OnChildAddedListener {
    void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName);
}
