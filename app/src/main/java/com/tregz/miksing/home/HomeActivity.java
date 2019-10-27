package com.tregz.miksing.home;

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

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class HomeActivity extends BaseActivity implements HomeView,
        AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout> {

    private FloatingActionButton fab;
    private VideoView video;
    private PlayVideo webView;
    private boolean collapsing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ((AppBarLayout)findViewById(R.id.app_bar)).addOnOffsetChangedListener(this);

        /* Image viewer */
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

        // Container for media players
        FrameLayout frame = findViewById(R.id.players);
        LinearLayout.LayoutParams frameParams = (LinearLayout.LayoutParams) frame.getLayoutParams();
        frameParams.height = panoramic();
        frame.setLayoutParams(frameParams);

        /* Video player */
        video = findViewById(R.id.video_1);
        video.setMediaController(new MediaController(this));
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start(); // auto play
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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
                video.setVideoURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.getMessage() != null) toast(e.getMessage());
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.setVisibility(View.GONE);
                webView.load("5-q3meXJ6W4"); // testing
            }
        });

        // Web video player
        webView = findViewById(R.id.webview);

        // Listeners
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
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
                ((HomeDialog)add(new HomeDialog(HomeActivity.this))).clear();
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeDialog)add(new HomeDialog(HomeActivity.this))).save();
            }
        });
        findViewById(R.id.web_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(new HomeDialog(webView));
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
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset < -175) { if (!collapsing) collapsing = true; }
        else if (collapsing) collapsing = false;
        LinearLayout dial = findViewById(R.id.dial);
        if (collapsing) {
            if (dial.getVisibility() == View.VISIBLE) dial.setVisibility(View.INVISIBLE);
        } else if (dial.getVisibility() == View.INVISIBLE && fab.isExpanded())
            dial.setVisibility(View.VISIBLE);
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
        if (primary instanceof WorkFragment) ((WorkFragment) primary).save();
    }

    @Override
    public void onSaved() {
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

    private int panoramic() {
        float top = getResources().getDimension(R.dimen.material_toolbar_height_with_mini_gap);
        return ((int)(getResources().getDisplayMetrics().widthPixels * 0.5625)) + (int)top;
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
