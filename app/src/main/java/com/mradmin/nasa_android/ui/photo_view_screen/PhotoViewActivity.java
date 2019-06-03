package com.mradmin.nasa_android.ui.photo_view_screen;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.mradmin.nasa_android.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoViewActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO_URL = "extra+photo_url";

    @BindView(R.id.iv_photo)
    PhotoView photoView;

    @BindView(R.id.iv_back)
    ImageView ivback;

    private String image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_PHOTO_URL)) {
            image = getIntent().getStringExtra(EXTRA_PHOTO_URL);
            initViews();
        } else {
            finish();
        }
    }

    @OnClick(R.id.iv_back)
    void onClickBack() {
        finish();
    }

    private void initViews() {
        Picasso.get().load(image).into(photoView);
    }
}
