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

@Database(entities = {User.class, Song.class}, version = 3, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class DataAccess extends RoomDatabase {

    private static DataAccess instance;

    public static DataAccess getInstance(final Context context) {
        if (instance == null) synchronized (DataAccess.class) {
            if (instance == null) instance = newInstance(context.getApplicationContext());
        }
        return instance;
    }

    private static DataAccess newInstance(final Context context) {
        return Room.databaseBuilder(context, DataAccess.class, "miksing-db")
                .fallbackToDestructiveMigration().build();
    }

    public abstract SongAccess songAccess();

    public abstract UserAccess userAccess();

}
