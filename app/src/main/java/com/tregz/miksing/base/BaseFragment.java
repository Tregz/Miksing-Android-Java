package com.tregz.miksing.base;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tregz.miksing.home.HomeView;

public abstract class BaseFragment extends Fragment {

    protected HomeView listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (HomeView) context;
        } catch (ClassCastException e) {
            String name = HomeView.class.getSimpleName();
            throw new ClassCastException(context.toString() + " must implement " + name);
        }
    }

    protected void toast(@NonNull String message) {
        if (getContext() != null) Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
