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
import com.tregz.miksing.home.work.WorkCollection;
import com.tregz.miksing.home.work.WorkType;

public class ListFragment extends BaseFragment {

    private TextView log;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
        Log.d(TAG, "homeFrag");
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

    public void update() {
        log.setText("");
        Log.d(TAG, "List size " + WorkCollection.getInstance().getList().size());
        for (Work work : WorkCollection.getInstance().getList()) {
            log.append(work.getArtist() + " - " + work.getTitle());
            if (work instanceof Song) log.append(" (" + WorkType.SONG.getType() + ")\n");
            else if (work instanceof Take) log.append(" (" + WorkType.TAKE.getType() + ")\n");
            else log.append("\n");
            // TODO more info
        }
    }

    static {
        TAG = ListFragment.class.getSimpleName();
    }
}
