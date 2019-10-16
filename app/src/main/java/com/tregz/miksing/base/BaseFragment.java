package com.tregz.miksing.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tregz.miksing.home.HomeView;

public abstract class BaseFragment extends Fragment {
    protected static String TAG = BaseFragment.class.getSimpleName();

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
}
