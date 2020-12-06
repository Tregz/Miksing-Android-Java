package com.tregz.miksing.home.list.song.plan;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.tube.song.TubeSongRelation;
import com.tregz.miksing.data.user.tube.UserTubeRelation;
import com.tregz.miksing.data.user.tube.UserTubeTransfer;
import com.tregz.miksing.home.HomeView;
import com.tregz.miksing.home.list.ListPosition;
import com.tregz.miksing.home.list.song.SongListFragment;

import java.util.List;

import static com.tregz.miksing.data.Data.UNDEFINED;

/**
 * Fragment listing prepared songs
 *
 * @author Jerome M Robbins
 */
public class SongPlanFragment extends SongListFragment {
    private final String TAG = SongPlanFragment.class.getSimpleName();

    private HomeView home;
    public static UserTubeRelation relation;
    public static String getPrepareListTubeId() {
        return relation != null ? relation.tube.getId() : UNDEFINED;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            home = (HomeView) context;
        } catch (ClassCastException e) {
            String name = HomeView.class.getSimpleName();
            throw new ClassCastException(context.toString() + " must implement " + name);
        }
    }

    @Override
    public void onChanged(List<TubeSongRelation> relations) {
        super.onChanged(relations);
        boolean guidance = relations == null || relations.isEmpty();
        binding.txGuidance.setVisibility(guidance ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onGestureClear(final int destination) {
        boolean editable = relation != null && AuthUtil.isUser(relation.user.getId());
        String tubeId = editable ? relation.tube.getId() : null;
        onGestureClear(new ListPosition(Tube.TABLE, tubeId, Song.TABLE), destination);
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.END) {
            remove(position);
        } else if (direction == ItemTouchHelper.START || direction == ItemTouchHelper.LEFT) {
            remove(position);
        }
    }

    @Override
    public void onTubeSongRelationObserve() {
        super.onTubeSongRelationObserve();
        String id = getPrepareListTubeId();
        if (BuildConfig.DEBUG) Log.d(TAG, "Prepare list id: " + id);
        mediator.addSource(access().whereTubeLive(id), this);
    }

    public void save(String name, boolean paste) {
        String userId = PrefShared.getInstance(getContext()).getUid();
        int position = relation.join.getPosition();
        UserTubeTransfer transfer = new UserTubeTransfer(userId, relation.tube.getId(), position);
        transfer.upload(name, relations, null, paste);
        home.onSaved();
    }

    private void remove(int position) {
        String tubeId = getPrepareListTubeId();
        TubeSongRelation relation = relations.get(position);
        if (!tubeId.equals(UNDEFINED)) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
            ref.child(tubeId).child(Song.TABLE).child(relation.join.getSongId()).removeValue();
        } else new DataUpdate(access().delete(relation.join));
        adapter.notifyItemRemoved(position);
    }
}
