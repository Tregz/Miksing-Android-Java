package com.tregz.miksing.home.list.song;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.song.SongAccess;
import com.tregz.miksing.data.song.SongRelation;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongAccess;
import com.tregz.miksing.data.tube.song.TubeSongDelete;
import com.tregz.miksing.data.tube.song.TubeSongRelation;
import com.tregz.miksing.data.tube.song.TubeSongUpdate;
import com.tregz.miksing.home.HomeActivity;
import com.tregz.miksing.home.HomeView;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.ListGesture;
import com.tregz.miksing.home.list.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongListFragment extends ListFragment implements Observer<List<SongRelation>>,
        ListView {

    private final String TAG = SongListFragment.class.getSimpleName();
    private String tubeId = null;
    private final static String POSITION = "position";
    private MediatorLiveData<List<SongRelation>> mediator = new MediatorLiveData<>();
    private List<SongRelation> relations;
    private int destination = 0; // last gesture's target position
    private Page page;
    private OnItem onItem;
    private HomeView home;

    public static SongListFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        SongListFragment fragment = new SongListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onItem = (OnItem) context;
            home = (HomeView) context;
        } catch (ClassCastException e) {
            String name = OnItem.class.getSimpleName();
            throw new ClassCastException(context.toString() + " must implement " + name);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getArguments() != null ? getArguments().getInt(POSITION, 0) : 0;
        page = Page.values()[position];
        live(null);

    }

    public void live(String tubeId) {
        this.tubeId = tubeId;
        TubeSongAccess accessTubeSong = DataReference.getInstance(getContext()).accessTubeSong();
        SongAccess accessSong = DataReference.getInstance(getContext()).accessSong();
        if (mediator != null) {
            if (mediator.hasObservers()) mediator.removeObserver(this);
            mediator.observe(this, this);
            switch (page) {
                case EVERYTHING:
                    String all = "-M0A1B6LQlpJpgdbkYyx";
                    mediator.addSource(accessSong.tube(), this);
                    break;
                case PREPARE:
                    String listId = PrefShared.getInstance(getContext()).getUid() + "-Prepare";
                    String id = tubeId == null ? listId : tubeId;
                    //mediator.addSource(accessTubeSong.prepare(id), this);
                    //mediator.addSource(accessSong.all(), this);
                    break;
            }
            //if (songLiveData != null) songLiveData.observe(this, this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SongListAdapter(onItem);
        recycler.setAdapter(adapter);
        new ItemTouchHelper(new ListGesture(this)).attachToRecyclerView(recycler);
    }

    @Override
    public void onChanged(List<SongRelation> relations) {

        Log.d(TAG, "onChanged " + relations.size());
        if (this.relations == null || this.relations.size() != relations.size()) {
            Log.d(TAG, "onChanged.. " + relations.size());
            this.relations = relations;
            if (getActivity() instanceof HomeActivity)
                ((HomeActivity) getActivity()).setPlaylist(relations);
            sort();
        }
    }

    @Override
    public void onGestureClear(final int from, final int destination) {
        ordered();
    }

    @Override
    public void onItemMoved(final int from, final int destination) {
        // Update unsorted array to save to new position after gesture event
        this.destination = destination;
        int start = Math.min(from, destination);
        int end = Math.max(from, destination);
        for (int i = start; i < end; i++) Collections.swap(relations, i, i + 1);
        // Update sorted list to animate gesture event
        adapter.notifyItemMoved(from, destination);
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.END) {
            if (page == Page.EVERYTHING) {
                final TubeSong join = relations.get(position).join;
                join.setTubeId(home.getPrepareListTitle());
                new TubeSongUpdate(getContext(), join);
            } else remove(position);
        } else if (direction == ItemTouchHelper.START || direction == ItemTouchHelper.LEFT) {
            if (page == Page.EVERYTHING) {
            } else remove(position);
        }
    }

    @Override
    public void ordered() {
        ((SongListAdapter) adapter).items.beginBatchedUpdates();
        for (int i = 0; i < relations.size(); i++) {
            Log.d(TAG, "relation " + relations.get(0).join.getTubeId() + " size? " + relations.size());
            SongRelation relation = relations.get(i);
            Log.d(TAG, "position changed: " + (relation.join.getPosition() != i));
            if (relation.join.getPosition() != i) {
                relation.join.setPosition(i);
                if (tubeId != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
                    ref.child(tubeId).child(Song.TABLE).child(relation.join.getSongId()).setValue(i);
                } else new TubeSongUpdate(getContext(), relation.join);
            }
        }
        ((SongListAdapter) adapter).items.endBatchedUpdates();
        recycler.smoothScrollToPosition(destination);
    }

    @Override
    public void save(String name, boolean paste) {
        if (getArguments() != null) switch (getArguments().getInt(POSITION, 0)) {
            case 0:
                //
                break;
            case 1:
                // Save playlist
                DatabaseReference tube = FirebaseDatabase.getInstance().getReference("tube");
                boolean create = tubeId == null || paste;
                DatabaseReference node = create ? tube.push() : tube.child(tubeId);
                node.child("name").setValue(name);
                for (SongRelation relation : relations) {
                    int position = relation.join.getPosition();
                    node.child("song").child(relation.song.getId()).setValue(position);
                }
                // Save user's playlist
                DatabaseReference user = FirebaseDatabase.getInstance().getReference("user");
                String currentUser = PrefShared.getInstance(getContext()).getUid();
                if (node.getKey() != null)
                    user.child(currentUser).child("tube").child(node.getKey()).setValue(true);
                home.onSaved();
                break;
        }
    }

    @Override
    public void search(String query) {
        if (query.isEmpty()) ((SongListAdapter) adapter).items.replaceAll(relations);
        else {
            List<SongRelation> searched = new ArrayList<>();
            for (SongRelation relation : relations)
                if (relation.song.getTitle().toLowerCase().startsWith(query.toLowerCase()))
                    searched.add(relation);
            ((SongListAdapter) adapter).items.replaceAll(searched);
        }
    }

    @Override
    public void sort() {
        if (relations != null && adapter != null && recycler != null) {
            destination = 0;
            if (ListSorted.customOrder()) ordered();
            ((SongListAdapter) adapter).items.replaceAll(relations);
            recycler.smoothScrollToPosition(0);
        }
    }

    private void remove(int position) {
        SongRelation relation = relations.get(position);
        if (tubeId != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
            ref.child(tubeId).child(Song.TABLE).child(relation.join.getSongId()).removeValue();
        } else new TubeSongDelete(getContext(), relation.join);
        adapter.notifyItemRemoved(position);
    }

    // Allow an interaction to be communicated to the activity
    public interface OnItem {
        void onItemClick(Song song);

        void onItemLongClick(Song song);
    }

    public enum Page {
        EVERYTHING,
        PREPARE
    }
}
