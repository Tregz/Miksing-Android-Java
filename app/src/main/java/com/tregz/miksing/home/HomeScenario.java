package com.tregz.miksing.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.tregz.miksing.R;

class HomeScenario {
    private static final HomeScenario ourInstance = new HomeScenario();

    static HomeScenario getInstance() {
        return ourInstance;
    }

    private HomeScenario() {
    }

    private final int HOST = R.id.nav_host_fragment;

    void pop(AppCompatActivity activity) {
        controller(activity).popBackStack();
    }

    void navigate(AppCompatActivity activity, int action) {
        controller(activity).navigate(action);
    }

    Fragment primary(AppCompatActivity activity) {
        return host(activity).getChildFragmentManager().getPrimaryNavigationFragment();
    }

    Integer fragmentId(AppCompatActivity activity) {
        NavDestination destination = controller(activity).getCurrentDestination();
        return destination != null ? destination.getId() : null;
    }

    private NavController controller(AppCompatActivity activity) {
        return Navigation.findNavController(activity, HOST);
    }

    private NavHostFragment host(AppCompatActivity activity) {
        return (NavHostFragment) activity.getSupportFragmentManager().findFragmentById(HOST);
    }
}
