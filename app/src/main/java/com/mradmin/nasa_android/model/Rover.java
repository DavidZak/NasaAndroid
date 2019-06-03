package com.mradmin.nasa_android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Rover implements BaseModel {
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("landing_date")
    private String landingDate;
    @SerializedName("launchDate")
    private String launchDate;
    @SerializedName("status")
    private String status;
    @SerializedName("maxSol")
    private int maxSol;
    @SerializedName("maxDate")
    private String maxDate;
    @SerializedName("totalPhotos")
    private long totalPhotos;
    @SerializedName("cameras")
    private RealmList<RoverCamera> cameras = new RealmList<>();

    public Rover() {
    }

    public Rover(int id, String name, String landingDate, String launchDate, String status, int maxSol, String maxDate, long totalPhotos, RealmList<RoverCamera> cameras) {
        this.id = id;
        this.name = name;
        this.landingDate = landingDate;
        this.launchDate = launchDate;
        this.status = status;
        this.maxSol = maxSol;
        this.maxDate = maxDate;
        this.totalPhotos = totalPhotos;
        this.cameras = cameras;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLandingDate() {
        return landingDate;
    }

    public void setLandingDate(String landingDate) {
        this.landingDate = landingDate;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMaxSol() {
        return maxSol;
    }

    public void setMaxSol(int maxSol) {
        this.maxSol = maxSol;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public long getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(long totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public RealmList<RoverCamera> getCameras() {
        return cameras;
    }

    public void setCameras(RealmList<RoverCamera> cameras) {
        this.cameras = cameras;
    }
}
