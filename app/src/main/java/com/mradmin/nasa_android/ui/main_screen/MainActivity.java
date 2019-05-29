package com.mradmin.nasa_android.ui.main_screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.mradmin.nasa_android.R;
import com.mradmin.nasa_android.api.ApiService;
import com.mradmin.nasa_android.api.OnApiServiceListener;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnApiServiceListener {

    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getPhotosApi();
    }

    private void getPhotosApi() {
        ApiService apiService = new ApiService(this);
        apiService.getPhotos();
    }

    @Override
    public void onRequestResponse(Response response) {
        System.out.println("-----------resp: " + response);
    }

    @Override
    public void onRequestFail(IOException e) {
        System.out.println("------------ fail: " + e.getMessage());
    }
}
