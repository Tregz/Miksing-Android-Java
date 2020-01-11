package com.tregz.miksing.base.spin;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.tregz.miksing.R;

public final class SpinUtil {

    public static void setAdapter(@NonNull Context context, @NonNull Spinner spin, int array) {
        spin.setAdapter(ArrayAdapter.createFromResource(context, array, R.layout.spinner_label));
    }

    public static void setAdapter(@NonNull Context context, @NonNull Spinner spin, String[] array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_label, array);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }
}
