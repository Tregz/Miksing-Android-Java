package com.tregz.miksing.home.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tregz.miksing.R;

public class ListHolder extends RecyclerView.ViewHolder {

    ImageView ivIcon;
    TextView tvWhat;
    TextView tvName;
    TextView tvDate;
    TextView tvFeat;
    TextView tvMark;

    ListHolder(View view) {
        super(view);
        tvDate = view.findViewById(R.id.tvDate);
        tvFeat = view.findViewById(R.id.tvFeat);
        ivIcon = view.findViewById(R.id.ivIcon);
        tvMark = view.findViewById(R.id.tvMark);
        tvName = view.findViewById(R.id.tvName);
        tvWhat = view.findViewById(R.id.tvWhat);
    }
}
