package com.example.kailashsaravanan.projectfalcon;

public class HistoryItem {
    private String mDateTime;
    private String mMotionCount;
    private String mLocation;

    public HistoryItem(){

    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getMotionCount() {
        return mMotionCount;
    }

    public void setMotionCount(String mMotionCount) {
        this.mMotionCount = mMotionCount;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
