package com.mradmin.nasa_android.ui.main_screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mradmin.nasa_android.R;
import com.mradmin.nasa_android.api.ApiService;
import com.mradmin.nasa_android.api.OnApiServiceListener;
import com.mradmin.nasa_android.db.DatabaseService;
import com.mradmin.nasa_android.model.Photo;
import com.mradmin.nasa_android.ui.photo_view_screen.PhotoViewActivity;
import com.mradmin.nasa_android.util.SharedPrefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnApiServiceListener, PhotosListAdapter.OnPhotoItemClickListener {

    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;

    @BindView(R.id.iv_load_count)
    ImageView ivLoadCount;

    private ApiService apiService;
    private SharedPrefs sharedPrefs;

    private PhotosListAdapter adapter;

    private List<Photo> photoList = new ArrayList<>();
    private int needMorePhotosCount = 0;

    private String currentDate = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sharedPrefs = new SharedPrefs(this);
        apiService = new ApiService(this);
        databaseService = new DatabaseService();

        initViews();
        onRefreshClick();
    }

    @OnClick(R.id.iv_refresh)
    void onRefreshClick() {
        clearData();
        requestRoverManifestMaxDate();
    }

    @OnClick(R.id.iv_load_count)
    void onLoadCountClick() {
        showLoadCountAlert();
    }

    private void initViews() {
        adapter = new PhotosListAdapter(photoList, this);
        rvPhotos.setAdapter(adapter);
        rvPhotos.setLayoutManager(new LinearLayoutManager(this));
    }

    private void clearData() {
        needMorePhotosCount = sharedPrefs.getPhotosLoadCount();
        adapter.clear();
        photoList.clear();
    }

    private void showLoading(boolean visible) {
        runOnUiThread(() -> progressBar.setVisibility(visible ? View.VISIBLE : View.GONE));
    }

    private void setToAdapter(List<Photo> photos) {
        runOnUiThread(() -> adapter.setPhotos(photos));
    }

    private void addToAdapter(List<Photo> photos) {
        runOnUiThread(() ->  {
            for (Photo photo: photos) {
                if (!photo.isRemoved()) {
                    adapter.addPhoto(photo);
                }
            }
        });
    }

    private void removeFromAdapter(Photo photo) {
        adapter.removePhoto(photo);
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
        if (photos.size() <= needMorePhotosCount) {

            List<Photo> synchedPhotos = synchronizeAndGetPhotos(photos, getPhotosFromDB());

            runOnUiThread(() ->  {
                saveItemsInDB(synchedPhotos);
            });

            needMorePhotosCount -= photos.size();
            if (needMorePhotosCount > 0) {
                setYesterdayDateString(currentDate);
                requestPhotos(currentDate);
            }
        } else {
            List<Photo> photoList = photos.subList(photos.size() - needMorePhotosCount, photos.size());
            List<Photo> synchedPhotos = synchronizeAndGetPhotos(photoList, getPhotosFromDB());

            runOnUiThread(() ->  {
                saveItemsInDB(synchedPhotos);
            });

            needMorePhotosCount = 0;
        }
    }

    @Override
    public void onResponseRoverManifestMaxDate(String roverManifestMaxDate) {
        currentDate = roverManifestMaxDate;
        showLoading(false);
        requestPhotos(roverManifestMaxDate);
    }

    @Override
    public void onRequestFail(Exception e) {
        showLoading(false);

        List<Photo> photos = getPhotosFromDB();
        List<Photo> forAdd = new ArrayList<>();
        for (Photo photo: photos) {
            if (!photo.isRemoved() && !forAdd.contains(photo)) {
                forAdd.add(photo);
            }
        }
        setToAdapter(forAdd);
    }

    //Adapter method

    @Override
    public void onItemClick(Photo photo) {
        openPhotoScreen(photo.getImgSrc());
    }

    @Override
    public void onItemLongClick(Photo photo) {
        showDeleteAlert(photo);
    }

    //load count alert
    private void showLoadCountAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.choose_load_count)
                .setItems(R.array.load_count_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                sharedPrefs.setPhotosLoadCount(5);
                                break;
                            case 1:
                                sharedPrefs.setPhotosLoadCount(10);
                                break;
                            case 2:
                                sharedPrefs.setPhotosLoadCount(20);
                                break;
                            case 3:
                                sharedPrefs.setPhotosLoadCount(30);
                                break;
                            case 4:
                                sharedPrefs.setPhotosLoadCount(40);
                                break;
                        }

                        onRefreshClick();
                    }
                })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();

        alertDialog.show();
    }

    //Delete alert
    private void showDeleteAlert(Photo photo) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alert_delete_title)
                .setPositiveButton(R.string.alert_delete_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //removeFromAdapter(photo);
                        photo.setRemoved(true);
                        saveItemInDB(photo, true);
                        getPhotosFromDB();
                    }
                })
                .setNegativeButton(R.string.alert_delete_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();

        alertDialog.show();
    }

    //Get yesterday date

    private Date yesterday(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private void setYesterdayDateString(String dateString) {
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
           currentDate = dateString;
        }
        currentDate = dateFormat.format(yesterday(date));
    }

    //open activity

    private void openPhotoScreen(String imageUrl) {
        Intent intent = new Intent(this, PhotoViewActivity.class);
        intent.putExtra(PhotoViewActivity.EXTRA_PHOTO_URL, imageUrl);
        startActivity(intent);
    }

    //synchronize response and local photos (for removing already removed photos)

    private List<Photo> synchronizeAndGetPhotos(List<Photo> responsePhotos, List<Photo> localPhotos) {
        List<Photo> resultPhotos = responsePhotos;
        for (int i = 0; i < localPhotos.size(); i++) {
            Photo localPhoto = localPhotos.get(i);
            for (Photo responsePhoto: responsePhotos) {
                if (localPhoto.getId().equals(responsePhoto.getId()) && localPhoto.isRemoved()) {
                    resultPhotos.remove(responsePhoto);
                    break;
                }
            }
        }
        return resultPhotos;
    }

    //db queries

    private List<Photo> getPhotosFromDB() {
        List<Photo> photos = databaseService.getItemList(Photo.class);
        return photos;
    }

    private void saveItemInDB(Photo photo, boolean toRemove) {
        if (databaseService.saveItem(photo)) {
            if (!toRemove) {
                List<Photo> photos = new ArrayList<>();
                photos.add(photo);
                addToAdapter(photos);
            } else {
                removeFromAdapter(photo);
            }
        } else {
            Toast.makeText(this, R.string.error_saving, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveItemsInDB(List<Photo> photos) {
        if (databaseService.saveItems(photos)) {
            addToAdapter(photos);
        } else {
            Toast.makeText(this, R.string.error_saving, Toast.LENGTH_SHORT).show();
        }
    }
}
