package com.tregz.miksing.data.item.work;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.tregz.miksing.data.item.Item;

import java.util.Date;

public abstract class Work extends Item {
    public static final String ARTIST = ItemField.MARK.getName();
    public static final String KIND = WorkField.KIND.getName();
    public static final String RELEASED_AT = ItemField.BORN.getName();
    public static final String TITLE = ItemField.NAME.getName();
    private static final String COLUMN_KIND = "kind";

    protected Work() {}

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    @ColumnInfo(name = COLUMN_KIND) protected int kind = 0;

    protected Work(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    public String getArtist() {
        return mark;
    }

    public void setArtist(String mark) {
        this.mark = mark;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public Date getReleasedAt() {
        return born;
    }

    public void setReleasedAt(Date releasedAt) {
        born = releasedAt;
    }

    protected enum WorkField {
        KIND(COLUMN_KIND);

        private String name;

        public String getName() {
            return name;
        }

        WorkField(String name) { this.name = name; }
    }
}
