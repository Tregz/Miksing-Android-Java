package com.tregz.miksing.home.list.song;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.user.list.song.ListSong;
import com.tregz.miksing.data.user.list.song.ListSongAccess;
import com.tregz.miksing.data.user.list.song.ListSongRelation;
import com.tregz.miksing.data.user.list.song.ListSongUpdate;
import com.tregz.miksing.home.HomeActivity;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.ListGesture;
import com.tregz.miksing.home.list.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongFragment extends ListFragment implements Observer<List<ListSongRelation>>,
        ListView {

    //private final String TAG = SongFragment.class.getSimpleName();
    private final static String POSITION = "position";
    private LiveData<List<ListSongRelation>> songLiveData;
    private List<ListSongRelation> relations;
    private int destination = 0; // last gesture's target position
    private OnItem listener;

    public static SongFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        SongFragment fragment = new SongFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
    public void onChanged(List<ListSongRelation> relations) {
        if (this.relations == null || this.relations.size() != relations.size()) {
            if (getActivity() instanceof HomeActivity)
                ((HomeActivity) getActivity()).setPlaylist(relations);
            sort();
        }
        this.relations = relations;
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
        for (int i = start; i < end; i++) Collections.swap(relations, i, i + 1);
        // Update sorted list to animate gesture event
        adapter.notifyItemMoved(from, destination);
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        final ListSong join = relations.get(position).join;
        join.setListId(PrefShared.getInstance(getContext()).getUid() + "-Prepare");

        new ListSongUpdate(getContext(), join);
    }

    @Override
    public void ordered() {
        adapter.items.beginBatchedUpdates();
        for (int i = 0; i < relations.size(); i++) {
            ListSongRelation relation = relations.get(i);
            if (relation.join.getPosition() != i) {
                relation.join.setPosition(i);
                new ListSongUpdate(getContext(), relation.join);
            }
        }
        adapter.items.endBatchedUpdates();
        recycler.smoothScrollToPosition(destination);
    }

    @Override
    public void save() {
        if (getArguments() != null) switch (getArguments().getInt(POSITION, 0)) {
            case 0:
                //
                break;
            case 1:
                //
                break;
        }
    }

    @Override
    public void search(String query) {
        if (query.isEmpty()) adapter.items.replaceAll(relations);
        else {
            List<ListSongRelation> searched = new ArrayList<>();
            for (ListSongRelation relation : relations)
                if (relation.song.getTitle().toLowerCase().startsWith(query.toLowerCase()))
                    searched.add(relation);
            adapter.items.replaceAll(searched);
        }
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

    private void observe() {
        if (getArguments() != null) {
            ListSongAccess access = DataReference.getInstance(getContext()).accessUserSong();
            if (songLiveData == null) switch (getArguments().getInt(POSITION, 0)) {
                case 0:
                    songLiveData = access.all();
                    break;
                case 1:
                    String listId = PrefShared.getInstance(getContext()).getUid() + "-Prepare";
                    songLiveData = access.prepare(listId);
                    break;
            }
            if (songLiveData.hasObservers()) songLiveData.removeObserver(this);
            songLiveData.observe(this, this);
        }
    }

    // Allow an interaction to be communicated to the activity
    public interface OnItem {
        void onItemClick(Song song);

        void onItemLongClick(Song song);
    }
}
