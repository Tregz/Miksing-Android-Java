package com.tregz.miksing.home.list;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.item.work.song.SongRealtime;
import com.tregz.miksing.data.join.work.song.user.UserSongRelation;
import com.tregz.miksing.data.join.work.song.user.UserSongUpdate;
import com.tregz.miksing.home.item.ItemType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.telephony.TelephonyManager.PHONE_TYPE_NONE;

public class ListFragment extends BaseFragment implements Observer<List<UserSongRelation>>,
        ListView {
    //private final String TAG = ListFragment.class.getSimpleName();

    private LiveData<List<UserSongRelation>> songLiveData;
    private List<UserSongRelation> relations;
    private ListAdapter adapter;
    private RecyclerView recycler;
    private int destination = 0; // last gesture's target position

    public static ListFragment newInstance(ItemType type) {
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ItemType.class.getSimpleName(), type.ordinal());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type;
        if (getArguments() != null)
            type = getArguments().getInt(ItemType.class.getSimpleName(), 0);
        else type = 0;
        if (type == ItemType.SONG.ordinal()) {
            new SongRealtime(getContext());
            observe();
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.recycler);
        recycler.setItemViewCacheSize(0);
        adapter = new ListAdapter();
        recycler.setAdapter(adapter);
        if (columns() <= 1) recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        else recycler.setLayoutManager(new GridLayoutManager(getContext(), columns()));
        new ItemTouchHelper(new ListGesture(this)).attachToRecyclerView(recycler);
    }

    @Override
    public void onChanged(List<UserSongRelation> relations) {
        this.relations = relations;
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

    public void sort() {
        if (relations != null && adapter != null && recycler != null) {
            destination = 0;
            if (ListSorted.customOrder()) ordered();
            adapter.items.replaceAll(relations);
            recycler.smoothScrollToPosition(0);
        }
    }

    private int columns() {
        if (getContext() != null) {
            Object tm = getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                boolean isTablet = ((TelephonyManager) tm).getPhoneType() == PHONE_TYPE_NONE;
                int orientation = getContext().getResources().getConfiguration().orientation;
                return isTablet ? orientation + 1 : orientation;
            }
        }
        return 1;
    }

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
}
