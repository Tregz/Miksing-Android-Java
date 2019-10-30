package com.tregz.miksing.home.item;

public enum ItemType {
    SONG("Music"),
    TAKE("Image");

    private String type;

    public String getType() {
        return type;
    }

    ItemType(String type) {
        this.type = type;
    }
}
