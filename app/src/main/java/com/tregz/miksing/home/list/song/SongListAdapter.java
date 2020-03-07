package com.tregz.miksing.home.list.song;

import android.util.Log;
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
import com.tregz.miksing.base.date.DateUtil;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.song.TubeSongRelation;
import com.tregz.miksing.home.list.ListHolder;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class SongListAdapter extends RecyclerView.Adapter<ListHolder> {
    public final String TAG = SongListAdapter.class.getSimpleName();

    private SongListFragment.OnItem listener;
    private SortedListAdapterCallback<TubeSongRelation> echo = new SongListSorted<>(this);
    public SortedList<TubeSongRelation> items = new SortedList<>(TubeSongRelation.class, echo);

    SongListAdapter(SongListFragment.OnItem listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, final int position) {
        if (items.get(position) != null) {
            final TubeSongRelation relation = items.get(position);
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
                } else holder.tvDate.setText("");
                if (song.getFeaturing() != null) {
                    Log.d(TAG, "");
                    String feat = holder.itemView.getContext().getString(R.string.card_feat);
                    String html = "<i>" + feat + "</i> " + song.getFeaturing();
                    holder.tvFeat.setText(HtmlCompat.fromHtml(html, FROM_HTML_MODE_LEGACY));
                } else holder.tvFeat.setText("");
                if (song.getWhat() > 0)
                    holder.tvWhat.setText(Song.What.values()[song.getMix()].name());
                else holder.tvWhat.setText("");
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(song);
                }
            });
            /* holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(song);
                    return false;
                }
            }); */
        }
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListHolder(inflater.inflate(R.layout.card_work, parent, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
