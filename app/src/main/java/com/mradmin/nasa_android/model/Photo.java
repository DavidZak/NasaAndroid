package com.mradmin.nasa_android.model;

import com.google.gson.annotations.SerializedName;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Photo implements BaseModel {
    @PrimaryKey
    private long id;
    private long sol;
    @SerializedName("earth_date")
    private String earthDate;
    private RoverCamera camera;
    @SerializedName("img_src")
    private String imgSrc;
    private Rover rover;

    private boolean isRemoved;

    public Photo() {
    }

    public Photo(long id, long sol, String earthDate, RoverCamera camera, String imgSrc, Rover rover, boolean isRemoved) {
        this.id = id;
        this.sol = sol;
        this.earthDate = earthDate;
        this.camera = camera;
        this.imgSrc = imgSrc;
        this.rover = rover;
        this.isRemoved = isRemoved;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSol() {
        return sol;
    }

    public void setSol(long sol) {
        this.sol = sol;
    }

    public RoverCamera getCamera() {
        return camera;
    }

    public void setCamera(RoverCamera camera) {
        this.camera = camera;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public Rover getRover() {
        return rover;
    }

    public void setRover(Rover rover) {
        this.rover = rover;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", sol=" + sol +
                ", earthDate='" + earthDate + '\'' +
                ", camera=" + camera +
                ", imgSrc='" + imgSrc + '\'' +
                ", rover=" + rover +
                ", isRemoved=" + isRemoved +
                '}';
    }
}
