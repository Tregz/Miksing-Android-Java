package com.tregz.miksing.base.text;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.tregz.miksing.R;

public class TextUniform extends AppCompatTextView {

    public TextUniform(Context context) {
        super(context);
        init(context);
    }

    public TextUniform(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextUniform(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setPadding(5,0,5,0);
        setTypeface(ResourcesCompat.getFont(context, R.font.medula_one), Typeface.NORMAL);
        int minTextSize = 8;
        int maxTextSize = 144;
        int step = 1;
        int unit = TypedValue.COMPLEX_UNIT_SP;
        int gravity = Gravity.CENTER;
        setAutoSizeTextTypeUniformWithConfiguration(minTextSize, maxTextSize, step, unit);
    }

}
