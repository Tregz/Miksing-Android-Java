package com.tregz.miksing.base;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.tregz.miksing.R;
import com.tregz.miksing.home.list.tube.TubeListFragment;

public abstract class BaseActivity extends AppCompatActivity {
    protected static String TAG = BaseActivity.class.getSimpleName();
    protected static int PRIMARY;
    protected final int HOST = R.id.nav_host_fragment;
    protected final int TUBE = R.id.nav_tube_fragment;

    @SuppressWarnings("DEPRECATION")
    protected void fullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) controller.hide(WindowInsets.Type.navigationBars());
        } else getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    @SuppressWarnings("DEPRECATION")
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            int navigator = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            int immersive = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(navigator | immersive);
        }
    }

    public FragmentManager manager(int host) {
        NavHostFragment nhf = (NavHostFragment) getSupportFragmentManager().findFragmentById(host);
        return nhf != null ? nhf.getChildFragmentManager() : null;
    }

    protected boolean check(String permission) {
        int granted = PackageManager.PERMISSION_GRANTED;
        return ActivityCompat.checkSelfPermission(this, permission) == granted;
    }

    protected void image(Uri uri, int resource) {
        Glide.with(this).load(uri).into((ImageView) findViewById(resource));
    }

    protected void snack(String message) {
        Snackbar.make(findViewById(R.id.bottom), message, Snackbar.LENGTH_LONG).show();
    }

    protected void toast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected boolean root() {
        return currentFragmentId() == PRIMARY;
    }

    protected Fragment primary() {
        return host(HOST).getChildFragmentManager().getPrimaryNavigationFragment();
    }

    protected TubeListFragment playlist() {
        Fragment fragment = host(TUBE).getChildFragmentManager().getPrimaryNavigationFragment();
        return fragment instanceof TubeListFragment ? (TubeListFragment) fragment : null;
    }

    protected Integer currentFragmentId() {
        NavDestination destination = controller().getCurrentDestination();
        return destination != null ? destination.getId() : null;
    }

    protected NavController controller() {
        return Navigation.findNavController(this, HOST);
    }

    protected NavHostFragment host(int id) {
        return (NavHostFragment) getSupportFragmentManager().findFragmentById(id);
    }
}
