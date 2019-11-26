package com.tregz.miksing.home.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tregz.miksing.R;
import com.tregz.miksing.core.date.DateUtil;
import com.tregz.miksing.data.item.work.Work;

import java.util.ArrayList;
import java.util.List;

public class ListArray extends ArrayAdapter<Work> {

    private Context context;
    private List<Work> list;

    public ListArray(@NonNull Context context, ArrayList<Work> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            int layout = R.layout.card_work;
            view = LayoutInflater.from(context).inflate(layout, parent,false);
        }
        Work work = list.get(position);
        String thumb = "https://img.youtube.com/vi/" + work.getId() + "/0.jpg";
        Glide.with(context).load(thumb).into((ImageView) view.findViewById(R.id.ivIcon));
        ((TextView) view.findViewById(R.id.tvName)).setText(work.getTitle());
        ((TextView) view.findViewById(R.id.tvMark)).setText(work.getArtist());
        String when = "[" + DateUtil.dayOfYear(null, work.getReleasedAt()) + "]";
        ((TextView) view.findViewById(R.id.tvFeat)).setText(when);
        return view;
    }
}
