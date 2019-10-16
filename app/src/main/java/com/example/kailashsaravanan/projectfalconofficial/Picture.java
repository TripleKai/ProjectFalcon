package com.example.kailashsaravanan.projectfalconofficial;

import android.net.Uri;

public class Picture {
    private Uri mImageUri;
    private String mImageUrl;
    private String mDateTime;
    private String mSize;
    private String mLocation;

    public Picture(){

    }

    public Uri getImageUri() {
        return mImageUri;
    }

    public void setImageUri(Uri mImageUri) {
        this.mImageUri = mImageUri;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
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
