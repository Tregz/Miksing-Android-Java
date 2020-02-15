package com.tregz.miksing.home.warn;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseWarning;

public class WarnError extends BaseWarning {

    public static WarnError newInstance(String title, String message) {
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        WarnError fragment = new WarnError();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() != null) {
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext(), style);
            if (getArguments() != null) {
                String title = getArguments().getString(TITLE);
                if (title != null) alert.setTitle(title);
                String message = getArguments().getString(MESSAGE);
                if (message != null) alert.setMessage(message);
            }
            alert.setPositiveButton(R.string.ok, null);
            return alert.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }
}
