package com.tregz.miksing.home.list.song;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.item.song.Song;
import com.tregz.miksing.data.item.song.SongRealtime;
import com.tregz.miksing.data.join.song.user.UserSongRelation;
import com.tregz.miksing.data.join.song.user.UserSongUpdate;
import com.tregz.miksing.home.HomeActivity;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.ListGesture;
import com.tregz.miksing.home.list.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongFragment extends ListFragment implements Observer<List<UserSongRelation>>,
        ListView {

    private LiveData<List<UserSongRelation>> songLiveData;
    private List<UserSongRelation> relations;
    private int destination = 0; // last gesture's target position
    private OnItem listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnItem) context;
        } catch (ClassCastException e) {
            String name = OnItem.class.getSimpleName();
            throw new ClassCastException(context.toString() + " must implement " + name);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SongRealtime(getContext());
        observe();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SongAdapter(listener);
        recycler.setAdapter(adapter);
        new ItemTouchHelper(new ListGesture(this)).attachToRecyclerView(recycler);
    }

    @Override
    public void onChanged(List<UserSongRelation> relations) {
        this.relations = relations;
        if (getActivity() instanceof HomeActivity)
            ((HomeActivity) getActivity()).setPlaylist(relations);
        sort();
    }

    @Override
    public void onGestureClear(final int from, final int destination) {
        ordered();
    }

    @Override
    public void onItemMoved(final int from, final int destination) {
        // Update unsorted array to save to new position after gesture event
        this.destination = destination;
        int start = from < destination ? from : destination;
        int end = from < destination ? destination : from;
        for (int i = start; i < end; i++) {
            Collections.swap(relations, i, i + 1);
        }
        // Update sorted list to animate gesture event
        adapter.notifyItemMoved(from, destination);
    }

    @Override
    public void ordered() {
        adapter.items.beginBatchedUpdates();
        for (int i = 0; i < relations.size(); i++) {
            UserSongRelation relation = relations.get(i);
            if (relation.join.getPosition() != i) {
                relation.join.setPosition(i);
                new UserSongUpdate(getContext(), relation.join);
            }
        }
        adapter.items.endBatchedUpdates();
        recycler.smoothScrollToPosition(destination);
    }

    @Override
    public void sort() {
        if (relations != null && adapter != null && recycler != null) {
            destination = 0;
            if (ListSorted.customOrder()) ordered();
            adapter.items.replaceAll(relations);
            recycler.smoothScrollToPosition(0);
        }
    }

    @Override
    public void search(String query) {
        if (query.isEmpty()) adapter.items.replaceAll(relations);
        else {
            List<UserSongRelation> searched = new ArrayList<>();
            for (UserSongRelation relation : relations)
                if (relation.song.getTitle().toLowerCase().startsWith(query.toLowerCase()))
                    searched.add(relation);
            adapter.items.replaceAll(searched);
        }
    }

    private void observe() {
        if (songLiveData == null)
            songLiveData = DataReference.getInstance(getContext()).accessUserSong().all();
        if (songLiveData.hasObservers()) songLiveData.removeObserver(this);
        songLiveData.observe(this, this);
    }

    // Allow an interaction to be communicated to the activity
    public interface OnItem {
        void onItemClick(Song song);

        void onItemLongClick(Song song);
    }
}
