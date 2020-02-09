package com.tregz.miksing.data.join.song.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.tregz.miksing.data.item.song.Song;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface UserSongAccess {
    String FROM_TABLE = " FROM " + UserSong.TABLE;
    //String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;

    @Transaction
    @Query(SELECT_FROM_TABLE)
    LiveData<List<UserSongRelation>> all();

    @Transaction
    @Query(SELECT_FROM_TABLE + " WHERE user = :userId") // ORDER BY user_song.spot
    LiveData<List<UserSongRelation>> mine(String userId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(UserSong...joins);

    @Query(SELECT_FROM_TABLE + " WHERE song = :key")
    LiveData<UserSongRelation> query(String key);

    @Update //(onConflict = REPLACE)
    Single<Integer> update(UserSong...joins);

    @RawQuery(observedEntities = UserSong.class)
    LiveData<UserSong> item(SupportSQLiteQuery query);

}
