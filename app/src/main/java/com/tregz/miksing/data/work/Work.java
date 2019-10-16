package com.tregz.miksing.data.work;

import com.tregz.miksing.data.DataModel;

import java.util.Date;

public abstract class Work extends DataModel {

    private String mark;
    private String name;

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

    public Date getCreatedAt() {
        return copy;
    }

    public void setCreatedAt(Date copy) {
        this.copy = copy;
    }

    public Date getReleasedAt() {
        return born;
    }

    public void setReleasedAt(Date born) {
        this.born = born;
    }
}
