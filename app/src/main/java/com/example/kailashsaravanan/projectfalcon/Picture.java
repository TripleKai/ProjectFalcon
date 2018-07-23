package com.example.kailashsaravanan.projectfalcon;

public class Picture {
    private String mImageUrl;
    private String mDateTime;
    private String mSize;

    public Picture(){

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
}
