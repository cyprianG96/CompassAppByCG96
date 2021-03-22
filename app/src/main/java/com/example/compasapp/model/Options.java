package com.example.compasapp.model;

public class Options {

    private static Options mInstance;
    private Double mLongitude;
    private Double mLatitude;
    private Float mDistance;

    public Options(Double mLongitude, Double mLatitude) {
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;
    }

    public Options(){}

    public static Options getInstance(){
        if (mInstance == null) mInstance = new Options();
        return mInstance;
    }

    public Options(Double mLongitude, Double mLatitude, Float mDistance) {
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;
        this.mDistance = mDistance;
    }

    public Float getmDistance() {
        return mDistance;
    }

    public void setmDistance(Float mDistance) {
        this.mDistance = mDistance;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        this.mLongitude = longitude;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        this.mLatitude = latitude;
    }

}
