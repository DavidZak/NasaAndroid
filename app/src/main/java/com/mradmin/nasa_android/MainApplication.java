package com.mradmin.nasa_android;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainApplication extends Application {

    private OkHttpClient _okHttpClient;
    private static MainApplication _instance;

    public OkHttpClient getOkHttpClient() {
        if (_okHttpClient == null)
            _okHttpClient = initOkHttp();

        return _okHttpClient;
    }

    public static MainApplication getInstance() {
        if (_instance == null)
            _instance = new MainApplication();

        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initOkHttp();
        initRoom();
    }

    private OkHttpClient initOkHttp() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.connectTimeout(2, TimeUnit.MINUTES);
        httpClientBuilder.writeTimeout(2, TimeUnit.MINUTES);
        httpClientBuilder.readTimeout(2, TimeUnit.MINUTES);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.interceptors().add(interceptor);

        return httpClientBuilder.build();
    }

    private void initRoom() {

    }
}
