package com.tregz.miksing.data.item.work.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tregz.miksing.data.item.user.User;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface SongAccess {
    String FROM_TABLE = " FROM " + Song.TABLE;
    String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;

    @Query(SELECT_FROM_TABLE)
    LiveData<List<Song>> all();

    @Query(DELETE_FROM_TABLE)
    Single<Integer> wipe();

    @Update
    Single<Integer> update(Song...songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<List<Long>> insert(Song...songs);

    @Query(SELECT_FROM_TABLE + " WHERE _key = :key")
    Maybe<Song> query(String key);
}

