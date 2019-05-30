package com.mradmin.nasa_android.model;

public class RoverManifest {
    private Rover rover;

    public RoverManifest() {
    }

    public RoverManifest(Rover rover) {
        this.rover = rover;
    }

    public Rover getRover() {
        return rover;
    }

    public void setRover(Rover rover) {
        this.rover = rover;
    }
}
