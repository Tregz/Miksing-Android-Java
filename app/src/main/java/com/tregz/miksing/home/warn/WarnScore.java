package com.tregz.miksing.home.warn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.tregz.miksing.R;

public class WarnScore extends WarnInput {
    public static final String TAG = WarnScore.class.getSimpleName();

    public static WarnScore newInstance(String name) {
        WarnScore fragment = new WarnScore();
        fragment.setArguments(args(name));
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() != null) {
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext(), style);
            alert.setTitle(R.string.score);

            if (name == null) {
                final TextInputEditText edit = setInputLayout(alert);
                if (edit != null) {
                    alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (nameNotNull(edit)) listener.onSaveItem(name(edit));
                        }
                    });
                }
            } else {
                if (getActivity() != null) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View view = inflater.inflate(R.layout.text_label, null);
                    AppCompatTextView label = view.findViewById(R.id.tv_title);
                    label.setText(name);
                    alert.setView(view);
                    alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onSaveItem(name);
                        }
                    });
                }
            }
            alert.setNegativeButton(R.string.cancel, null);
            return alert.create();
        } else return super.onCreateDialog(savedInstanceState);
    }
}
