package com.mradmin.nasa_android.api;

import java.io.IOException;

import okhttp3.Response;

public interface OnApiServiceListener {
    void onRequestResponse(Response response);
    void onRequestFail(IOException e);
}
