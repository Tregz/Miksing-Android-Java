package com.tregz.miksing.home;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseActivity;
import com.tregz.miksing.home.work.WorkFragment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class HomeActivity extends BaseActivity implements HomeView {

    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

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
        LinearLayout.LayoutParams videoParams = (LinearLayout.LayoutParams) video.getLayoutParams();
        videoParams.height = panoramic();
        video.setLayoutParams(videoParams);

        String path = "anim/Miksing_Logo-Animated.mp4";
        Task<Uri> task = FirebaseStorage.getInstance().getReference().child(path).getDownloadUrl();
        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
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
