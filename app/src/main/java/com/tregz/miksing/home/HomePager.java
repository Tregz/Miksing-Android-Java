package com.tregz.miksing.home;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tregz.miksing.R;
import com.tregz.miksing.home.list.ListFragment;

public class HomePager extends FragmentStatePagerAdapter {

    private Context context;
    private ListFragment[] pages = new ListFragment[2];

    HomePager(Context context, FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        pages[0] = new ListFragment();
        pages[1] = new ListFragment();
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
        return context.getResources().getStringArray(R.array.page_tabs)[position];
    }
}
