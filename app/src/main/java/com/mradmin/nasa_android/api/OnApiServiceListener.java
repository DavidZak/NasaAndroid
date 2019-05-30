package com.mradmin.nasa_android.api;

import com.mradmin.nasa_android.model.Photo;
import com.mradmin.nasa_android.model.RoverManifest;

import java.util.List;

import okhttp3.Response;

public interface OnApiServiceListener {
    void onResponsePhotos(List<Photo> photos);
    void onResponseRoverManifestMaxDate(String roverManifestMaxDate);
    void onRequestFail(Exception e);
}
