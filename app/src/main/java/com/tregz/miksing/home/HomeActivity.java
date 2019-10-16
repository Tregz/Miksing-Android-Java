package com.tregz.miksing.home;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseActivity;
import com.tregz.miksing.home.work.WorkFragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class HomeActivity extends BaseActivity implements HomeView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!back()) {
                    navigate(R.id.action_homeFragment_to_workFragment);
                    expand((FloatingActionButton)v);
                }
            }
        });
        findViewById(R.id.clear_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO dialog
                Fragment primary = primary();
                if (primary instanceof WorkFragment) ((WorkFragment) primary).clear();
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO dialog
                Fragment primary = primary();
                if (primary instanceof WorkFragment) ((WorkFragment) primary).save();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void saved() {
        back();
        //Fragment primary = primary();
        //Log.d(TAG, "homeFrag " + (primary instanceof HomeFragment));
        //if (primary instanceof HomeFragment) ((HomeFragment) primary).update();
    }

    private void navigate(int action) {
        HomeNavigation.getInstance().navigate(this, action);
    }

    private void expand(FloatingActionButton fab) {
        fab.setExpanded(!fab.isExpanded());
        fab.setImageResource(fab.isExpanded() ? R.drawable.ic_close : R.drawable.ic_add);
    }

    private boolean back() {
        if (HomeNavigation.getInstance().fragmentId(this) != R.id.homeFragment) {
            expand((FloatingActionButton)findViewById(R.id.fab));
            HomeNavigation.getInstance().pop(this);
            // TODO if (icPerson != null) icPerson.setIcon(R.drawable.ic_person);
            return true;
        } else return false;
    }

    private Fragment primary() {
        return HomeNavigation.getInstance().primary(this);
    }

    static {
        TAG = HomeActivity.class.getSimpleName();
    }
}
