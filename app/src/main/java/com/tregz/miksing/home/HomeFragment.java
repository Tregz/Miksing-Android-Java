package com.tregz.miksing.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.data.user.tube.UserTubeRelation;
import com.tregz.miksing.databinding.FragmentHomeBinding;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.song.main.SongMainFragment;
import com.tregz.miksing.home.list.song.plan.SongPlanFragment;

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
        binding.pager.setAdapter(new HomePager(getContext(), getChildFragmentManager()));
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
        sort();
    }

    void prepare(UserTubeRelation relation) {
        binding.pager.setCurrentItem(1);
        SongPlanFragment.relation = relation;
        ListFragment main = page(HomePager.HomeTab.EVERYTHING.ordinal());
        if (main instanceof SongMainFragment) ((SongMainFragment)main).onTubeSongRelationObserve();
        ListFragment plan = page(HomePager.HomeTab.PREPARE.ordinal());
        if (plan instanceof SongPlanFragment) ((SongPlanFragment)plan).onTubeSongRelationObserve();
    }

    void save(String name, boolean paste) {
        ListFragment page = page();
        if (page instanceof SongPlanFragment) ((SongPlanFragment)page).save(name, paste);
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
        return page(binding.pager.getCurrentItem());
    }

    private ListFragment page(int position) {
        if (binding.pager.getAdapter() != null) {
            Object fragment = binding.pager.getAdapter().instantiateItem(binding.pager, position);
            if (fragment instanceof ListFragment) return ((ListFragment) fragment);
        }
        return null;
    }

    private enum Page {
        EVERYTHING,

    }
}
