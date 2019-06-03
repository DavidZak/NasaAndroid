package com.mradmin.nasa_android.ui.main_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mradmin.nasa_android.R;
import com.mradmin.nasa_android.model.Photo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosListAdapter extends RecyclerView.Adapter<PhotosListAdapter.PhotosListViewHolder> {

    private List<Photo> photos;

    private OnPhotoItemClickListener clickListener;

    public PhotosListAdapter(List<Photo> photos, OnPhotoItemClickListener clickListener) {
        this.photos = photos;
        this.clickListener = clickListener;
    }

    @Override
    public PhotosListAdapter.PhotosListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_photo_item, parent, false);
        return new PhotosListAdapter.PhotosListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotosListAdapter.PhotosListViewHolder holder, int position) {
        holder.init(photos.get(position));
    }

    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public void addPhotos(List<Photo> photos) {
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        notifyItemInserted(this.photos.size() - 1);
    }

    public void removePhoto(Photo photo) {
        int index = this.photos.indexOf(photo);
        this.photos.remove(index);
        notifyItemRemoved(index);
    }

    class PhotosListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_photo_date)
        TextView tvPhotoUrl;
        @BindView(R.id.pb_loading)
        ProgressBar progressBar;

        PhotosListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void init(Photo entity) {

            progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(entity.getImgSrc()).into(ivPhoto, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });

            tvPhotoUrl.setText(itemView.getResources().getString(R.string.earth_date, entity.getEarthDate()));

            itemView.setOnClickListener((view) -> {
                clickListener.onItemClick(entity);
            });

            itemView.setOnLongClickListener((view) -> {
                clickListener.onItemLongClick(entity);
                return false;
            });
        }
    }

    public List<Photo> getItems() {
        return photos;
    }

    public void clear() {
        photos.clear();
        notifyDataSetChanged();
    }

    interface OnPhotoItemClickListener {

        void onItemClick(Photo photo);
        void onItemLongClick(Photo photo);
    }
}

