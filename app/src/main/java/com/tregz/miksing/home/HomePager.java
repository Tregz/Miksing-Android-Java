package com.tregz.miksing.home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tregz.miksing.R;
import com.tregz.miksing.home.list.song.main.SongMainFragment;
import com.tregz.miksing.home.list.song.plan.SongPlanFragment;

import org.jetbrains.annotations.NotNull;

public class HomePager extends FragmentStateAdapter {

    public HomePager(FragmentActivity fa) {
        super(fa);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0: return new SongMainFragment();
            case 1: return new SongPlanFragment();
        }
        return new Fragment();
    }

    @Override
    public int getItemCount() {
        return HomeTab.values().length;
    }

    public enum HomeTab {
        EVERYTHING(R.string.page_everything),
        PREPARE(R.string.page_prepare);

        private final int tab;

        public int getTab() {
            return tab;
        }

        HomeTab(int tab) {
            this.tab = tab;
        }
    }
}
