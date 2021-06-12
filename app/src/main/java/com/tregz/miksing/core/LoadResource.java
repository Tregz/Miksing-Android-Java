package com.tregz.miksing.core;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public final class LoadResource {

    private final static String TAG = LoadResource.class.getSimpleName();

    public static int getId(Context context, Type type, String name) {
        return context.getResources().getIdentifier(name, type.defType(), context.getPackageName());
    }

    public static String getString(Context context, String name) {
        try {
            return context.getString(LoadResource.getId(context, Type.STRING, name));
        } catch (Resources.NotFoundException e) {
            if (e.getMessage() != null) Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public enum Type {
        DRAWABLE,
        STRING;

        String defType() {
            return this.name().toLowerCase();
        }
    }
}
