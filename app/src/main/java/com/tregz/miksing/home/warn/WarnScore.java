package com.tregz.miksing.home.warn;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseWarning;
import com.tregz.miksing.home.HomeView;

public class WarnScore extends WarnInput {
    public static final String TAG = WarnScore.class.getSimpleName();

    public static WarnScore newInstance(String name) {
        Bundle args = new Bundle();
        args.putString(NAME, name);
        WarnScore fragment = new WarnScore();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() != null) {
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext(), style);
            alert.setTitle(R.string.save);
            final TextInputEditText edit = setInputLayout(alert);
            if (edit != null) {
                alert.setNegativeButton(R.string.cancel, null);
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nameNotNull(edit)) listener.onSaveItem(name(edit));
                    }
                });
            }
            return alert.create();
        } else return super.onCreateDialog(savedInstanceState);
    }
}
