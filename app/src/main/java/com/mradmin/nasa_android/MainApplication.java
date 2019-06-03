package com.mradmin.nasa_android;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainApplication extends Application {

    private final static String REALM_ENCRYPTION_KEY = "EPnodN3jo086LA3UeFxZc0YuljyNw1QRBEVTbhSprDDoP6iQPpNbLxlPy6Q989V0";

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
        initRealm();
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

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("nasa.realm")
                .encryptionKey(REALM_ENCRYPTION_KEY.getBytes())
                .build();

        Realm.setDefaultConfiguration(configuration);
    }

}
