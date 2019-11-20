package com.tregz.miksing.home.list;

import android.view.LayoutInflater;
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
import com.tregz.miksing.data.item.work.song.Song;
import com.tregz.miksing.data.join.work.song.user.UserSongRelation;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class ListAdapter extends RecyclerView.Adapter<ListHolder> {
    public final String TAG = ListAdapter.class.getSimpleName();

    private SortedListAdapterCallback<UserSongRelation> callback = new ListSorted<>(this);
    SortedList<UserSongRelation> items = new SortedList<>(UserSongRelation.class, callback);

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListHolder(inflater.inflate(R.layout.card_song, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, final int position) {
        if (items.get(position) != null) {
            final UserSongRelation relation = items.get(position);
            final Song song = relation.song;
            if (song != null) {
                String thumb = "https://img.youtube.com/vi/" + song.getId() + "/0.jpg";
                Glide.with(holder.itemView.getContext()).load(thumb).into(holder.ivIcon);
                holder.tvName.setText(song.getTitle());
                holder.tvMark.setText(song.getArtist());
                if (song.getReleasedAt() != null) {
                    String when = "[" + DateUtil.dayOfYear(null, song.getReleasedAt()) + "]";
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
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
