package com.tregz.miksing.home.list;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.databinding.FragmentListBinding;

import static android.telephony.TelephonyManager.PHONE_TYPE_NONE;

public abstract class ListFragment extends BaseFragment implements ListListener {
    //private final String TAG = ListFragment.class.getSimpleName();

    protected RecyclerView recycler;
    protected FragmentListBinding binding;
    protected RecyclerView.Adapter<?> adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = binding.recycler;
        if (columns() <= 1) recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        else recycler.setLayoutManager(new GridLayoutManager(getContext(), columns()));
    }

    abstract public void search(String query);

    abstract public void sort();

    private int columns() {
        if (getContext() != null) {
            Object tm = getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                boolean isTablet = ((TelephonyManager) tm).getPhoneType() == PHONE_TYPE_NONE;
                int orientation = getContext().getResources().getConfiguration().orientation;
                return isTablet ? orientation + 1 : orientation;
            }
        }
        return 1;
    }
}
