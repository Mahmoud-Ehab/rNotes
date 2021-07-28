package com.me.rnotes;

import android.net.Uri;

import java.net.URI;

public class RecordedNote {

    private String title;
    private int duration;
    private String path;

    public RecordedNote(String title, int duration, String path) {
        this.title = title;
        this.duration = duration;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }
}
