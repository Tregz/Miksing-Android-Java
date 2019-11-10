package com.tregz.miksing.data;

import androidx.annotation.Nullable;

import java.util.NoSuchElementException;

public class DataOptional <M> {

    private final M optional;

    public DataOptional(@Nullable M optional) {
        this.optional = optional;
    }

    public boolean isEmpty() {
        return this.optional == null;
    }

    public M get() {
        if (optional == null) {
            throw new NoSuchElementException("No value present");
        }
        return optional;
    }
}
