package com.tregz.miksing.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.tregz.miksing.R;

public class HomeLauncher extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        final Intent intent = new Intent(this, HomeActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 2000);
    }
}
