package com.tregz.miksing.home.list.song.main;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongWrite;
import com.tregz.miksing.data.user.User;
import com.tregz.miksing.data.user.tube.UserTubeRelation;
import com.tregz.miksing.home.list.ListPosition;
import com.tregz.miksing.home.list.song.SongListAdapter;
import com.tregz.miksing.home.list.song.SongListFragment;
import com.tregz.miksing.home.list.song.plan.SongPlanFragment;

import static com.tregz.miksing.data.Data.UNDEFINED;

public class SongMainFragment extends SongListFragment {
    //private final String TAG = SongMainFragment.class.getSimpleName();

    @Override
    public void live() {
        super.live();
        String exclude = SongPlanFragment.getPrepareListTubeId();
        String uid = PrefShared.getInstance(getContext()).getUid();
        String include = uid != null ? uid : "-M0A1B6LQlpJpgdbkYyx";
        mediator.addSource(access().whereTubeLive(include, exclude), this);
    }

    @Override
    public void onGestureClear(final int destination) {
        onGestureClear(new ListPosition(User.TABLE, AuthUtil.userId(), Song.TABLE), destination);
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.END) {
            final TubeSong join = ((SongListAdapter) adapter).items.get(position).join;
            UserTubeRelation relation = SongPlanFragment.relation;
            String newTubeId = relation != null ? relation.tube.getId() : UNDEFINED;
            new TubeSongWrite(getContext(), new TubeSong(newTubeId, join.getSongId()));

            adapter.notifyItemRemoved(position);
        } /* else if (direction == ItemTouchHelper.START || direction == ItemTouchHelper.LEFT) {
            TODO
        } */

    }
}
