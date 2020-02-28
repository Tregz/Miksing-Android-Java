package com.tregz.miksing.home.list;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.R;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.data.DataPositionable;

public class ListPosition {

    private DatabaseReference ref;

    public ListPosition(String p0, String p1, String p2) {
        if (p1 != null) ref = FirebaseDatabase.getInstance().getReference(p0).child(p1).child(p2);
    }

    public boolean hasChanged(DataPositionable data, String childId, int position) {
        if (data.getPosition() != position) {
            data.setPosition(position);
            if (ref != null) ref.child(childId).setValue(position);
            return true;
        } else return false;
    }

    public int error() {
        return AuthUtil.logged() ? R.string.to_save_paste : R.string.to_save_login;
    }
}
