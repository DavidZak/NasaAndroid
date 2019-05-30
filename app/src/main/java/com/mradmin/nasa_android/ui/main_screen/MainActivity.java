package com.mradmin.nasa_android.ui.main_screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.mradmin.nasa_android.R;
import com.mradmin.nasa_android.api.ApiService;
import com.mradmin.nasa_android.api.OnApiServiceListener;
import com.mradmin.nasa_android.model.Photo;
import com.mradmin.nasa_android.model.Rover;
import com.mradmin.nasa_android.model.RoverManifest;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnApiServiceListener {

    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        apiService = new ApiService(this);
        requestRoverManifestMaxDate();
    }

    private void showLoading(boolean visible) {
        runOnUiThread(() -> progressBar.setVisibility(visible ? View.VISIBLE : View.GONE));
    }

    //Api requests

    private void requestPhotos(String date) {
        showLoading(true);
        apiService.getPhotos(date);
    }

    private void requestRoverManifestMaxDate() {
        showLoading(true);
        apiService.getRoverManifest();
    }

    //Api responses

    @Override
    public void onResponsePhotos(List<Photo> photos) {
        showLoading(false);
        System.out.println("---------------------- response photos: " + photos);
    }

    @Override
    public void onResponseRoverManifestMaxDate(String roverManifestMaxDate) {
        showLoading(false);
        System.out.println("---------------------- response max date: " + roverManifestMaxDate);
        requestPhotos(roverManifestMaxDate);
    }

    @Override
    public void onRequestFail(Exception e) {
        showLoading(false);
    }
}
