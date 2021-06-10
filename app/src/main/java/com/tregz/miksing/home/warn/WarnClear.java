package com.tregz.miksing.home.warn;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseWarning;
import com.tregz.miksing.home.HomeView;

public class WarnClear extends BaseWarning {

    public static final String TAG = WarnClear.class.getSimpleName();
    private HomeView listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeView) listener = (HomeView) context;
        else onListenerError(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() != null && getActivity() != null) {
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext(), style);
            alert.setTitle(R.string.clear);
            alert.setNegativeButton(R.string.cancel, null);
            alert.setPositiveButton(R.string.ok, (dialog, which) -> listener.onClearPlaylist());
            return alert.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }
}
