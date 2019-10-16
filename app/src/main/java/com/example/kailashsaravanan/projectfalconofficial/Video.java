package com.example.kailashsaravanan.projectfalconofficial;

import android.net.Uri;

public class Video {
    private Uri mVideoUri;
    private String mName;
    private String mVideoUrl;
    private String mDateTime;
    private String mSize;
    private String mLocation;

    public Video(){

    }

    public Uri getVideoUri() {
        return mVideoUri;
    }

    public void setVideoUri(Uri mVideoUri) {
        this.mVideoUri = mVideoUri;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String mSize) {
        this.mSize = mSize;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
