package com.tregz.miksing.home;

import android.app.Activity;
import android.content.Intent;
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
import com.tregz.miksing.data.item.Item;
import com.tregz.miksing.home.item.ItemFragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavGraph;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static android.view.View.GONE;
import static com.tregz.miksing.home.HomeNavigation.SIGN_IN;

public class HomeActivity extends BaseActivity implements HomeView,
        AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout>,
        FragmentManager.OnBackStackChangedListener {

    private boolean collapsing = false;
    private FloatingActionButton[] buttons = new FloatingActionButton[Button.values().length];
    private ImageView imageView;
    private HomeNavigation navigation;
    private PlayVideo webView;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        new NoteUtil().fcmTokenLog();
        if (portrait()) {
            // Top menu
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // Hamburger menu item for start (gravity) drawer
            NavGraph graph = controller().getGraph();
            AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(graph);
            DrawerLayout dl = findViewById(R.id.drawerLayout);
            AppBarConfiguration abc = builder.setDrawerLayout(dl).build();
            CollapsingToolbarLayout ctl = findViewById(R.id.toolbar_layout);
            // Setup including auto update of the collapsing toolbar's title
            NavigationUI.setupWithNavController(ctl, toolbar, controller(), abc);
            // DrawerListener & OnNavigationItemSelectedListener
            NavigationView[] drawers = new NavigationView[Drawer.values().length];
            drawers[Drawer.RIGHT.ordinal()] = findViewById(R.id.nav_right);
            drawers[Drawer.START.ordinal()] = findViewById(R.id.nav_start);
            if (dl != null) navigation = new HomeNavigation(this, dl, drawers);
            host().getChildFragmentManager().addOnBackStackChangedListener(this);

            // Panoramic height for the container of the media players
            FrameLayout players = findViewById(R.id.players);
            ViewGroup.LayoutParams params = players.getLayoutParams();
            params.height = (int) (getResources().getDisplayMetrics().widthPixels * 0.5625);
            players.setLayoutParams(params);

            // Content menu's floating action buttons and logo image
            imageView = findViewById(R.id.image_1);
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
            });
            buttons[Button.FAB.ordinal()] = findViewById(R.id.fab);
            buttons[Button.FAB.ordinal()].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!back()) {
                        controller().navigate(R.id.action_homeFragment_to_workFragment);
                        expand((FloatingActionButton)v);
                        ((FootNavigation)findViewById(R.id.bottom)).hide();
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
        videoView.setMediaController(new MediaController(this));
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
                videoView.setVisibility(GONE);
                webView.load("5-q3meXJ6W4"); // testing
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (portrait()) getMenuInflater().inflate(R.menu.toolbar_home, menu);
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
                     PrefShared.getInstance(this).setUsername(user.getDisplayName());
                     PrefShared.getInstance(this).setEmail(user.getEmail());
                     // Retrieve fcm token for testing (result printed to Logcat)
                     new NoteUtil().fcmTokenLog();
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
        Log.d(TAG, "OffsetChanged " + verticalOffset);
        if (portrait()) {
            if (verticalOffset < -300) { if (!collapsing) collapsing = true; }
            else if (collapsing) collapsing = false;
            if (collapsing) {
                if (buttons[0].getVisibility() == View.VISIBLE) {
                    imageView.setVisibility(View.INVISIBLE);
                    if (buttons[0].isExpanded())
                        for (FloatingActionButton fab : buttons) fab.hide();
                    else buttons[0].hide();
                }
            } else if (buttons[0].getVisibility() == GONE) {
                imageView.setVisibility(View.VISIBLE);
                if (buttons[0].isExpanded())
                    for (FloatingActionButton fab : buttons) fab.show();
                else buttons[0].show();
            }
        }
    }

    @Override
    public void commit(int container, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().add(container, fragment, tag).commit();
    }

    @Override
    public void remove(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onClearItemDetails() {
        Fragment primary = primary();
        if (primary instanceof ItemFragment) ((ItemFragment) primary).clear();
    }

    @Override
    public void onFillItemDetails(Item item) {
        Fragment primary = primary();
        if (primary instanceof ItemFragment) ((ItemFragment) primary).fill(item);
    }

    @Override
    public void onSaveItem() {
        Fragment primary = primary();
        Log.d(TAG, "onSaveItem " + (primary.getClass().getSimpleName()));
        if (primary instanceof ItemFragment) ((ItemFragment) primary).save();
    }

    @Override
    public void onSaved() {
        back();
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
        PRIMARY = R.id.homeFragment;
    }
}
