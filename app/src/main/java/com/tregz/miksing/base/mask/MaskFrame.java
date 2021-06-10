package com.tregz.miksing.base.mask;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.tregz.miksing.R;

public class MaskFrame extends FrameLayout {
    private static final String TAG = MaskFrame.class.getSimpleName();
    private static final int MODE_ADD = 0;
    private static final int MODE_CLEAR = 1;
    private static final int MODE_DARKEN = 2;
    private static final int MODE_DST = 3;
    private static final int MODE_DST_ATOP = 4;
    private static final int MODE_DST_IN = 5;
    private static final int MODE_DST_OUT = 6;
    private static final int MODE_DST_OVER = 7;
    private static final int MODE_LIGHTEN = 8;
    private static final int MODE_MULTIPLY = 9;
    private static final int MODE_OVERLAY = 10;
    private static final int MODE_SCREEN = 11;
    private static final int MODE_SRC = 12;
    private static final int MODE_SRC_ATOP = 13;
    private static final int MODE_SRC_IN = 14;
    private static final int MODE_SRC_OUT = 15;
    private static final int MODE_SRC_OVER = 16;
    private static final int MODE_XOR = 17;

    private Handler MainHandler;

    @Nullable
    private Drawable DrawableMask = null;
    @Nullable
    private Bitmap FinalMask = null;

    private Paint MaskPaint = null;
    private PorterDuffXfermode PorterDuffXferMode = null;

    public MaskFrame(Context context) {
        super(context);
    }

