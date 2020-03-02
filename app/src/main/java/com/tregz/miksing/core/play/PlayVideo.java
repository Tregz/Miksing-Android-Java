package com.tregz.miksing.core.play;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.tregz.miksing.home.HomeView;


public class PlayVideo extends VideoView implements
        MediaPlayer.OnPreparedListener,
        OnFailureListener,
        OnSuccessListener<Uri>,
        View.OnClickListener,
        View.OnTouchListener {
    private String TAG = PlayVideo.class.getSimpleName();

    private HomeView listener;

    public PlayVideo(Context context) {
        super(context);
    }

    public PlayVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {
        listener.load("5-q3meXJ6W4");
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (e.getMessage() != null)
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
        mp.start(); // auto play

    }

    @Override
    public void onSuccess(Uri uri) {
        setVideoURI(uri);
    }

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

    @Override
    public boolean performClick() {
        hide();
        super.performClick();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        setMeasuredDimension(width, width * 9 / 16);
    }

    public void hide() {
        if (getVisibility() != GONE)
            setVisibility(GONE);
    }

    public void init(HomeView listener) {
        this.listener = listener;
        setOnPreparedListener(this);
        String anim = "anim/Miksing_Logo-Animated.mp4";
        Task<Uri> task = FirebaseStorage.getInstance().getReference().child(anim).getDownloadUrl();
        task.addOnSuccessListener(this).addOnFailureListener(this);
        setOnClickListener(this);
        setOnTouchListener(this);
    }
}