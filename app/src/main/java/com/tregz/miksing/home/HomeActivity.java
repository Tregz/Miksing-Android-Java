package com.tregz.miksing.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.tregz.miksing.R;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.arch.note.NoteUtil;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.base.BaseActivity;
import com.tregz.miksing.base.foot.FootScroll;
import com.tregz.miksing.core.play.PlayWeb;
import com.tregz.miksing.data.DataObject;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.tube.song.TubeSongRelation;
import com.tregz.miksing.data.user.UserListener;
import com.tregz.miksing.data.user.tube.UserTubeDelete;
import com.tregz.miksing.data.user.tube.UserTubeRelation;
import com.tregz.miksing.databinding.ActivityHomeBinding;
import com.tregz.miksing.home.edit.song.SongEditFragment;
import com.tregz.miksing.home.edit.song.SongEditView;
import com.tregz.miksing.home.list.song.SongListFragment;
import com.tregz.miksing.home.list.song.plan.SongPlanFragment;
import com.tregz.miksing.home.list.tube.TubeListFragment;
import com.tregz.miksing.home.warn.WarnFetch;
import com.tregz.miksing.home.warn.WarnPaste;
import com.tregz.miksing.home.warn.WarnPlaylist;
import com.tregz.miksing.home.warn.WarnScore;
import com.tregz.miksing.home.user.UserFragment;
import com.tregz.miksing.home.user.UserMap;
import com.tregz.miksing.home.warn.WarnWreck;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavGraph;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Date;
import java.util.List;

import static com.tregz.miksing.arch.auth.AuthLogin.SIGN_IN;

