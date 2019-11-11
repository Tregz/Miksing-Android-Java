package com.tregz.miksing.base;

import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public abstract class BaseRealtime implements ChildEventListener {

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // do nothing
    }

    protected int getInt(DataSnapshot snap, String key) {
        Integer value = snap.child(key).getValue(Integer.class);
        return value != null ? value : 0;
    }

    protected long getLong(DataSnapshot snap, String key) {
        Long value = snap.child(key).getValue(Long.class);
        return value != null ? value : 0;
    }

    protected String getString(DataSnapshot snap, String key) {
        return snap.child(key).getValue(String.class);
    }

    protected boolean getBoolean(DataSnapshot snap, String key) {
        Boolean value = snap.child(key).getValue(Boolean.class);
        return value != null ? value : false;
    }
}
