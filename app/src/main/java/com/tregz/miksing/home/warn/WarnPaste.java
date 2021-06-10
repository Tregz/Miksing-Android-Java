package com.tregz.miksing.home.warn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.tregz.miksing.R;

public class WarnPaste extends WarnInput {

    public static final String TAG = WarnPaste.class.getSimpleName();

    public static WarnPaste newInstance(String name) {
        WarnPaste fragment = new WarnPaste();
        fragment.setArguments(args(name));
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() != null) {
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext(), style);
            alert.setTitle(R.string.paste);
            final TextInputEditText edit = setInputLayout(alert);
            alert.setNegativeButton(R.string.cancel, null);
            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (nameNotNull(edit)) listener.onPastePlaylist(name(edit));
                }
            });
            return alert.create();
        } else return super.onCreateDialog(savedInstanceState);
    }
}
