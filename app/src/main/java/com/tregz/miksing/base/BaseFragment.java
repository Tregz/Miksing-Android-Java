package com.tregz.miksing.base;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected void toast(int resource) {
        String message = getContext() != null ? getContext().getString(resource) : null;
        if (message != null) toast(message);
    }

    protected void toast(@NonNull String message) {
        if (getContext() != null) Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
