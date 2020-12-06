package com.tregz.miksing.home.list.song;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.tregz.miksing.R;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.song.TubeSongAccess;
import com.tregz.miksing.data.tube.song.TubeSongRelation;
import com.tregz.miksing.home.HomeActivity;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.ListGesture;
import com.tregz.miksing.home.list.ListPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment listing songs
 *
 * @author Jerome M Robbins
 */
public abstract class SongListFragment extends ListFragment implements
        Observer<List<TubeSongRelation>> {
    //private final String TAG = SongListFragment.class.getSimpleName();

    protected MediatorLiveData<List<TubeSongRelation>> mediator = new MediatorLiveData<>();
    protected List<TubeSongRelation> relations;
    private OnItem onItem;
    private int destination = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onItem = (OnItem) context;
        } catch (ClassCastException e) {
            String name = OnItem.class.getSimpleName();
            throw new ClassCastException(context.toString() + " must implement " + name);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SongListAdapter(onItem);
        recycler.setAdapter(adapter);
        new ItemTouchHelper(new ListGesture(this)).attachToRecyclerView(recycler);
        onTubeSongRelationObserve();
    }

    @Override
    public void onChanged(List<TubeSongRelation> relations) {
        this.relations = relations;
        if (getActivity() instanceof HomeActivity && !relations.isEmpty())
            ((HomeActivity) getActivity()).setPlaylist(relations);
        sort();
    }

    @Override
    public boolean onItemMoved(final int from, final int destination) {
        // Swap items
        int start = Math.min(from, destination);
        int end = Math.max(from, destination);
        for (int i = start; i < end; i++) Collections.swap(relations, i, i + 1);
        // Update sorted list to animate gesture event
        adapter.notifyItemMoved(from, destination);
        return true;
    }

    @Override
    public void search(String query) {
        if (query.isEmpty()) ((SongListAdapter) adapter).items.replaceAll(relations);
        else {
            List<TubeSongRelation> searched = new ArrayList<>();
            for (TubeSongRelation relation : relations)
                if (relation.song.getTitle().toLowerCase().startsWith(query.toLowerCase()))
                    searched.add(relation);
            ((SongListAdapter) adapter).items.replaceAll(searched);
        }
    }

    @Override
    public void sort() {
        if (getView() != null && relations != null && adapter != null && recycler != null) {
            ((SongListAdapter) adapter).items.replaceAll(relations);
            recycler.smoothScrollToPosition(destination);
        }
    }

    protected void onTubeSongRelationObserve() {
        if (getView() != null && mediator != null) {
            if (mediator.hasObservers()) mediator.removeObserver(this);
            mediator.observe(getViewLifecycleOwner(), this);
        }
    }

    protected void onGestureClear(@NonNull final ListPosition list, final int destination) {
        if (destination >= 0) this.destination = destination; // retrieve scroll position on paging
        // Notify the recycler of position changes after gesture event and save new positions
        adapter.notifyDataSetChanged();
        boolean changed = false;
        for (int i = 0; i < relations.size(); i++) {
            TubeSongRelation relation = relations.get(i);
            if (list.hasChanged(relation.join, relation.join.getSongId(), i)) {
                if (!changed) changed = true;
                if (!list.editable()) new DataUpdate(access().update(relation.join));
            }
        }
        if (changed) {
            // Replace the sorted list with updated data, before the auto update for each position
            ((SongListAdapter) adapter).items.replaceAll(relations);
            if (!list.editable()) new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    toast(AuthUtil.logged() ? R.string.to_save_paste : R.string.to_save_login);
                }
            });
        }
    }

    protected TubeSongAccess access() {
        return DataReference.getInstance(getContext()).accessTubeSong();
    }

    // Allow an interaction to be communicated to the activity
    public interface OnItem {
        void onItemClick(Song song);

        void onItemLongClick(Song song);
    }
}