public class HomeActivity extends BaseActivity implements
        AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout>,
        FragmentManager.OnBackStackChangedListener, HomeView, SongEditView, SongListFragment.OnItem,
        TubeListFragment.OnItem {

    //private boolean collapsing = false;
    private final int LOCATION_CODE = 103;

    private ActivityHomeBinding binding;
    private CollapsingToolbarLayout toolbarLayout;
    //private ImageView imageView;
    private HomeNavigation navigation;
    private PlayWeb webView;
    private Handler countdown;

    @Override
    public void onItemClick(Song song) {
        next(song.getId());
        Log.d(TAG, "song.getId(): " + song.getId());
    }

    @Override
    public void onItemLongClick(Song song) {
        Bundle bundle = new Bundle();
        bundle.putString("id", song.getId());
        controller().navigate(R.id.action_home_fragment_to_item_fragment, bundle);
        if (binding.contentHome != null) binding.contentHome.bottom.hide();
        //if (videoView != null) videoView.hide();
    }

    @Override
    public void onItemClick(UserTubeRelation relation) {
        Fragment primary = primary();
        navigation.toggle(Gravity.START);
        if (primary instanceof HomeFragment) ((HomeFragment) primary).prepare(relation);
    }

    @Override
    public void onItemLongClick(UserTubeRelation relation) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // to delete all song data from sql table: new SongWipe(getContext());
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fullscreen();

        new NoteUtil();
        if (binding.contentHome != null) {
            // Top menu
            Toolbar toolbar = binding.contentHome.appBar.toolbar;
            setSupportActionBar(toolbar);
            // Hamburger menu item for start (gravity) drawer
            NavGraph graph = controller().getGraph();
            AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(graph);
            DrawerLayout layout = binding.drawerLayout;
            AppBarConfiguration abc = builder.setOpenableLayout(layout).build();
            toolbarLayout = binding.contentHome.appBar.toolbarLayout;
            // Toolbar setup including auto update of the collapsing toolbar's title
            NavigationUI.setupWithNavController(toolbarLayout, toolbar, controller(), abc);
            // DrawerListener & OnNavigationItemSelectedListener
            if (layout != null) navigation = new HomeNavigation(this, binding);
            host(HOST).getChildFragmentManager().addOnBackStackChangedListener(this);

            // Panoramic height for the container of the media players
            FrameLayout players = binding.contentHome.appBar.players;
            ViewGroup.LayoutParams params = players.getLayoutParams();
            params.height = (int) (getResources().getDisplayMetrics().widthPixels * 0.5625);
            players.setLayoutParams(params);

            // Content menu's floating action buttons and logo image
            /* imageView = findViewById(R.id.image_1);
            String logo = "draw/Cshawi-logo-mini.png";
            task(logo).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    image(uri, R.id.image_1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e.getMessage() != null) toast(e.getMessage());
                }
            }); */
            binding.contentHome.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if (!back()) {
                    if (v instanceof FloatingActionButton) {
                        if (!((FloatingActionButton) v).isExpanded()) {
                            //controller().navigate(R.id.action_homeFragment_to_workFragment);
                            expand((FloatingActionButton) v);
                            binding.contentHome.bottom.hide();
                        } else {
                            expand((FloatingActionButton) v);
                            binding.contentHome.bottom.show(false);
                        }
                    }
                }
            });
            /* binding.contentHome.clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new WarnClear().show(getSupportFragmentManager(), WarnClear.TAG);
                }
            }); */
            binding.contentHome.fetch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new WarnFetch().show(getSupportFragmentManager(), WarnFetch.TAG);
                }
            });
            binding.contentHome.paste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = SongPlanFragment.relation.tube.getName(HomeActivity.this);
                    WarnPaste warning = WarnPaste.newInstance(name);
                    warning.show(getSupportFragmentManager(), WarnScore.TAG);
                }
            });
            binding.contentHome.wreck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WarnWreck warning = WarnWreck.newInstance(SongPlanFragment.relation.tube);
                    warning.show(getSupportFragmentManager(), WarnWreck.TAG);
                }
            });
            // Scroll listener, to show/hide options while collapsing
            //((AppBarLayout) binding.contentHome.appBar.getRoot()).addOnOffsetChangedListener(this);

            binding.contentHome.appBar.video1.init(this);
            webView = binding.contentHome.appBar.webview;
        } else {
            if (binding.video1 != null) binding.video1.init(this);
            webView = binding.webview;
        }

        if (!PlayWeb.videoId.equals("''")) load(PlayWeb.videoId);

        // Check Google Map permission
        if (!check(Manifest.permission.ACCESS_FINE_LOCATION)) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, LOCATION_CODE);
        }

        // Start observing user's cloud data or default user's playlists
        FirebaseUser firebaseUser = AuthUtil.user();
        String id = firebaseUser != null ? firebaseUser.getUid() : PrefShared.defaultUser;
        new UserListener(this, id);
    }

    public void load(String id) {
        if (binding.contentHome != null) binding.contentHome.appBar.video1.hide();
        else if (binding.video1 != null) binding.video1.hide();
        webView.load(id);
    }

    @Override
    public void next(String id) {
        if (binding.contentHome != null) binding.contentHome.appBar.video1.hide();
        else if (binding.video1 != null) binding.video1.hide();
        webView.mix(id);
    }

    private void countdown() {
        if (countdown != null) countdown.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.countdown();
                countdown();
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (countdown == null) {
            countdown = new Handler(Looper.getMainLooper());
            countdown();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        countdown = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (portrait()) {
            getMenuInflater().inflate(R.menu.toolbar_home, menu);
            new HomeSearch(this, (SearchView) menu.findItem(R.id.search).getActionView());
        }
        return true;
    }

    @Override
    public void onDrawerStartOpened() {
        TubeListFragment tube = playlist();
        if (tube != null) tube.sort();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!back()) navigation.toggle(GravityCompat.START);
                return true;
            case R.id.login:
                if (navigation != null) navigation.toggle(GravityCompat.END);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                AuthUtil.onUserLogin(this);
                navigation.update();
            } else if (!AuthUtil.hasNetwork(data)) snack(getString(R.string.no_internet));
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onBackStackChanged() {
        if (binding.contentHome != null && root()) {
            expand(binding.contentHome.fab);
            binding.contentHome.bottom.show(true);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d(TAG, "OffsetChanged " + verticalOffset); // TODO: adjust for device sizes
        /* if (portrait()) {
            if (verticalOffset < -300) {
                if (!collapsing) collapsing = true;
            } else if (collapsing) collapsing = false;
            if (collapsing) {
                if (buttons[0].getVisibility() == View.VISIBLE) {
                    //imageView.setVisibility(View.INVISIBLE);
                    if (buttons[0].isExpanded())
                        for (FloatingActionButton fab : buttons) fab.hide();
                    else buttons[0].hide();
                }
            } else if (buttons[0].getVisibility() == GONE) {
                //imageView.setVisibility(View.VISIBLE);
                if (buttons[0].isExpanded())
                    for (FloatingActionButton fab : buttons) fab.show();
                else buttons[0].show();
            }
        } */
    }

    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] asks, @NonNull int[] grant) {
        super.onRequestPermissionsResult(code, asks, grant);
        if (code == LOCATION_CODE) {
            if (grant.length > 0 && grant[0] == PackageManager.PERMISSION_GRANTED) {
                toast("Google map location enable");
            }
        }
    }

    @Override
    public void onClearItemDetails() {
        Fragment primary = primary();
        if (primary instanceof SongEditFragment) ((SongEditFragment) primary).clear();
    }

    @Override
    public void onClearPlaylist() {
        // TODO
    }

    @Override
    public void onFillItemDetails(DataObject item) {
        Fragment primary = primary();
        if (primary instanceof SongEditFragment) ((SongEditFragment) primary).fill(item);
    }

    @Override
    public void onHttpRequestResult(List<Song> list) {
        WarnPlaylist.newInstance(list).show(getSupportFragmentManager(), WarnPlaylist.TAG);
    }

    @Override
    public void onPastePlaylist(String name) {
        Fragment primary = primary();
        if (primary instanceof SongEditFragment) ((SongEditFragment) primary).save();
        else if (primary instanceof HomeFragment) ((HomeFragment) primary).save(name, true);
    }

    @Override
    public void onSaveItem(String name) {
        Fragment primary = primary();
        if (primary instanceof SongEditFragment) ((SongEditFragment) primary).save();
        else if (primary instanceof HomeFragment) ((HomeFragment) primary).save(name, false);
    }

    @Override
    public void onSaved() {
        back();
    }

    @Override
    public void onWreck(Tube tube) {
        new UserTubeDelete(this, PrefShared.getInstance(this).getUid(), tube.getId());
    }

    @Override
    public PlayWeb getWebView() {
        return webView;
    }

    @Override
    public void release(Date at) {
        Fragment primary = primary();
        if (primary instanceof SongEditFragment) ((SongEditFragment) primary).release(at);
    }

    @Override
    public void search(String query) {
        Fragment primary = primary();
        if (primary instanceof HomeFragment) ((HomeFragment) primary).search(query);
    }

    @Override
    public void search(boolean focused) {
        if (binding.contentHome != null) {
            ViewGroup.LayoutParams toolbarParams = toolbarLayout.getLayoutParams();
            int largeToolbar = (int) getResources().getDimension(R.dimen.large_toolbar_height);
            int normalToolbar = (int) getResources().getDimension(R.dimen.normal_toolbar_height);
            toolbarParams.height = focused ? normalToolbar : largeToolbar;
            toolbarLayout.setLayoutParams(toolbarParams);
            FrameLayout header = binding.contentHome.header;
            ViewGroup.LayoutParams headerParams = header.getLayoutParams();
            int normalHeader = (int) getResources().getDimension(R.dimen.normal_header_height);
            headerParams.height = focused ? 0 : normalHeader;
            header.setLayoutParams(headerParams);
        }
    }

    @Override
    public void sort() {
        Fragment primary = primary();
        if (primary instanceof HomeFragment) ((HomeFragment) primary).sort();
    }

    @Override
    public UserFragment userFragment() {
        FragmentManager fm = manager(R.id.nav_user_fragment);
        return fm != null ? (UserFragment) fm.getPrimaryNavigationFragment() : null;
    }

    @Override
    public UserMap areaFragment() {
        FragmentManager fm = manager(R.id.nav_area_fragment);
        return fm != null ? (UserMap) fm.getPrimaryNavigationFragment() : null;
    }

    public void setFabVisibility(boolean show) {
        if (binding.contentHome != null) {
            FloatingActionButton fab = binding.contentHome.fab;
            if (show) {
                if (FootScroll.state == FootScroll.State.UP)
                    fab.setVisibility(View.INVISIBLE);
                else {
                    fab.setVisibility(View.VISIBLE);
                    fab.show();
                }
            } else {
                if (fab.isExpanded()) expand(fab);
                fab.setVisibility(View.GONE);
            }
        }
    }

    public void setPlaylist(List<TubeSongRelation> relations) {
        StringBuilder builder = new StringBuilder();
        for (TubeSongRelation relation : relations) {
            if (builder.length() > 0) builder.append(",");
            builder.append("'");
            builder.append(relation.song.getId());
            builder.append("'");
        }
        String playlist = "[" + builder.toString() + "]";
        Log.d(TAG, playlist);
        webView.setListing(playlist);
    }

    private void expand(FloatingActionButton fab) {
        fab.setExpanded(!fab.isExpanded());
        fab.setImageResource(fab.isExpanded() ? R.drawable.ic_close : R.drawable.ic_add);
    }

    private boolean back() {
        if (portrait() && !root()) {
            controller().popBackStack();
            return true;
        } else return false;
    }

    private boolean portrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    static {
        TAG = HomeActivity.class.getSimpleName();
        PRIMARY = R.id.home_fragment;
    }
}