    public MaskFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        construct(context, attrs);
    }

    public MaskFrame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        construct(context, attrs);
    }

    private void construct(Context context, AttributeSet attrs) {
        MainHandler = new Handler();
        setDrawingCacheEnabled(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null); // Only works for software layers
        MaskPaint = createPaint(false);
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedArray a = theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.MaskableLayout,
                    0, 0);
            try {
                // Load the mask if specified in xml
                initMask(loadMask(a));
                // Load the mode if specified in xml
                PorterDuffXferMode = getModeFromInteger(
                        a.getInteger(R.styleable.MaskableLayout_porterduffxfermode, 0));
                initMask(DrawableMask);
                // Check antiAlias
                if (a.getBoolean(R.styleable.MaskableLayout_anti_aliasing, false)) {
                    // Recreate paint with anti aliasing enabled
                    MaskPaint = createPaint(true);
                }
            } finally {
                if (a != null)
                    a.recycle();
            }
        } else {
            log("Couldn't load theme, mask in xml won't be loaded.");
        }
        registerMeasure();
    }

    @NonNull
    private Paint createPaint(boolean antiAliasing) {
        Paint output = new Paint(Paint.ANTI_ALIAS_FLAG);
        output.setAntiAlias(antiAliasing);
        output.setXfermode(PorterDuffXferMode);
        return output;
    }

    //Mask functions
    @Nullable
    private Drawable loadMask(@NonNull TypedArray a) {
        final int drawableResId = a.getResourceId(R.styleable.MaskableLayout_mask, -1);
        if (drawableResId == -1)
            return null;
        return AppCompatResources.getDrawable(getContext(), drawableResId);
    }

    private void initMask(@Nullable Drawable input) {
        if (input != null) {
            DrawableMask = input;
            if (DrawableMask instanceof AnimationDrawable)
                DrawableMask.setCallback(this);
        } else {
            log("Are you sure you don't want to provide a mask ?");
        }
    }

    @Nullable
    private Bitmap makeBitmapMask(@Nullable Drawable drawable) {
        if (drawable != null) {
            if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
                Bitmap mask = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                drawable.draw(canvas);
                return mask;
            } else {
                log("Can't create a mask with height 0 or width 0. Or the layout has no children and is wrap content");
                return null;
            }
        } else
            log("No bitmap mask loaded, view will NOT be masked !");
        return null;
    }

    //Once the size has changed we need to remake the mask.
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setSize(w, h);
    }

    private void setSize(int width, int height) {
        if (width > 0 && height > 0) {
            if (DrawableMask != null)
                swapBitmapMask(makeBitmapMask(DrawableMask));
        } else
            log("Width and height must be higher than 0");
    }

    //Drawing
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (FinalMask != null && MaskPaint != null) {
            MaskPaint.setXfermode(PorterDuffXferMode);
            canvas.drawBitmap(FinalMask, 0.0f, 0.0f, MaskPaint);
            MaskPaint.setXfermode(null);
        } else {
            log("Mask or paint is null ...");
        }
    }

    //Once inflated we have no height or width for the mask. Wait for the layout.
    private void registerMeasure() {
        final ViewTreeObserver treeObserver = MaskFrame.this.getViewTreeObserver();
        if (treeObserver != null && treeObserver.isAlive()) {
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver aliveObserver = treeObserver;
                    if (!aliveObserver.isAlive())
                        aliveObserver = MaskFrame.this.getViewTreeObserver();
                    if (aliveObserver != null)
                        aliveObserver.removeOnGlobalLayoutListener(this);
                    else
                        log("GlobalLayoutListener not removed as ViewTreeObserver is not valid");
                    swapBitmapMask(makeBitmapMask(DrawableMask));
                }
            });
        }
    }

    private void log(@NonNull String message) {
        Log.d(TAG, message);
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        if (dr != null) {
            initMask(dr);
            swapBitmapMask(makeBitmapMask(dr));
            invalidate();
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (who != null && what != null)
            MainHandler.postAtTime(what, when);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (who != null && what != null)
            MainHandler.removeCallbacks(what);
    }

    private void swapBitmapMask(@Nullable Bitmap newMask) {
        if (newMask != null) {
            if (FinalMask != null && !FinalMask.isRecycled())
                FinalMask.recycle();
            FinalMask = newMask;
        }
    }

    private PorterDuffXfermode getModeFromInteger(int index) {
        PorterDuff.Mode mode;
        switch (index) {
            case MODE_ADD:
                mode = PorterDuff.Mode.ADD;
                break;
            case MODE_CLEAR:
                mode = PorterDuff.Mode.CLEAR;
                break;
            case MODE_DARKEN:
                mode = PorterDuff.Mode.DARKEN;
                break;
            case MODE_DST:
                mode = PorterDuff.Mode.DST;
                break;
            case MODE_DST_ATOP:
                mode = PorterDuff.Mode.DST_ATOP;
                break;
            case MODE_DST_IN:
                mode = PorterDuff.Mode.DST_IN;
                break;
            case MODE_DST_OUT:
                mode = PorterDuff.Mode.DST_OUT;
                break;
            case MODE_DST_OVER:
                mode = PorterDuff.Mode.DST_OVER;
                break;
            case MODE_LIGHTEN:
                mode = PorterDuff.Mode.LIGHTEN;
                break;
            case MODE_MULTIPLY:
                mode = PorterDuff.Mode.MULTIPLY;
                break;
            case MODE_OVERLAY:
                mode = PorterDuff.Mode.OVERLAY;
                break;
            case MODE_SCREEN:
                mode = PorterDuff.Mode.SCREEN;
                break;
            case MODE_SRC:
                mode = PorterDuff.Mode.SRC;
                break;
            case MODE_SRC_ATOP:
                mode = PorterDuff.Mode.SRC_ATOP;
                break;
            case MODE_SRC_IN:
                mode = PorterDuff.Mode.SRC_IN;
                break;
            case MODE_SRC_OUT:
                mode = PorterDuff.Mode.SRC_OUT;
                break;
            case MODE_SRC_OVER:
                mode = PorterDuff.Mode.SRC_OVER;
                break;
            case MODE_XOR:
                mode = PorterDuff.Mode.XOR;
                break;
            default:
                mode = PorterDuff.Mode.DST_IN;
        }
        log("Mode is " + mode.toString());
        return new PorterDuffXfermode(mode);
    }
}