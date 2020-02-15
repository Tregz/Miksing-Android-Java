package com.tregz.miksing.core.play;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;


public class PlayVideo extends VideoView {

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
    public boolean performClick() {
        super.performClick();
        setVisibility(GONE);
        return true;
    }
}