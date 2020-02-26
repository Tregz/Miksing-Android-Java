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
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.arch.note.NoteUtil;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.base.BaseActivity;
import com.tregz.miksing.base.foot.FootNavigation;
import com.tregz.miksing.base.foot.FootScroll;
import com.tregz.miksing.core.play.PlayVideo;
import com.tregz.miksing.core.play.PlayWeb;
import com.tregz.miksing.data.DataObject;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.tube.TubeListener;
import com.tregz.miksing.data.tube.song.TubeSongRelation;
import com.tregz.miksing.data.user.UserListener;
import com.tregz.miksing.databinding.ActivityHomeBinding;
import com.tregz.miksing.home.edit.song.SongEditFragment;
import com.tregz.miksing.home.edit.song.SongEditView;
import com.tregz.miksing.home.list.song.SongListFragment;
import com.tregz.miksing.home.list.tube.TubeListFragment;
import com.tregz.miksing.home.warn.WarnClear;
import com.tregz.miksing.home.warn.WarnFetch;
import com.tregz.miksing.home.warn.WarnPaste;
import com.tregz.miksing.home.warn.WarnPlaylist;
import com.tregz.miksing.home.warn.WarnScore;
import com.tregz.miksing.home.user.UserFragment;
import com.tregz.miksing.home.user.UserMap;

import android.util.Log;
import android.view.Gravity;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static com.tregz.miksing.arch.auth.AuthLogin.SIGN_IN;

public class HomeActivity extends BaseActivity implements
        AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout>,
        FragmentManager.OnBackStackChangedListener, HomeView, SongEditView, SongListFragment.OnItem,
        TubeListFragment.OnItem {

    private boolean collapsing = false;
    private final int LOCATION_CODE = 103;
    private String prepareListTitle = null;

    private final String APERO = "-M0uHaGKccSZf3yFQl1P";
    private final String BISTRO = "-M121RV6XpDcLssTFj_3";
    private final String CLUB = "-M121Sw09oyK7jePDiAv";
    private final String DEFAULT = "-M0A1B6LQlpJpgdbkYyx";
    private final List<String> defaultPlaylists = Arrays.asList(DEFAULT, APERO, BISTRO, CLUB);

    @Override
    public String getPrepareListTitle() {
        if (prepareListTitle != null) return prepareListTitle;
        else return PrefShared.getInstance(this).getUid() + "-Prepare";
    }

    private ActivityHomeBinding binding;
    private CollapsingToolbarLayout toolbarLayout;
    //private FloatingActionButton[] buttons = new FloatingActionButton[Button.values().length];
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
        if (binding.contentHome != null) binding.contentHome.bottom.hide();
    }

    @Override
    public void onItemClick(Tube tube) {
        Fragment primary = primary();
        navigation.toggle(Gravity.START);
        if (primary instanceof HomeFragment) ((HomeFragment) primary).prepare(tube.getId());
        prepareListTitle = tube.getName(this);
    }

    @Override
    public void onItemLongClick(Tube tube) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // to delete all song data from sql table: new SongWipe(getContext());
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new NoteUtil(this);
        if (binding.contentHome != null) {
            // Top menu
            Toolbar toolbar = binding.contentHome.appBar.toolbar;
            setSupportActionBar(toolbar);
            // Hamburger menu item for start (gravity) drawer
            NavGraph graph = controller().getGraph();
            AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(graph);
            DrawerLayout layout = binding.drawerLayout;
            AppBarConfiguration abc = builder.setDrawerLayout(layout).build();
            toolbarLayout = binding.contentHome.appBar.toolbarLayout;
            // Toolbar setup including auto update of the collapsing toolbar's title
            NavigationUI.setupWithNavController(toolbarLayout, toolbar, controller(), abc);
            // DrawerListener & OnNavigationItemSelectedListener
            NavigationView[] drawers = new NavigationView[Drawer.values().length];
            drawers[Drawer.RIGHT.ordinal()] = binding.navRight;
            drawers[Drawer.START.ordinal()] = binding.navStart;
            final FootNavigation bottom = binding.contentHome.bottom;
            if (layout != null) navigation = new HomeNavigation(this, bottom, layout, drawers);
            host().getChildFragmentManager().addOnBackStackChangedListener(this);

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
                            bottom.hide();
                        } else {
                            expand((FloatingActionButton) v);
                            bottom.show(false);
                        }
                    }
                }
            });
            binding.contentHome.clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new WarnClear().show(getSupportFragmentManager(), WarnClear.TAG);
                }
            });
            binding.contentHome.fetch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new WarnFetch().show(getSupportFragmentManager(), WarnFetch.TAG);
                }
            });
            binding.contentHome.paste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WarnPaste warning = WarnPaste.newInstance(prepareListTitle);
                    warning.show(getSupportFragmentManager(), WarnScore.TAG);
                }
            });
            binding.contentHome.score.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WarnScore warning = WarnScore.newInstance(prepareListTitle);
                    warning.show(getSupportFragmentManager(), WarnScore.TAG);
                }
            });
            // Scroll listener, to show/hide options while collapsing
            ((AppBarLayout) binding.contentHome.appBar.getRoot()).addOnOffsetChangedListener(this);

            videoView = binding.contentHome.appBar.video1;
            webView = binding.contentHome.appBar.webview;
        } else {
            videoView = binding.video1;
            webView = binding.webview;
        }

        // Stream video player
        // Stock video player
        if (videoView != null) {
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
        }
        String anim = "anim/Miksing_Logo-Animated.mp4";
        task(anim).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // TODO videoView.setVideoURI(uri);
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
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, LOCATION_CODE);
        }

        FirebaseUser firebaseUser = AuthUtil.user();
        if (firebaseUser != null) {
            // Start observing user's cloud data
            new UserListener(this, firebaseUser.getUid());
        } else {
            // Start observing default playlists
            for (String playlistId : defaultPlaylists) {
                new TubeListener(this, null, playlistId);
            }
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
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    Log.d(TAG, "set UID " + firebaseUser.getUid());
                    PrefShared.getInstance(this).setUid(firebaseUser.getUid());
                    PrefShared.getInstance(this).setUsername(firebaseUser.getDisplayName());
                    PrefShared.getInstance(this).setEmail(firebaseUser.getEmail());
                    new UserListener(this, firebaseUser.getUid());
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
        if (binding.contentHome != null && root()) {
            expand(binding.contentHome.fab);
            binding.contentHome.bottom.show(true);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d(TAG, "OffsetChanged " + verticalOffset); // TODO: adjust for device sizes
        if (portrait()) {
            if (verticalOffset < -300) {
                if (!collapsing) collapsing = true;
            } else if (collapsing) collapsing = false;
            /* if (collapsing) {
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
            } */
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
        FETCH,
        PASTE,
        SCORE
    }

    static {
        TAG = HomeActivity.class.getSimpleName();
        PRIMARY = R.id.home_fragment;
    }
}
