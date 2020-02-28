package com.tregz.miksing.home.list.tube;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import com.bumptech.glide.Glide;
import com.tregz.miksing.R;
import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.user.User;
import com.tregz.miksing.data.user.tube.UserTubeRelation;
import com.tregz.miksing.home.list.ListHolder;

public class TubeListAdapter extends RecyclerView.Adapter<ListHolder> {
    private String TAG = TubeListAdapter.class.getSimpleName();

    private Context context;
    private TubeListFragment.OnItem listener;
    private SortedListAdapterCallback<UserTubeRelation> echo = new TubeListSorted<>(this);
    SortedList<UserTubeRelation> items = new SortedList<>(UserTubeRelation.class, echo);

    TubeListAdapter(Context context, TubeListFragment.OnItem listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {

        if (items.get(position) != null) {
            final UserTubeRelation relation = items.get(position);
            final Tube tube = relation.tube;
            Log.d(TAG, "tube? " + (tube != null));
            if (tube != null) {
                // Download thumbnail with Glide dependency
                int drawable = R.drawable.ic_playlist_play;
                Glide.with(holder.itemView.getContext()).load(drawable).into(holder.ivIcon);

                holder.tvName.setText(tube.getName(context));
                if (relation.user != null) {
                    String username = relation.user.getName();
                    if (username != null) holder.tvMark.setText(username);
                }

                if (tube.getUpdatedAt() != null) {
                    /* String dayOfYear = DateUtil.dayOfYear(null, song.getReleasedAt());
                    String when = "[" + dayOfYear + "]";
                    holder.tvDate.setText(when); */
                }
                /* if (song.getFeaturing() != null) {
                    String feat = holder.itemView.getContext().getString(R.string.card_feat);
                    String html = "<i>" + feat + "</i> " + song.getFeaturing();
                    holder.tvFeat.setText(HtmlCompat.fromHtml(html, FROM_HTML_MODE_LEGACY));
                } */
                /* if (song.getWhat() > 0)
                    holder.tvWhat.setText(Song.What.values()[song.getMix()].name()); */
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(relation.join, relation.tube.getName(context));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(relation.join);
                    return false;
                }
            });
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
