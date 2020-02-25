package com.tregz.miksing.home.warn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseWarning;
import com.tregz.miksing.home.HomeView;

public class WarnInput extends BaseWarning {

    static final String NAME = "name";

    protected HomeView listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeView) listener = (HomeView) context;
        else onListenerError(context);
    }

    TextInputEditText setInputLayout(MaterialAlertDialogBuilder alert) {
        TextInputEditText edit = null;
        if (getActivity() != null) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_input, null);
            edit = view.findViewById(R.id.et_input);
            TextInputLayout inputLayout = view.findViewById(R.id.input_layout);
            inputLayout.setHint(str(R.string.playlist_name));
            if (getArguments() != null && getArguments().getString(NAME) != null)
                edit.setText(getArguments().getString(NAME));
            alert.setView(view);
        }
        return edit;
    }

    String name(TextInputEditText edit) {
        return edit.getText() != null ? edit.getText().toString() : "null";
    }

    boolean nameNotNull(TextInputEditText edit) {
        if (edit.getText() == null || edit.getText().toString().isEmpty()) {
            toast(R.string.playlist_name_must_not_null);
            return false;
        }
        return true;
    }
}
