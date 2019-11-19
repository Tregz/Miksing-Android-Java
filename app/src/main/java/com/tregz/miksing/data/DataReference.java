package com.tregz.miksing.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tregz.miksing.data.item.user.UserAccess;
import com.tregz.miksing.data.item.work.song.Song;
import com.tregz.miksing.data.item.user.User;
import com.tregz.miksing.data.item.work.song.SongAccess;
import com.tregz.miksing.data.join.work.song.user.UserSong;
import com.tregz.miksing.data.join.work.song.user.UserSongAccess;

@Database(entities = {
        User.class,
        Song.class,
        UserSong.class
}, version = 12, exportSchema = false)
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
                .fallbackToDestructiveMigration().build();
    }

    public abstract SongAccess accessSong();

    public abstract UserAccess accessUser();

    public abstract UserSongAccess accessUserSong();

}
