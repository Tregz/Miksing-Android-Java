package com.tregz.miksing.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tregz.miksing.data.user.UserAccess;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.user.User;
import com.tregz.miksing.data.song.SongAccess;
import com.tregz.miksing.data.user.list.song.ListSong;
import com.tregz.miksing.data.user.list.song.ListSongAccess;

@Database(entities = {
        User.class,
        Song.class,
        ListSong.class
}, version = 15, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class DataReference extends RoomDatabase {

    private static DataReference instance;

    public static DataReference getInstance(final Context context) {
        if (instance == null) synchronized (DataReference.class) {
            if (instance == null) instance = newInstance(context.getApplicationContext());
        }
        return instance;
    }

    private static DataReference newInstance(final Context context) {
        return Room.databaseBuilder(context, DataReference.class, "miksing-db")
                // do not do this in production: .allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
    }

    public abstract SongAccess accessSong();

    public abstract UserAccess accessUser();

    public abstract ListSongAccess accessUserSong();

}
