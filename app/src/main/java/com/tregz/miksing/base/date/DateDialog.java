package com.tregz.miksing.base.date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.tregz.miksing.R;

public class DateDialog extends DatePickerDialog {

    public DateDialog(
            Context context,
            @StyleRes int themeResId,
            OnDateSetListener listener,
            int year,
            int monthOfYear,
            int dayOfMonth
    ) {
        super(context, themeResId, listener, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void show() {
        super.show();
        Button negative = getButton(AlertDialog.BUTTON_NEGATIVE);
        negative.setBackgroundColor(color(getContext(), R.color.secondaryPageLight));
        negative.setTextColor(color(getContext(), R.color.secondaryDark));
        Button positive = getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setBackgroundColor(color(getContext(), R.color.primaryDark));
        positive.setTextColor(color(getContext(), R.color.primaryPage));
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) positive.getLayoutParams();
        p.setMargins(24, p.topMargin, p.rightMargin, p.bottomMargin);
    }

    private int color(Context context, int resource) {
        return ContextCompat.getColor(context, resource);
    }
}
