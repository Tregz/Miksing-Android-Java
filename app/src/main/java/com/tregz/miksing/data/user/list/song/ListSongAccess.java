package com.tregz.miksing.data.user.list.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ListSongAccess {
    String FROM_TABLE = " FROM " + ListSong.TABLE;
    //String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;

    @Transaction
    @Query(SELECT_FROM_TABLE)
    LiveData<List<ListSongRelation>> all();

    @Transaction
    @Query(SELECT_FROM_TABLE + " WHERE user_list = :listId") // ORDER BY user_song.spot
    LiveData<List<ListSongRelation>> prepare(String listId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(ListSong...joins);

    @Transaction
    @Query(SELECT_FROM_TABLE + " WHERE song = :key")
    LiveData<ListSongRelation> query(String key);

    @Update //(onConflict = REPLACE)
    Single<Integer> update(ListSong...joins);

    @RawQuery(observedEntities = ListSong.class)
    LiveData<ListSong> item(SupportSQLiteQuery query);

}
