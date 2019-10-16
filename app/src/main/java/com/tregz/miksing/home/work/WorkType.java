package com.tregz.miksing.home.work;

public enum WorkType {
    TAKE("Image"),
    SONG("Video");

    private String type;

    public String getType() {
        return type;
    }

    WorkType(String type) {
        this.type = type;
    }
}
