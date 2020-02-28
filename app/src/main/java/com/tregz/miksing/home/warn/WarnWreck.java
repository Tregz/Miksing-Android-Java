package com.tregz.miksing.home.warn;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseWarning;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.home.HomeView;

public class WarnWreck extends BaseWarning {
    public static final String TAG = WarnWreck.class.getSimpleName();
    private static final String DATA = "data";

    protected HomeView listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeView) listener = (HomeView) context;
        else onListenerError(context);
    }

    public static WarnWreck newInstance(Tube tube) {
        WarnWreck fragment = new WarnWreck();
        Bundle args = new Bundle();
        args.putParcelable(DATA, tube);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() != null && getArguments() != null) {
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext(), style);
            alert.setTitle(R.string.wreck);
            final Tube tube = getArguments().getParcelable(DATA);
            if (tube != null && getActivity() != null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.text_label, null);
                AppCompatTextView label = view.findViewById(R.id.tv_title);
                label.setText(tube.getName(getContext()));
                alert.setView(view);
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onWreck(tube);
                    }
                });
            }
            alert.setNegativeButton(R.string.cancel, null);
            return alert.create();
        } else return super.onCreateDialog(savedInstanceState);
    }
}
