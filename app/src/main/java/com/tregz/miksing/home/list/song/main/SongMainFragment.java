package com.tregz.miksing.home.list.song.main;

import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.tregz.miksing.BuildConfig;
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

/**
 * Fragment listing all available songs
 *
 * @author Jerome M Robbins
 */
public class SongMainFragment extends SongListFragment {
    private final String TAG = SongMainFragment.class.getSimpleName();

    @Override
    public void onGestureClear(final int destination) {
        String userId = AuthUtil.userId();
        String nodeId = userId != null ? userId : "Q0ueoA9u7idnE10fqLk8Cr6FXHK2";
        onGestureClear(new ListPosition(User.TABLE, nodeId, Song.TABLE), destination);
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.END) {
            final TubeSong join = ((SongListAdapter) adapter).items.get(position).join;
            UserTubeRelation relation = SongPlanFragment.relation;
            String newTubeId = relation != null ? relation.tube.getId() : UNDEFINED;
            if (BuildConfig.DEBUG) Log.d(TAG, "New tube id: " + newTubeId);
            new TubeSongWrite(getContext(), new TubeSong(newTubeId, join.getSongId()));
            adapter.notifyItemRemoved(position);
        } /* else if (direction == ItemTouchHelper.START || direction == ItemTouchHelper.LEFT) {
            TODO
        } */
    }

    @Override
    public void onTubeSongRelationObserve() {
        super.onTubeSongRelationObserve();
        String exclude = SongPlanFragment.getPrepareListTubeId();
        if (BuildConfig.DEBUG) Log.d(TAG, "Observe TubeSongRelation exclude: " + exclude);
        String uid = PrefShared.getInstance(getContext()).getUid();
        String include = uid != null ? uid : "-M0A1B6LQlpJpgdbkYyx";
        if (BuildConfig.DEBUG) Log.d(TAG, "Observe TubeSongRelation include: " + include);
        mediator.addSource(access().whereTubeLive(include, exclude), this);
    }
}
