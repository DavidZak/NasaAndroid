package com.mradmin.nasa_android.model;

import java.util.ArrayList;
import java.util.List;

public class Photos {
    private List<Photo> photos = new ArrayList<>();

    public Photos() {
    }

    public Photos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
