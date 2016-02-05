package com.benwong.popularmovies1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benwong on 2016-02-02.
 */
public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private List<MovieItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    private PhotoAdapter mAdapter;



    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
//        updateItems("popular");
        new FetchItemsTask("popular").execute();
        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder photoHolder, Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        photoHolder.bindDrawable(drawable);
                    }
                }
        );

        mThumbnailDownloader.start();;
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) v
                .findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        mAdapter = new PhotoAdapter(mItems);
        mPhotoRecyclerView.setAdapter(mAdapter);


        setupAdapter();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    private void setupAdapter() {

//        if (isAdded()) {
//            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
//
//        } else {
//            mAdapter.notifyDataSetChanged();
//        }
//        if (isAdded()) {
//            mAdapter.notifyDataSetChanged();
//        }

    }
    void updateItems(String query){
        mItems.clear();
        new FetchItemsTask(query).execute();
//        mPhotoRecyclerView.getAdapter().notifyDataSetChanged();
    }
    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mItemImageView;
        private MovieItem mMovieItem;


        public PhotoHolder(View itemView) {
            super(itemView);

            mItemImageView = (ImageView) itemView
                    .findViewById(R.id.fragment_photo_gallery_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }

        public void bindMovieItem(MovieItem movieItem){
            mMovieItem = movieItem;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ViewPhotoDetailsActivity.class);
            intent.putExtra("Movie Title", mMovieItem.getCaption());
            intent.putExtra("Movie Poster", mMovieItem.getUrl());
            intent.putExtra("Movie Plot", mMovieItem.getPlot());
            intent.putExtra("Movie Rating", mMovieItem.getRating());
            intent.putExtra("Release Date", mMovieItem.getRelease_date());
            startActivity(intent);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<MovieItem> mMovieItems;

        public PhotoAdapter(List<MovieItem> movieItems) {
            mMovieItems = movieItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            MovieItem movieItem = mMovieItems.get(position);
            photoHolder.bindMovieItem(movieItem);
            Drawable placeholder = getResources().getDrawable(R.drawable.bill_up_close);
            photoHolder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(photoHolder, movieItem.getUrl());
        }

        @Override
        public int getItemCount() {
            return mMovieItems.size();
        }

    }

    public  class FetchItemsTask extends AsyncTask<Void,Void,List<MovieItem>> {
        private String mQuery;
        public FetchItemsTask(String query) {
            mQuery = query;
        }

        @Override
        protected List<MovieItem> doInBackground(Void... params) {
            return new MovieFetchr().fetchItems(mQuery);
        }

        @Override
        protected void onPostExecute(List<MovieItem> items) {
            mItems = items;
            for (int i = 0; i < mItems.size(); i++) {
                Log.i("MovieInGalleryFragment", mItems.get(i).getCaption());
            }

            setupAdapter();

        }

    }



}