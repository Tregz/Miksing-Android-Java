package com.tregz.miksing.data.song;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.user.tube.UserTubeListener;

import java.util.List;

public class SongCount implements ValueEventListener, Observer<List<Song>> {
    private String TAG = SongCount.class.getSimpleName();

    private long count = 0;
    private Total listener;
    private Context context;
    private DatabaseReference reference;
    private String userId;

    SongCount(Context context, Total listener) {
        this.context = context;
        this.listener = listener;
    }

    SongCount(Context context, DatabaseReference reference, String userId) {
        this.context = context;
        this.userId = userId;
        this.reference = reference;
        reference.addValueEventListener(this);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // do nothing
    }

    @Override
    public void onChanged(List<Song> songs) {
        if (listener != null) listener.size(songs.size());
        if (count == 0) {
            int position = 0;
            for (Song song : songs) reference.child(song.getId()).setValue(position++);
        } else if (songs.size() == count) {
            new UserTubeListener(context, userId);
            count = -1;
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        count = snapshot.getChildrenCount();
        LifecycleOwner owner = null;
        if (context instanceof AppCompatActivity) owner = (AppCompatActivity) context;
        if (owner != null) {
            SongAccess access = DataReference.getInstance(context).accessSong();
            access.liveSongList().observe(owner, this);
        }
    }

    public interface Total {
        void size(int number);
    }
}
