package com.tregz.miksing.home.list.song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import com.bumptech.glide.Glide;
import com.tregz.miksing.R;
import com.tregz.miksing.core.date.DateUtil;
import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.user.list.song.ListSongRelation;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class SongAdapter extends RecyclerView.Adapter<SongHolder> {
    public final String TAG = SongAdapter.class.getSimpleName();

    private SongFragment.OnItem listener;
    private SortedListAdapterCallback<ListSongRelation> callback = new ListSorted<>(this);
    SortedList<ListSongRelation> items = new SortedList<>(ListSongRelation.class, callback);

    SongAdapter(SongFragment.OnItem listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SongHolder(inflater.inflate(R.layout.card_work, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, final int position) {
        if (items.get(position) != null) {
            final ListSongRelation relation = items.get(position);
            final Song song = relation.song;
            if (song != null) {
                // Download thumbnail with Glide dependency
                String thumb = "https://img.youtube.com/vi/" + song.getId() + "/0.jpg";
                Glide.with(holder.itemView.getContext()).load(thumb).into(holder.ivIcon);

                holder.tvName.setText(song.getTitle());
                holder.tvMark.setText(song.getArtist());
                if (song.getReleasedAt() != null) {
                    String dayOfYear = DateUtil.dayOfYear(null, song.getReleasedAt());
                    String when = "[" + dayOfYear + "]";
                    holder.tvDate.setText(when);
                }
                if (song.getFeaturing() != null) {
                    String feat = holder.itemView.getContext().getString(R.string.card_feat);
                    String html = "<i>" + feat + "</i> " + song.getFeaturing();
                    holder.tvFeat.setText(HtmlCompat.fromHtml(html, FROM_HTML_MODE_LEGACY));
                }
                if (song.getWhat() > 0)
                    holder.tvWhat.setText(Song.What.values()[song.getMix()].name());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(song);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(song);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
