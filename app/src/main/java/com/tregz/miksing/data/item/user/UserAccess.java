package com.tregz.miksing.data.item.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;

@Dao
public interface UserAccess {
    String FROM_TABLE = " FROM " + User.TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;
    String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<List<Long>> insert(User...users);

    @Query(SELECT_FROM_TABLE + " WHERE born BETWEEN :from AND :then")
    LiveData<List<User>> findBirthDayBetween(Date from, Date then);

    @Query(DELETE_FROM_TABLE)
    Single<Integer> wipe();
}
