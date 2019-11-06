package com.tregz.miksing.base;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.tregz.miksing.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    protected static String TAG = BaseActivity.class.getSimpleName();
    protected static int PRIMARY;
    protected final int HOST = R.id.nav_host_fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        for (BaseDialog dialog : dialogs) if (dialog.alert != null) dialog.alert.dismiss();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public BaseDialog add(BaseDialog dialog) {
        dialogs.add(dialog);
        return dialog;
    }

    public ViewGroup getViewGroup() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
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
        return host().getChildFragmentManager().getPrimaryNavigationFragment();
    }

    protected Integer currentFragmentId() {
        NavDestination destination = controller().getCurrentDestination();
        return destination != null ? destination.getId() : null;
    }

    protected List<BaseDialog> dialogs = new ArrayList<>();

    protected NavController controller() {
        return Navigation.findNavController(this, HOST);
    }

    protected NavHostFragment host() {
        return (NavHostFragment) getSupportFragmentManager().findFragmentById(HOST);
    }
}
