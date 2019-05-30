package com.mradmin.nasa_android.api;

import com.mradmin.nasa_android.model.Photo;

import java.util.List;

public interface OnApiServiceListener {
    void onResponsePhotos(List<Photo> photos);
    void onResponseRoverManifestMaxDate(String roverManifestMaxDate);
    void onRequestFail(Exception e);
}
