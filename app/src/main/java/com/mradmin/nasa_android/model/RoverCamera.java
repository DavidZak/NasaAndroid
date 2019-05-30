package com.mradmin.nasa_android.model;

import com.google.gson.annotations.SerializedName;

public class RoverCamera {

    private int id;
    private String name;
    @SerializedName("rover_id")
    private int roverId;
    @SerializedName("full_name")
    private String fulName;

    public RoverCamera() {
    }

    public RoverCamera(int id, String name, int roverId, String fulName) {
        this.id = id;
        this.name = name;
        this.roverId = roverId;
        this.fulName = fulName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoverId() {
        return roverId;
    }

    public void setRoverId(int roverId) {
        this.roverId = roverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFulName() {
        return fulName;
    }

    public void setFulName(String fulName) {
        this.fulName = fulName;
    }
}
