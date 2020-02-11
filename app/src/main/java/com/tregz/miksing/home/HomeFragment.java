package com.tregz.miksing.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.home.list.ListFragment;

public class HomeFragment extends BaseFragment {
    //private final String TAG = HomeFragment.class.getSimpleName();

    private ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager = view.findViewById(R.id.view_pager);
        pager.setAdapter(new HomePager(getChildFragmentManager()));
    }

    void save() {
        ListFragment page = page();
        if (page != null) page.save("Test");
    }

    void search(String query) {
        ListFragment page = page();
        if (page != null) page.search(query);
    }

    void sort() {
        ListFragment page = page();
        if (page != null) page.sort();
    }

    private ListFragment page() {
        if (pager.getAdapter() != null) {
            Object fragment = pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
            if (fragment instanceof ListFragment) return ((ListFragment) fragment);
        }
        return null;
    }
}
