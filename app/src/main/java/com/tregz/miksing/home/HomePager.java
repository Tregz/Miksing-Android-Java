package com.tregz.miksing.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.song.SongFragment;
import com.tregz.miksing.home.list.take.TakeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePager extends FragmentStatePagerAdapter {

    private List<ListFragment> pages = new ArrayList<>();

    HomePager(FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pages.add(new SongFragment());
        pages.add(new TakeFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return HomeTab.values()[position].getTab();
    }
}
