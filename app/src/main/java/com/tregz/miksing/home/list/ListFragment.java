package com.tregz.miksing.home.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.data.work.Work;
import com.tregz.miksing.data.work.song.Song;
import com.tregz.miksing.data.work.take.Take;
import com.tregz.miksing.home.item.ItemType;

public class ListFragment extends BaseFragment {

    private TextView log;
    private int type;

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
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
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

    private void update() {
        log.setText("");
        Log.d(TAG, "List size " + ListCollection.getInstance().getList().size());
        for (Work work : ListCollection.getInstance().getList()) {
            if (type == ItemType.SONG.ordinal() && work instanceof Song ||
                    type == ItemType.TAKE.ordinal() && work instanceof Take) {
                log.append(work.getArtist() + " - " + work.getTitle());
                if (work instanceof Song) log.append(" (" + ItemType.SONG.getType() + ")\n");
                else log.append(" (" + ItemType.TAKE.getType() + ")\n");
                // TODO more info
            }
        }
    }

    static {
        TAG = ListFragment.class.getSimpleName();
    }
}
