package com.tregz.miksing.home.list;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.data.DataPositionable;

import static com.tregz.miksing.data.Data.UNDEFINED;

public class ListPosition {
    private final String TAG = ListPosition.class.getSimpleName();

    private DatabaseReference ref;

    public ListPosition(String p0, String p1, String p2) {
        if (p1 != null && !p1.equals(UNDEFINED))
            ref = FirebaseDatabase.getInstance().getReference(p0).child(p1).child(p2);
    }

    public boolean editable() {
        return ref != null;
    }

    public boolean hasChanged(DataPositionable data, final String childId, final int position) {
        if (data.getPosition() != position) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Relation i: " + position + " id: " + childId);
            data.setPosition(position);
            if (ref != null) ref.child(childId).setValue(position);
            return true;
        } else return false;
    }
}
