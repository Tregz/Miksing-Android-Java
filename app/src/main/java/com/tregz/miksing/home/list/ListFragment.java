package com.tregz.miksing.home.list;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
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
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.item.work.song.SongRealtime;
import com.tregz.miksing.data.join.work.song.user.UserSong;
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

    //private TextView log;
    private LiveData<List<UserSongRelation>> songLiveData;
    private List<UserSongRelation> relations;
    private List<ListAdapter> adapters = new ArrayList<>();
    private RecyclerView recycler;

    private boolean updating = false;

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
        adapters.add(new ListAdapter());
        adapters.add(new ListAdapter());
        recycler.setAdapter(adapters.get(position()));
        if (columns() <= 1) recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        else recycler.setLayoutManager(new GridLayoutManager(getContext(), columns()));
        new ItemTouchHelper(new ListGesture(this)).attachToRecyclerView(recycler);
    }

    @Override
    public void onChanged(List<UserSongRelation> relations) {
        if (!updating) {
            this.relations = relations;
            adapters.get(position()).items.addAll(relations);
        }
    }

    @Override
    public void onGestureClear(final int position) {
        updating = true;
        for (int i = 0; i < relations.size(); i++) update(i);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updating = false;
            }
        }, 1000);
        recycler.smoothScrollToPosition(position);
    }

    @Override
    public void onItemMoved(final int from, final int destination) {
        int start = from < destination ? from : destination;
        int end = from < destination ? destination : from;
        for (int i = start; i < end; i++)Collections.swap(relations, i, i + 1);
        adapter().notifyItemMoved(from, destination);
    }

    public void sort() {
        if (relations != null && adapters.get(position()) != null && recycler != null) {
            adapters.get(position()).items.replaceAll(relations);
            recycler.smoothScrollToPosition(0);
        }
    }

    private ListAdapter adapter() {
        return adapters.get(position());
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

    private int position() {
        return ListSearch.searching ? 1 : 0;
    }

    /* private void append(Data item) {
        if (type == ItemType.SONG.ordinal() || type == ItemType.TAKE.ordinal()
                && item instanceof Work) {
            //log.append(((Work)item).getArtist() + " - " + ((Work)item).getTitle());
            //if (item instanceof Song) log.append(" (" + ItemType.SONG.getType() + ")\n");
            //else log.append(" (" + ItemType.TAKE.getType() + ")\n");
            // TODO more info
        }
    }

    private void listing() {
        for (UserSongRelation join : joins) append(join);
    } */

    private void observe() {
        if (songLiveData == null)
            songLiveData = DataReference.getInstance(getContext()).accessUserSong().all();
        if (songLiveData.hasObservers()) songLiveData.removeObserver(this);
        songLiveData.observe(this, this);
    }

    private void update(int position) {
        UserSong join = relations.get(position).join;
        if (join.getPosition() != position) {
            join.setPosition(position);
            new UserSongUpdate(getContext(), join);
        }
    }
}
