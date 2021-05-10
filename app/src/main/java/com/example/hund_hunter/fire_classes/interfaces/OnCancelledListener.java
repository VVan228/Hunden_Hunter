package com.example.hund_hunter.fire_classes.interfaces;

import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseError;


public interface OnCancelledListener {
    void onCancelled(@NonNull DatabaseError error);
}
