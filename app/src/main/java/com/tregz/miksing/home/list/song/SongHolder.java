package com.tregz.miksing.home.list.song;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tregz.miksing.R;

class SongHolder extends RecyclerView.ViewHolder {

    ImageView ivIcon;
    TextView tvWhat, tvName, tvDate, tvFeat, tvMark;

    SongHolder(View view) {
        super(view);
        tvDate = view.findViewById(R.id.tv_date);
        tvFeat = view.findViewById(R.id.tv_feat);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvMark = view.findViewById(R.id.tv_mark);
        tvName = view.findViewById(R.id.tv_name);
        tvWhat = view.findViewById(R.id.tv_what);
    }
}
