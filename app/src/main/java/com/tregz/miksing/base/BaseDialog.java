package com.tregz.miksing.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tregz.miksing.R;

public abstract class BaseDialog implements DialogInterface.OnShowListener {

    protected Context context;
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

    private Drawable icon(int resource, int tint) {
        Drawable drawable = ContextCompat.getDrawable(context, resource);
        if (drawable != null) {
            int color = ContextCompat.getColor(context, tint);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP); // no inspection
            else drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
        }
        return drawable;
    }

    protected EditText input() {
        FrameLayout container = new FrameLayout(context);
        int match = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(match, match);
        container.setLayoutParams(containerParams);
        EditText editText = new AppCompatEditText(context);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(match, match);
        frameParams.setMargins(20,0,20,0);
        editText.setLayoutParams(frameParams);
        int color = ContextCompat.getColor(context, R.color.primaryDark);
        editText.setTextColor(color);
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        ViewCompat.setBackgroundTintList(editText, colorStateList);
        container.addView(editText);
        builder.setView(container);
        return editText;
    }

    protected View inflate(ViewGroup group, int layout) {
        return LayoutInflater.from(context).inflate(layout, group, false);
    }
}
