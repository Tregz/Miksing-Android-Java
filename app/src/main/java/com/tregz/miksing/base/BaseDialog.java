package com.tregz.miksing.base;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tregz.miksing.R;

public abstract class BaseDialog implements DialogInterface.OnShowListener {

    private Context context;
    AlertDialog alert;
    protected AlertDialog.Builder builder;
    protected String okay;

    public BaseDialog(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context, R.style.AlertDialogWrap);
        okay = context.getString(R.string.ok);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        // TODO
    }

    protected void show() {
        if (context instanceof Activity && !((Activity) context).isDestroyed()) {
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exit();
                }
            });
            builder.setNegativeButtonIcon(icon(R.drawable.ic_close, R.color.secondaryDark));
            builder.setPositiveButtonIcon(icon(R.drawable.ic_check, R.color.primaryPage));
            alert = builder.create();
            alert.setOnShowListener(this);
            alert.show();
        }
    }

    private void exit() {
        if (alert != null) {
            if (alert.isShowing()) alert.dismiss();
            alert.cancel();
        }
    }

    @SuppressWarnings("DEPRECATED")
    private Drawable icon(int resource, int tint) {
        Drawable drawable = ContextCompat.getDrawable(context, resource);
        if (drawable != null) {
            int color = ContextCompat.getColor(context, tint);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            else drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
        }
        return drawable;
    }

    protected View inflate(ViewGroup group, int layout) {
        return LayoutInflater.from(context).inflate(layout, group, false);
    }
}
