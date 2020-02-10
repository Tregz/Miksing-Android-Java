package com.tregz.miksing.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseUiException;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.tregz.miksing.R;
import com.tregz.miksing.arch.note.NoteUtil;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.base.BaseActivity;
import com.tregz.miksing.base.foot.FootNavigation;
import com.tregz.miksing.base.play.PlayVideo;
import com.tregz.miksing.base.play.PlayWeb;
import com.tregz.miksing.data.DataItem;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.user.list.song.ListSongRelation;
import com.tregz.miksing.home.item.ItemFragment;
import com.tregz.miksing.home.item.ItemView;
import com.tregz.miksing.home.list.song.SongFragment;
import com.tregz.miksing.home.user.UserFragment;
import com.tregz.miksing.home.user.UserMap;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import static android.view.View.GONE;
import static com.tregz.miksing.home.HomeNavigation.SIGN_IN;

public class HomeActivity extends BaseActivity implements HomeView,
        AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout>,
        FragmentManager.OnBackStackChangedListener, SongFragment.OnItem, ItemView {

    private boolean collapsing = false;
    private final int LOCATION_CODE = 103;
    private CollapsingToolbarLayout ctl;
    private FloatingActionButton[] buttons = new FloatingActionButton[Button.values().length];
    private FootNavigation bottom;
    //private ImageView imageView;
    private HomeNavigation navigation;
    private PlayWeb webView;
    private PlayVideo videoView;

    @Override
    public void onItemClick(Song song) {
        videoView.setVisibility(GONE);
        webView.mix(song.getId()); // testing
        Log.d(TAG, "song.getId(): " + song.getId());
    }

    @Override
    public void onItemLongClick(Song song) {
        String id = song.getId();
        controller().navigate(HomeFragmentDirections.actionHomeFragmentToItemFragment(id));
        bottom.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // to delete all song data from sql table: new SongWipe(getContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        new NoteUtil(this);
        if (portrait()) {
            // Top menu
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // Hamburger menu item for start (gravity) drawer
            NavGraph graph = controller().getGraph();
            AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(graph);
            DrawerLayout layout = findViewById(R.id.drawerLayout);
            AppBarConfiguration abc = builder.setDrawerLayout(layout).build();
            ctl = findViewById(R.id.toolbar_layout);
            // Toolbar setup including auto update of the collapsing toolbar's title
            NavigationUI.setupWithNavController(ctl, toolbar, controller(), abc);
            // DrawerListener & OnNavigationItemSelectedListener
            NavigationView[] drawers = new NavigationView[Drawer.values().length];
            drawers[Drawer.RIGHT.ordinal()] = findViewById(R.id.nav_right);
            drawers[Drawer.START.ordinal()] = findViewById(R.id.nav_start);
            bottom = findViewById(R.id.bottom);
            if (layout != null) navigation = new HomeNavigation(this, bottom, layout, drawers);
            host().getChildFragmentManager().addOnBackStackChangedListener(this);

            // Panoramic height for the container of the media players
            FrameLayout players = findViewById(R.id.players);
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
            buttons[Button.FAB.ordinal()] = findViewById(R.id.fab);
            buttons[Button.FAB.ordinal()].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!back()) {
                        //controller().navigate(R.id.action_homeFragment_to_workFragment);
                        expand((FloatingActionButton)v);
                        bottom.hide();
                    }
                }
            });
            buttons[Button.CLEAR.ordinal()] = findViewById(R.id.clear_all);
            buttons[Button.CLEAR.ordinal()].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeDialog)add(new HomeDialog(HomeActivity.this))).clear();
                }
            });
            buttons[Button.SAVE.ordinal()] = findViewById(R.id.save);
            buttons[Button.SAVE.ordinal()].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeDialog)add(new HomeDialog(HomeActivity.this))).save();
                }
            });
            buttons[Button.SEARCH.ordinal()] = findViewById(R.id.web_search);
            buttons[Button.SEARCH.ordinal()].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add(new HomeDialog(HomeActivity.this, webView));
                }
            });
            // Scroll listener, to show/hide options while collapsing
            ((AppBarLayout)findViewById(R.id.app_bar)).addOnOffsetChangedListener(this);
        }

        // Stream video player
        webView = findViewById(R.id.webview);
        // Stock video player
        videoView = findViewById(R.id.video_1);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start(); // auto play
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start(); // loop
            }
        });
        String anim = "anim/Miksing_Logo-Animated.mp4";
        task(anim).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                videoView.setVideoURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.getMessage() != null) toast(e.getMessage());
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.load("'5-q3meXJ6W4'"); // testing
                //webView.setListing("['5-q3meXJ6W4']");
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        break;
                }
                return true;
            }
        });

        // Check Google Map permission
        if (!check(Manifest.permission.ACCESS_FINE_LOCATION)) {
            String[] permissions = new String[] { Manifest.permission.ACCESS_FINE_LOCATION };
            ActivityCompat.requestPermissions(this, permissions, LOCATION_CODE);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: if (!back()) navigation.toggle(GravityCompat.START);
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
                 FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                 if (user != null) {
                     Log.d(TAG, "set UID " + user.getUid());
                     PrefShared.getInstance(this).setUid(user.getUid());
                     PrefShared.getInstance(this).setUsername(user.getDisplayName());
                     PrefShared.getInstance(this).setEmail(user.getEmail());
                     // Retrieve fcm token for testing (result printed to Logcat)
                     new NoteUtil(this);
                 }
                 navigation.update();
             } else {
                 IdpResponse response = IdpResponse.fromResultIntent(data);
                 if (response != null) {
                     FirebaseUiException exception = response.getError();
                     if (exception != null) {
                         if (exception.getErrorCode() == ErrorCodes.NO_NETWORK)
                             snack(getString(R.string.no_internet_connection));
                         if (exception.getMessage() != null) Log.e(TAG, exception.getMessage());
                     }
                 }
             }
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onBackStackChanged() {
        if (portrait() && root()) {
            expand((FloatingActionButton)findViewById(R.id.fab));
            ((FootNavigation)findViewById(R.id.bottom)).show();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d(TAG, "OffsetChanged " + verticalOffset); // TODO: adjust for device sizes
        if (portrait()) {
            if (verticalOffset < -300) { if (!collapsing) collapsing = true; }
            else if (collapsing) collapsing = false;
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
        }
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
        if (primary instanceof ItemFragment) ((ItemFragment) primary).clear();
    }

    @Override
    public void onFillItemDetails(DataItem item) {
        Fragment primary = primary();
        if (primary instanceof ItemFragment) ((ItemFragment) primary).fill(item);
    }

    @Override
    public void onSaveItem() {
        Fragment primary = primary();
        if (primary instanceof ItemFragment) ((ItemFragment) primary).save();
        else if (primary instanceof HomeFragment) ((HomeFragment) primary).save();
    }

    @Override
    public void onSaved() {
        back();
    }

    @Override
    public void release(Date at) {
        Fragment primary = primary();
        if (primary instanceof ItemFragment) ((ItemFragment) primary).release(at);
    }

    @Override
    public void search(String query) {
        Fragment primary = primary();
        if (primary instanceof HomeFragment) ((HomeFragment) primary).search(query);
    }

    @Override
    public void search(boolean focused) {
        ViewGroup.LayoutParams toolbarParams = ctl.getLayoutParams();
        int largeToolbar = (int) getResources().getDimension(R.dimen.large_toolbar_height);
        int normalToolbar = (int) getResources().getDimension(R.dimen.normal_toolbar_height);
        toolbarParams.height = focused ? normalToolbar : largeToolbar;
        ctl.setLayoutParams(toolbarParams);
        FrameLayout header = findViewById(R.id.header);
        ViewGroup.LayoutParams headerParams = header.getLayoutParams();
        int normalHeader = (int) getResources().getDimension(R.dimen.normal_header_height);
        headerParams.height = focused ? 0 : normalHeader;
        header.setLayoutParams(headerParams);
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

    public void setPlaylist(List<ListSongRelation> relations) {
        StringBuilder builder = new StringBuilder();
        for (ListSongRelation relation : relations) {
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

    private Task<Uri> task(String path) {
        return FirebaseStorage.getInstance().getReference().child(path).getDownloadUrl();
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

    enum Drawer {
        START,
        RIGHT
    }

    private enum Button {
        FAB,
        CLEAR,
        SEARCH,
        SAVE
    }

    static {
        TAG = HomeActivity.class.getSimpleName();
        PRIMARY = R.id.home_fragment;
    }
}
