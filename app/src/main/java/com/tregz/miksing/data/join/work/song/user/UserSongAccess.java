package com.tregz.miksing.data.join.work.song.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.tregz.miksing.data.item.work.song.Song;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface UserSongAccess {
    String FROM_TABLE = " FROM " + UserSong.TABLE;
    //String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;

    //@Query(SELECT_FROM_TABLE)
    @Query(SELECT_FROM_TABLE + " INNER JOIN " + Song.TABLE + " ON song.id = user_song.song_id")
    LiveData<List<UserSongRelation>> all();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(UserSong...joins);

    @Update
    Single<Integer> update(UserSong...joins);

    @RawQuery(observedEntities = UserSong.class)
    LiveData<UserSong> item(SupportSQLiteQuery query);

    /* error? varargs mismatch; boolean cannot be converted to String
    @RawQuery(observedEntities = UserWork.class)
    Observable<List<UserWork>> get(SupportSQLiteQuery query); */

}
