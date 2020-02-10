package com.tregz.miksing.base.icon;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;

public final class IconUtil {

    public static void setTint(ImageView view, int resource) {
        int color = ContextCompat.getColor(view.getContext(), resource);
        ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(color));
    }

    public static void setTint(Context context, Drawable drawable, int resource) {
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, resource));
    }
}
