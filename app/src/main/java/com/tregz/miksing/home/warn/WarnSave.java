package com.tregz.miksing.home.warn;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseWarning;
import com.tregz.miksing.home.HomeView;

public class WarnSave extends BaseWarning {

    public static final String TAG = WarnSave.class.getSimpleName();
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
            alert.setTitle(R.string.save);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_input, null);
            final TextInputEditText edit = view.findViewById(R.id.et_input);
            TextInputLayout inputLayout = view.findViewById(R.id.input_layout);
            inputLayout.setHint(str(R.string.playlist_name));
            alert.setView(view);
            alert.setNegativeButton(R.string.cancel, null);
            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (edit.getText() == null || edit.getText().toString().isEmpty())
                        toast(R.string.playlist_name_must_not_null);
                    else listener.onSaveItem(edit.getText().toString());
                }
            });
            return alert.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }
}
