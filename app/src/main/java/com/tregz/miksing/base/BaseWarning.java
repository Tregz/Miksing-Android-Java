package com.tregz.miksing.base;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.tregz.miksing.R;

public abstract class BaseWarning extends DialogFragment {

    protected static final String TITLE = "title";
    protected static final String MESSAGE = "message";
    protected int style = R.style.MaterialDialog;

    protected String str(int resource) {
        return getContext() != null ? getContext().getString(resource) : null;
    }

    protected void toast(int resource) {
        Toast.makeText(getContext(), str(resource), Toast.LENGTH_SHORT).show();
    }

    protected void onListenerError(Context context) {
        String error = " must implement interaction listener";
        throw new RuntimeException(context.toString() + error);
    }
}
