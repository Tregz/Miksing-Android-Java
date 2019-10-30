package com.tregz.miksing.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.item.ItemType;

public class HomePager extends FragmentStatePagerAdapter {

    private ListFragment[] pages = new ListFragment[2];

    HomePager(FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pages[0] = ListFragment.newInstance(ItemType.values()[0]);
        pages[1] = ListFragment.newInstance(ItemType.values()[1]);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages[position];
    }

    @Override
    public int getCount() {
        return pages.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return ItemType.values()[position].getType();
    }
}
