package com.tregz.miksing.home.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.data.Data;
import com.tregz.miksing.data.DataAccess;
import com.tregz.miksing.data.item.work.Work;
import com.tregz.miksing.data.item.work.song.Song;
import com.tregz.miksing.data.item.work.song.SongRealtime;
import com.tregz.miksing.home.item.ItemType;

import java.util.List;

public class ListFragment extends BaseFragment implements Observer<List<Song>> {

    private TextView log;
    private int type;
    private LiveData<List<Song>> songLiveData;
    private List<Song> songs;

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
        log = view.findViewById(R.id.log);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (log.getText().toString().isEmpty() && songs != null) listing();
    }

    @Override
    public void onChanged(List<Song> songs) {
        this.songs = songs;
        log.setText("");
        listing();
    }

    private void append(Data item) {
        if (type == ItemType.SONG.ordinal() || type == ItemType.TAKE.ordinal()
                && item instanceof Work) {
            log.append(((Work)item).getArtist() + " - " + ((Work)item).getTitle());
            if (item instanceof Song) log.append(" (" + ItemType.SONG.getType() + ")\n");
            else log.append(" (" + ItemType.TAKE.getType() + ")\n");
            // TODO more info
        }
    }

    private void listing() {
        for (Song song : songs) append(song);
    }

    private void observe() {
        if (songLiveData == null)
            songLiveData = DataAccess.getInstance(getContext()).songAccess().all();
        if (songLiveData.hasObservers()) songLiveData.removeObserver(this);
        songLiveData.observe(this, this);
    }

    static {
        TAG = ListFragment.class.getSimpleName();
    }
}
