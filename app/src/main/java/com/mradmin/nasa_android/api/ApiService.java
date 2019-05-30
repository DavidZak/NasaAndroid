package com.mradmin.nasa_android.api;

import com.mradmin.nasa_android.MainApplication;
import com.mradmin.nasa_android.R;
import com.mradmin.nasa_android.model.Photo;
import com.mradmin.nasa_android.model.Photos;
import com.mradmin.nasa_android.model.Rover;
import com.mradmin.nasa_android.model.RoverManifest;
import com.mradmin.nasa_android.util.ApiServiceException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

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
        this.okHttpClient = MainApplication.getInstance().getOkHttpClient();
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

    @Override
    public void getPhotos(String date){
        Request request = new Request.Builder()
                .url(String.format("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=%1$s&api_key=DEMO_KEY", date))
                .build();

        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getListener().get().onRequestFail(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonString = new JSONObject(response.body().string());
                        JSONArray jsonArrayPhotos = jsonString.getJSONArray("photos");
                        Photos photos = new Photos();
                        for (int i = 0;i < jsonArrayPhotos.length();i++) {
                            JSONObject jsonObjectPhoto =  jsonArrayPhotos.getJSONObject(i);
                            Photo photo = new Photo();
                            photo.setId(jsonObjectPhoto.getInt("id"));
                            photo.setImgSrc(jsonObjectPhoto.getString("img_src"));
                            photo.setEarthDate(jsonObjectPhoto.getString("earth_date"));
                            photos.getPhotos().add(photo);
                        }
                        if (photos.getPhotos() != null)
                            getListener().get().onResponsePhotos(photos.getPhotos());
                        else
                            throw new ApiServiceException("Не удалось получить список фотографий");
                    } catch (ApiServiceException | JSONException | IOException e) {
                        getListener().get().onRequestFail(e);
                    }
                } else {
                    getListener().get().onRequestFail(new IOException(response.message()));
                }
            }
        });
    }

    @Override
    public void getRoverManifest() {
        Request request = new Request.Builder()
                .url("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity?api_key=DEMO_KEY")
                .build();

        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getListener().get().onRequestFail(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        //Здесь нужно получить только max_date
                        JSONObject jsonString = new JSONObject(response.body().string());
                        JSONObject jsonObjectRover = jsonString.getJSONObject("rover");
                        String maxDate = jsonObjectRover.getString("max_date");
                        if (maxDate != null && !maxDate.isEmpty())
                            getListener().get().onResponseRoverManifestMaxDate(maxDate);
                        else
                            throw new ApiServiceException("Не удалось получить последнюю дату");
                    } catch (ApiServiceException | JSONException | IOException e) {
                        getListener().get().onRequestFail(e);
                    }

                } else {
                    getListener().get().onRequestFail(new IOException(response.message()));
                }
            }
        });
    }
}
