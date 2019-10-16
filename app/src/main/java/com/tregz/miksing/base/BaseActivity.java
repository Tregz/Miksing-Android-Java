package com.tregz.miksing.base;

import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.tregz.miksing.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    protected static String TAG = BaseActivity.class.getSimpleName();

    public List<BaseDialog> dialogs = new ArrayList<>();

    public ViewGroup getViewGroup() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public BaseDialog add(BaseDialog dialog) {
        dialogs.add(dialog);
        return dialog;
    }

    @Override
    protected void onDestroy() {
        for (BaseDialog dialog : dialogs) if (dialog.alert != null) dialog.alert.dismiss();
        super.onDestroy();
    }
}
