package com.tregz.miksing.data.item.work.song;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tregz.miksing.data.DataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(AndroidJUnit4.class)
public class SongInsertAndroidTest {
    private final String TAG = SongInsertAndroidTest.class.getSimpleName();

    private CountDownLatch latch = new CountDownLatch(1);
    private DataAccess db;
    private SongAccess access;
    private Song song;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.databaseBuilder(context, DataAccess.class, "testDatabase"
        ).fallbackToDestructiveMigration().build();
        db.clearAllTables();
        access = db.songAccess();
    }

    @Test
    public void dbTest() {
        waitForCallbacks();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Song.TABLE);
        ref.child("3C8meRrrSDg").addListenerForSingleValueEvent(eventListener);
    }

    @After
    public void closeDb() throws IOException {
        if (db != null) db.close();
    }

    private void waitForCallbacks() {
        try {
            latch.await(8000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            if (e.getLocalizedMessage() != null) Log.e(TAG, e.getLocalizedMessage());
        }
    }

    private MaybeObserver<Song> maybeObserver = new MaybeObserver<Song>() {
        @Override
        public void onComplete() {
            // do nothing
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onSubscribe(Disposable d) {
            // do nothing
        }
        @Override
        public void onSuccess(Song result) {
            assertThat(result, equalTo(song));
        }
    };

    private SingleObserver<List<Long>> singleObserver = new SingleObserver<List<Long>>() {
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onSubscribe(Disposable d) {
            // do nothing
        }
        @Override
        public void onSuccess(List<Long> t) {
            // do nothing
        }
    };

    private ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // do nothing
        }
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //new SongNotation(context, dataSnapshot);
            song = new Song(dataSnapshot.getKey(), new Date());
            song.setArtist(getString(dataSnapshot, "mark"));
            Log.d(TAG, "childAdded " + getString(dataSnapshot, "mark"));
            access.insert(song).subscribeOn(Schedulers.io()).subscribe(singleObserver);
            access.query(dataSnapshot.getKey()).subscribeOn(Schedulers.io()).subscribe(maybeObserver);
        }
        private String getString(DataSnapshot snap, String key) {
            return snap.child(key).getValue(String.class);
        }
    };
}
