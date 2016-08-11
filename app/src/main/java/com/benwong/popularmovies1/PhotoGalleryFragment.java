package com.benwong.popularmovies1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

    Callbacks mCallbacks;

    SQLiteDatabase favouriteDatabase;

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    public List<MovieItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    private PhotoAdapter mAdapter;
    public static String fetchPage = "1";
    private boolean mTwoPane;
    private String MOVIE_KEY;
    private SwipeRefreshLayout swipeContainer;
    ArrayList<MovieItem> saveInstanceList;
    public static String category = "popular";
    GridLayoutManager gridLayoutManager;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface Callbacks {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onMovieSelected(MovieItem movieItem);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("On Attach called");
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {

        System.out.println("On Create Called");

        super.onCreate(savedInstanceState);

        if( getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager  = new GridLayoutManager(getActivity(), 5);
        } else if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayoutManager  = new GridLayoutManager(getActivity(), 8);
        }
        if (savedInstanceState != null)
        {


//            mItems = (  List<MovieItem>)savedInstanceState.get(MOVIE_KEY);
            mItems = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
        }



//        updateItems("popular");
        new FetchItemsTask(category, fetchPage).execute();
        fetchPage = String.valueOf(Integer.parseInt(fetchPage) + 1) ;

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

        mThumbnailDownloader.start();
        ;
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(MOVIE_KEY, (ArrayList<? extends Parcelable>) mItems);
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) v
                .findViewById(R.id.fragment_photo_gallery_recycler_view);

        if( getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager  = new GridLayoutManager(getActivity(), 5);
        } else if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayoutManager  = new GridLayoutManager(getActivity(), 8);
        }

        mPhotoRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new PhotoAdapter(mItems);
        mPhotoRecyclerView.setAdapter(mAdapter);

        mPhotoRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                customLoadMoreDataFromApi(fetchPage);
//                List<Contact> moreContacts = Contact.createContactsList(10, fetchPage);
//                final int curSize = adapter.getItemCount();
//                allContacts.addAll(moreContacts);
//                Handler handler = new Handler();
//
//                final Runnable r = new Runnable() {
//                    public void run() {
//                        adapter.notifyItemRangeInserted(curSize, allContacts.size() - 1);
//                    }
//                };
                fetchPage = String.valueOf(Integer.parseInt(fetchPage) + 1) ;
                new FetchItemsTask(category, fetchPage).execute();
                swipeContainer.setRefreshing(true);
            }
        });

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
//        swipeContainer.setBackgroundResource(darkknight);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                mItems.clear();
                swipeContainer.setRefreshing(true);
                mAdapter.notifyDataSetChanged();
                fetchPage = "1";
                new FetchItemsTask(category, fetchPage).execute();
            }
        });
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

    public void setupAdapter() {

        if (isAdded()) {
            mAdapter.notifyDataSetChanged();

        }

//        if(mAdapter == null){
//            mAdapter = new PhotoAdapter(mItems);
//            mPhotoRecyclerView.setAdapter(mAdapter);
//        } else {
//            mAdapter.notifyDataSetChanged();
//        }

    }

    void updateItems(String query) {


        mItems.clear();
        if(!query.isEmpty()){
            new FetchItemsTask(query, fetchPage).execute();
        } else {

            try {
                List<MovieItem> items = new ArrayList<>();

                boolean inEmulator = "generic".equals(Build.BRAND.toLowerCase());
                //check to see if it's running on Emulator or device. Set favouriteDatabase to different path based on if it's running on emulator or device

                if(inEmulator){
                    favouriteDatabase = SQLiteDatabase.openDatabase("/data/data/com.benwong.popularmovies1/databases/Movies", null, SQLiteDatabase.OPEN_READONLY);

                } else {
                    favouriteDatabase = SQLiteDatabase.openDatabase("/data/user/0/com.benwong.popularmovies1/databases/Movies", null, SQLiteDatabase.OPEN_READONLY);
//
                }

                Log.i("dbpath", favouriteDatabase.getPath());


                Cursor c = favouriteDatabase.rawQuery("SELECT * FROM favouriteMovies", null);

                int idIndex = c.getColumnIndex("movieId");
                int urlIndex = c.getColumnIndex("movieURL");
                int titleIndex = c.getColumnIndex("movieTitle");
                int plotIndex = c.getColumnIndex("moviePlot");
                int ratingIndex = c.getColumnIndex("movieRating");
                int releaseDateIndex = c.getColumnIndex("movieReleaseDate");


                if(c != null && c.moveToFirst()){
                    do{
                        MovieItem item = new MovieItem();

                        item.setId(c.getString(idIndex));

                        item.setUrl(c.getString(urlIndex));

                        item.setCaption(c.getString(titleIndex));

                        item.setPlot(c.getString(plotIndex));

                        item.setRating(c.getDouble(ratingIndex));

                        item.setRelease_date(c.getString(releaseDateIndex));

                        items.add(item);
                    } while(c.moveToNext());
                }

                mItems = items;

                setupAdapter();
            } catch(Exception e){
                e.printStackTrace();
            }



        }

//        mPhotoRecyclerView.getAdapter().notifyDataSetChanged();

    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public void bindMovieItem(MovieItem movieItem) {
            mMovieItem = movieItem;
        }

        @Override
        public void onClick(View v) {
            if (getActivity().findViewById(R.id.detail_fragment_container) == null) {
                Intent intent = new Intent(getActivity(), MovieActivity.class);
                intent.putExtra("Movie Title", mMovieItem.getCaption());
                intent.putExtra("Movie Poster", mMovieItem.getUrl());
                intent.putExtra("Movie Plot", mMovieItem.getPlot());
                intent.putExtra("Movie Rating", mMovieItem.getRating());
                intent.putExtra("Release Date", mMovieItem.getRelease_date());
                intent.putExtra("Movie ID", mMovieItem.getId());
                startActivity(intent);
            } else {
                MovieFragment fragment = new MovieFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Movie Title", mMovieItem.getCaption());
                bundle.putString("Movie Poster", mMovieItem.getUrl());
                bundle.putString("Movie Plot", mMovieItem.getPlot());
                bundle.putString("Release Date", mMovieItem.getRelease_date());
                bundle.putString("Movie ID", mMovieItem.getId());
                bundle.putDouble("Movie Rating", mMovieItem.getRating());
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, fragment).commit();
            }


        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

//        private List<MovieItem> mMovieItems;

        public PhotoAdapter(List<MovieItem> movieItems) {
//            mMovieItems = movieItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            MovieItem movieItem = mItems.get(position);
            photoHolder.bindMovieItem(movieItem);

            mThumbnailDownloader.queueThumbnail(photoHolder, movieItem.getUrl());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

    }
    public class FetchItemsTask extends AsyncTask<Void, Void, List<MovieItem>> {
        private String mQuery;
        private String mPage;

        public FetchItemsTask(String query) {
            mQuery = query;
        }

        public FetchItemsTask(String query, String page) {
            mQuery = query;
            mPage = page;
        }

        @Override
        protected List<MovieItem> doInBackground(Void... params) {
            return new MovieFetchr().fetchItems(mQuery, mPage);
        }

        @Override
        protected void onPostExecute(List<MovieItem> items) {
            mItems.addAll(items);
            for (int i = 0; i < mItems.size(); i++) {
                Log.i("MovieInGalleryFragment", mItems.get(i).getCaption());
            }

            setupAdapter();
            swipeContainer.setRefreshing(false);
        }

    }


}