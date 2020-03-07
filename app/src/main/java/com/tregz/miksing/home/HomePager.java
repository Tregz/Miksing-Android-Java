package com.tregz.miksing.home;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tregz.miksing.R;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.song.main.SongMainFragment;
import com.tregz.miksing.home.list.song.plan.SongPlanFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePager extends FragmentStatePagerAdapter {

    private List<ListFragment> pages = new ArrayList<>();
    private Context context;

    HomePager(Context context, FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        pages.add(new SongMainFragment());
        pages.add(new SongPlanFragment());
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
        return context.getString(HomeTab.values()[position].getTab());
    }

    public enum HomeTab {
        EVERYTHING(R.string.page_everything),
        PREPARE(R.string.page_prepare);

        private int tab;

        public int getTab() {
            return tab;
        }

        HomeTab(int tab) {
            this.tab = tab;
        }
    }
}
