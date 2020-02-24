package com.tregz.miksing.data.tube;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tregz.miksing.data.DataAccess;
import com.tregz.miksing.data.user.User;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface TubeAccess extends DataAccess<Tube> {
    String FROM_TABLE = " FROM " + Tube.TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;
    String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;

    @Query(DELETE_FROM_TABLE)
    Single<Integer> wipe();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<List<Long>> insert(Tube...tubes);

    @Update
    Single<Integer> update(Tube...data);

    @Query(SELECT_FROM_TABLE + " WHERE id = :key")
    Maybe<Tube> query(String key);
}
