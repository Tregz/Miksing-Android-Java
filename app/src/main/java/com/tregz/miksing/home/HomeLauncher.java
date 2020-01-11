package com.tregz.miksing.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseActivity;

public class HomeLauncher extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        startActivity(new Intent(this, HomeActivity.class));
    }
}
