package com.tregz.miksing.home;

public enum HomeTab {
    MUSIC("Everything"),
    PREPARE("Prepare");

    private String tab;

    public String getTab() {
        return tab;
    }

    HomeTab(String tab) {
        this.tab = tab;
    }
}
