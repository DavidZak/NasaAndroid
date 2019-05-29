package com.mradmin.nasa_android.api;

import com.mradmin.nasa_android.MainApplication;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService implements IApiService {

    private OkHttpClient okHttpClient;
    private WeakReference<OnApiServiceListener> listener;

    public ApiService(OnApiServiceListener listener) {
        this.listener = new WeakReference<>(listener);
        this.okHttpClient = MainApplication.getOkHttpClient();
    }

    public ApiService(OkHttpClient okHttpClient, OnApiServiceListener listener) {
        this.okHttpClient = okHttpClient;
        this.listener = new WeakReference<>(listener);
    }

    private OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private WeakReference<OnApiServiceListener> getListener() {
        return listener;
    }

    private void makeCall(Request request) {
        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getListener().get().onRequestFail(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    getListener().get().onRequestResponse(response);
                } else {
                    getListener().get().onRequestFail(new IOException(response.message()));
                }
            }
        });
    }

    @Override
    public void getPhotos(){
        Request request = new Request.Builder().url("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&page=1&api_key=DEMO_KEY").build();
        makeCall(request);
    }
}
