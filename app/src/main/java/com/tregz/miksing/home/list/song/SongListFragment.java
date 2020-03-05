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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongAccess;
import com.tregz.miksing.data.tube.song.TubeSongRelation;
import com.tregz.miksing.data.user.tube.UserTube;
import com.tregz.miksing.data.user.tube.UserTubeTransfer;
import com.tregz.miksing.home.HomeActivity;
import com.tregz.miksing.home.HomeView;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.ListGesture;
import com.tregz.miksing.home.list.ListPosition;
import com.tregz.miksing.home.list.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongListFragment extends ListFragment implements Observer<List<TubeSongRelation>>,
        ListView {

    private final String TAG = SongListFragment.class.getSimpleName();
    private String tubeId = null;
    private final static String POSITION = "position";
    private MediatorLiveData<List<TubeSongRelation>> mediator = new MediatorLiveData<>();
    private List<TubeSongRelation> relations;
    private Page page;
    private OnItem onItem;
    private HomeView home;
    private UserTube join;

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
        //if (page == Page.EVERYTHING) tubeId = PrefShared.getInstance(getContext()).getKeySongs();
        if (page == Page.EVERYTHING) tubeId = null;
        else tubeId = home.getPrepareListId();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        live(tubeId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SongListAdapter(onItem);
        recycler.setAdapter(adapter);
        new ItemTouchHelper(new ListGesture(this)).attachToRecyclerView(recycler);
    }

    @Override
    public void onChanged(List<TubeSongRelation> relations) {
        if (this.relations == null || this.relations.size() != relations.size()) {
            this.relations = relations;
            if (getActivity() instanceof HomeActivity && !relations.isEmpty())
                ((HomeActivity) getActivity()).setPlaylist(relations);
            sort();
        } else {
            this.relations = relations;
            ((SongListAdapter)adapter).items.replaceAll(relations);
        }
    }

    @Override
    public void onGestureClear(final int from, final int destination) {
        boolean mainList = page == Page.EVERYTHING;
        boolean editable = mainList || join != null && AuthUtil.isUser(join.getUserId());
        String nodeId = editable ? tubeId : null;
        final ListPosition listPosition = new ListPosition(Tube.TABLE, nodeId, Song.TABLE);

        //((SongListAdapter) adapter).items.beginBatchedUpdates();
        boolean changed = false;
        for (int i = 0; i < relations.size(); i++) {
            TubeSongRelation relation = relations.get(i);
            if (listPosition.hasChanged(relation.join, relation.join.getSongId(), i)) {
                if (!changed) changed = true;
                if (!editable) new DataUpdate(access().update(relation.join));
            }
        }
        //((SongListAdapter) adapter).items.endBatchedUpdates();
        if (!editable) new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                toast(listPosition.error());
            }
        });
        if (changed) new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                ((SongListAdapter)adapter).items.replaceAll(relations);
                recycler.smoothScrollToPosition(destination);
            }
        });
    }

    @Override
    public void onItemMoved(final int from, final int destination) {
        // Update unsorted array to save to new position after gesture event
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
                join.setTubeId(home.getPrepareListId());
                new DataUpdate(access().update(join));
            } else remove(position);
        } else if (direction == ItemTouchHelper.START || direction == ItemTouchHelper.LEFT) {
            if (page == Page.EVERYTHING) {
            } else remove(position);
        }
    }

    @Override
    public void save(String name, boolean paste) {
        if (getArguments() != null) switch (getArguments().getInt(POSITION, 0)) {
            case 0:
                //
                break;
            case 1:
                String userId = PrefShared.getInstance(getContext()).getUid();
                int position = join.getPosition();
                UserTubeTransfer transfer = new UserTubeTransfer(userId, tubeId, position);
                transfer.upload(name, relations, null, paste);
                home.onSaved();
                break;
        }
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
        if (relations != null && adapter != null && recycler != null) {
            //if (ListSorted.customOrder()) ordered();
            ((SongListAdapter) adapter).items.replaceAll(relations);
            //recycler.smoothScrollToPosition(0);
        }
    }

    public void reload(String tubeId) {
        if (this.tubeId.equals(tubeId)) live(tubeId);
    }

    public void live(UserTube join) {
        this.join = join;
        tubeId = join.getTubeId();
        live(tubeId);
    }

    private void live(String tubeId) {
        if (getView() != null && mediator != null) {
            if (mediator.hasObservers()) mediator.removeObserver(this);
            mediator.observe(getViewLifecycleOwner(), this);
            if (tubeId == null) mediator.addSource(access().allSongs(), this);
            else mediator.addSource(access().whereLiveTube(tubeId), this);
        }
    }

    private TubeSongAccess access() {
        return DataReference.getInstance(getContext()).accessTubeSong();
    }

    private void remove(int position) {
        TubeSongRelation relation = relations.get(position);
        if (tubeId != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
            ref.child(tubeId).child(Song.TABLE).child(relation.join.getSongId()).removeValue();
        } else new DataUpdate(access().delete(relation.join));
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
