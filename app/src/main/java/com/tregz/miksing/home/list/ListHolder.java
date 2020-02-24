package com.tregz.miksing.home.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tregz.miksing.R;

public class ListHolder extends RecyclerView.ViewHolder {

    public ImageView ivIcon;
    public TextView tvWhat, tvName, tvDate, tvFeat, tvMark;

    public ListHolder(View view) {
        super(view);
        tvDate = view.findViewById(R.id.tv_date);
        tvFeat = view.findViewById(R.id.tv_feat);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvMark = view.findViewById(R.id.tv_mark);
        tvName = view.findViewById(R.id.tv_name);
        tvWhat = view.findViewById(R.id.tv_what);
    }
}
