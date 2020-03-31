package com.tregz.miksing.data.tube;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;

import android.content.Context;

import com.tregz.miksing.arch.load.LoadResource;
import com.tregz.miksing.data.DataObject;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.lang.Lang;
import com.tregz.miksing.data.lang.LangAccess;

import java.util.Date;
import java.util.Locale;

import io.reactivex.schedulers.Schedulers;

@Entity(tableName = Tube.TABLE)
public class Tube extends DataObject {

    public Tube(@NonNull String id, @NonNull Date createdAt, @Nullable String name) {
        super(id, createdAt);
        this.name = name;
    }

    public final static String TABLE = "tube";

    public String getName(Context context) {
        LangAccess access = DataReference.getInstance(context).accessLang();
        String langId = Tube.TABLE + "-" + getId();
        Lang lang = access.whereId(langId).subscribeOn(Schedulers.io()).blockingGet();
        if ( Locale.getDefault().getLanguage().equals("fr")) return lang.getFrench();
        else return lang.getEnglish();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Tube(Parcel parcel) {
        read(parcel);
    }

    public static final Creator CREATOR = new Creator<Tube>() {
        @Override
        public Tube createFromParcel(Parcel parcel) {
            return new Tube(parcel);
        }

        @Override
        public Tube[] newArray(int i) {
            return new Tube[i];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void read(Parcel parcel) {
        super.read(parcel);
    }
}
