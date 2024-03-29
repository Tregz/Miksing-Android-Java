package com.tregz.miksing.home;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.tregz.miksing.R;
import com.tregz.miksing.core.auth.AuthUtil;
import com.tregz.miksing.core.pref.PrefShared;
import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.song.SongCount;
import com.tregz.miksing.data.song.SongDelete;
import com.tregz.miksing.data.user.UserDelete;
import com.tregz.miksing.databinding.ActivityHomeBinding;

public class HomeNavigation implements
        DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener,
        NavigationBarView.OnItemSelectedListener,
        OnCompleteListener<Void>, SongCount.Total {
    private final String TAG = HomeNavigation.class.getSimpleName();

    private final ActivityHomeBinding binding;
    private final HomeView view;
    private String login, logout;
    private boolean initialized = false;

    HomeNavigation(@NonNull HomeView view, @NonNull ActivityHomeBinding binding) {
        this.view = view;
        this.binding = binding;
        if (binding.drawerLayout != null) binding.drawerLayout.addDrawerListener(this);
        if (binding.navStart != null) binding.navStart.setNavigationItemSelectedListener(this);
        if (binding.navRight != null) binding.navRight.setNavigationItemSelectedListener(this);
        if (binding.contentHome != null) {
            BottomNavigationView bottom = binding.contentHome.bottom;
            bottom.setOnItemSelectedListener(this);
            Context context = bottom.getContext();
            login = context.getString(R.string.nav_login_title);
            logout = context.getString(R.string.nav_logout_title);
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (binding.navStart != null) {
            Context context = binding.navStart.getContext();
            PrefShared.getInstance(context).setUsername("");
            PrefShared.getInstance(context).setEmail("");
            new SongDelete(context).wipe();
            new UserDelete(context).wipe();
            update();
        }
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        // do nothing
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        if (binding.navRight != null && drawerView.getId() == binding.navRight.getId()) update();
        else view.onDrawerStartOpened();
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        // do nothing
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (!initialized) {
            initialized = true;
            headerHeight(binding.navRight);
            headerHeight(binding.navStart);
            /* Custom height for MenuItem */
            if (binding.navRight != null && binding.contentHome != null) {
                View itemView = binding.navRight.getMenu().getItem(0).getActionView();
                View menuView = ((ViewGroup)itemView.getParent().getParent());
                int margin = menuView.getMeasuredHeight() - itemView.getMeasuredHeight();
                int height = binding.contentHome.bottom.getMeasuredHeight() - margin;
                setHeight(menuView, height);
                setHeight(itemView, height);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_login) {
            if (!AuthUtil.logged() && view != null) AuthUtil.login(view);
            else if (binding.navRight != null) {
                Context context = binding.navRight.getContext();
                AuthUI.getInstance().signOut(context).addOnCompleteListener(this);
            }
        } else if (item.getItemId() == R.id.nav_clear) {
            if (binding.navStart != null) {
                Context context =binding.navStart.getContext();
                new SongDelete(context).wipe();
            }
        } else if (item.getItemId() == R.id.sort_alpha) {
            ListSorted.comparator = ListSorted.Order.ALPHA;
            item.setChecked(true);
            view.sort();
        } else if (item.getItemId() == R.id.sort_digit) {
            ListSorted.comparator = ListSorted.Order.DIGIT;
            item.setChecked(true);
            view.sort();
        } else if (item.getItemId() == R.id.sort_fresh) {
            ListSorted.comparator = ListSorted.Order.FRESH;
            item.setChecked(true);
            view.sort();
        }
        return false;
    }

    @Override
    public void size(int number) {
        Log.d(TAG, "Song size: " + number);
    }

    void toggle(int gravity) {
        if (binding.drawerLayout != null) {
            if (binding.drawerLayout.isDrawerOpen(gravity))
                binding.drawerLayout.closeDrawer(gravity);
            else binding.drawerLayout.openDrawer(gravity);
        }
    }

    void update() {
        view.userFragment().update();
        if (binding.navRight != null) {
            MenuItem item = binding.navRight.getMenu().getItem(0);
            if (AuthUtil.logged()) {
                if (item.getTitle() != logout) {
                    item.setIcon(R.drawable.ic_exit);
                    item.setTitle(logout);
                }
            } else {
                if (item.getTitle() != login) {
                    item.setIcon(R.drawable.ic_key);
                    item.setTitle(login);
                }
            }
        }
    }

    /* Proportional height of drawer's header */
    private void headerHeight(NavigationView navigation) {
        if (binding.contentHome != null) {
            Context context = navigation.getContext();
            View header = navigation.getHeaderView(0);
            int height = ((Activity) context).getWindow().getDecorView().getHeight();
            setHeight(header, height - binding.contentHome.bottom.getMeasuredHeight());
        }
    }

    private void setHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }
}
