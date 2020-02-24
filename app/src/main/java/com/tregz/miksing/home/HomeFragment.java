package com.tregz.miksing.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.databinding.FragmentHomeBinding;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.song.SongListFragment;

public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    //private final String TAG = HomeFragment.class.getSimpleName();
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pager.setAdapter(new HomePager(getChildFragmentManager()));
        binding.pager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // do nothing
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // do nothing
    }

    @Override
    public void onPageSelected(int position) {
        if (getActivity() != null) ((HomeActivity) getActivity()).setFabVisibility(position > 0);
    }

    void prepare(String id) {
        binding.pager.setCurrentItem(1);
        ListFragment page = page();
        if (page instanceof SongListFragment) ((SongListFragment)page).live(id);
    }

    void save(String name) {
        ListFragment page = page();
        if (page != null) page.save(name);
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
        if (binding.pager.getAdapter() != null) {
            int position = binding.pager.getCurrentItem();
            Object fragment = binding.pager.getAdapter().instantiateItem(binding.pager, position);
            if (fragment instanceof ListFragment) return ((ListFragment) fragment);
        }
        return null;
    }
}
