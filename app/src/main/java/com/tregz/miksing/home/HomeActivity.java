package com.tregz.miksing.home;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseActivity;
import com.tregz.miksing.base.play.PlayVideo;
import com.tregz.miksing.data.work.Work;
import com.tregz.miksing.home.work.WorkFragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends BaseActivity implements HomeView,
        AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout> {

    private FloatingActionButton[] buttons = new FloatingActionButton[4];
    private VideoView videoView;
    private ImageView imageView;
    private PlayVideo webView;
    private boolean collapsing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //NavigationUI.setupWithNavController((Toolbar)findViewById(R.id.toolbar),
        // HomeNavigation.getInstance().controller(this))
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        AppBarLayout appBar = findViewById(R.id.app_bar);
        appBar.addOnOffsetChangedListener(this);

        /* testing
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int gap = TypedValue.complexToDimensionPixelSize(tv.data, dm);
            int gap = (int) getResources().getDimension(R.dimen.small_gap);
            int top = (int) getResources().getDimension(R.dimen.app_bar_layout_height) + gap;
            panoramic(appBar, top);
        } */

        //BottomNavigationView bottom = findViewById(R.id.bottom);
        /* Set navigation on bottom view
        NavigationUI.setupWithNavController(bottom, HomeNavigation.getInstance().controller(this));
        */

        // Container for media players
        FrameLayout frame = findViewById(R.id.players);
        panoramic(frame, 0);

        /* Video player */
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
        String path1 = "anim/Miksing_Logo-Animated.mp4";
        Task<Uri> t1 = FirebaseStorage.getInstance().getReference().child(path1).getDownloadUrl();
        t1.addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                videoView.setVisibility(View.GONE);
                webView.load("5-q3meXJ6W4"); // testing
            }
        });

        // Web video player
        webView = findViewById(R.id.webview);

        if (portrait()) {
            /* Image viewer */
            imageView = findViewById(R.id.image_1);
            String path2 = "draw/Cshawi-logo-mini.png";
            Task<Uri> t2 = FirebaseStorage.getInstance().getReference().child(path2).getDownloadUrl();
            t2.addOnSuccessListener(new OnSuccessListener<Uri>() {
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

            // Listeners
            buttons[0] = findViewById(R.id.fab);
            buttons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!back()) {
                        navigate(R.id.action_homeFragment_to_workFragment);
                        expand((FloatingActionButton)v);
                    }
                }
            });
            buttons[1] = findViewById(R.id.clear_all);
            buttons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeDialog)add(new HomeDialog(HomeActivity.this))).clear();
                }
            });
            buttons[2] = findViewById(R.id.save);
            buttons[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeDialog)add(new HomeDialog(HomeActivity.this))).save();
                }
            });
            buttons[3] = findViewById(R.id.web_search);
            buttons[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add(new HomeDialog(webView));
                }
            });
        }
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
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d(TAG, "OffsetChanged " + verticalOffset);
        if (portrait()) {
            if (verticalOffset < -300) { if (!collapsing) collapsing = true; }
            else if (collapsing) collapsing = false;
            if (collapsing) {
                if (buttons[0].getVisibility() == View.VISIBLE) {
                    imageView.setVisibility(View.INVISIBLE);
                    if (buttons[0].isExpanded()) for (FloatingActionButton fab : buttons) fab.hide();
                    else buttons[0].hide();
                }
            } else if (buttons[0].getVisibility() == View.GONE) {
                imageView.setVisibility(View.VISIBLE);
                if (buttons[0].isExpanded()) for (FloatingActionButton fab : buttons) fab.show();
                else buttons[0].show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onClearItemDetails() {
        Fragment primary = primary();
        if (primary instanceof WorkFragment) ((WorkFragment) primary).clear();
    }

    @Override
    public void onFillItemDetails(Work work) {
        Fragment primary = primary();
        if (primary instanceof WorkFragment) ((WorkFragment) primary).fill(work);
    }

    @Override
    public void onSaveItem() {
        Fragment primary = primary();
        Log.d(TAG, "onSaveItem " + (primary.getClass().getSimpleName()));
        if (primary instanceof WorkFragment) ((WorkFragment) primary).save();
    }

    @Override
    public void onSaved() {
        back();
    }

    private void navigate(int action) {
        HomeNavigation.getInstance().navigate(this, action);
    }

    private void expand(FloatingActionButton fab) {
        fab.setExpanded(!fab.isExpanded());
        fab.setImageResource(fab.isExpanded() ? R.drawable.ic_close : R.drawable.ic_add);
    }

    private void panoramic(View view, int top) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ((int)(getResources().getDisplayMetrics().widthPixels * 0.5625)) + top;
        view.setLayoutParams(params);
    }

    private boolean back() {
        if (HomeNavigation.getInstance().fragmentId(this) != R.id.homeFragment) {
            expand((FloatingActionButton)findViewById(R.id.fab));
            HomeNavigation.getInstance().pop(this);
            // TODO if (icPerson != null) icPerson.setIcon(R.drawable.ic_person);
            return true;
        } else return false;
    }

    private boolean portrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private Fragment primary() {
        return HomeNavigation.getInstance().primary(this);
    }

    static {
        TAG = HomeActivity.class.getSimpleName();
    }
}
