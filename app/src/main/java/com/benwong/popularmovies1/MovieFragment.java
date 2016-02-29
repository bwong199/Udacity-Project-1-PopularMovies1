package com.benwong.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benwong on 2016-02-24.
 */
public class MovieFragment extends Fragment implements View.OnClickListener{

    private TextView movieCaption;
    private ImageView moviePoster;
    private TextView movieRating;
    private TextView moviePlot;
    private TextView movieReleaseDate;
    private TextView movieReview;
    private TextView movieReview2;
    private List<MovieTrailer> mTrailers = new ArrayList<>();
    private List<MovieReview> mReviews = new ArrayList<>();
    private MovieItem mMovieItem;
    //    private String GOOGLE_API_KEY = "REPLACE WITH YOUR OWN GOOGLE API KEY";
    private String GOOGLE_API_KEY = "AIzaSyCAmyNN7EIvsE1wwubJh0E5ItpS9ydVFTo";
    private String YOUTUBE_VIDEO_ID = "";
    private String movieReviewURL;
    private String movieReviewURL2;
    private ImageView btnPlayVideo;
    private CheckBox movieFavourite;
    private Cursor c;

    private String caption;
    private String url;
    private String plot;
    private Double rating;
    private String releaseDate;
    private String movieId;

    private SQLiteDatabase favouriteDatabase;

    private static final String ARG_MOVIE_ID = "movie_id";

//    //         intent.putExtra("Movie Title", mMovieItem.getCaption());
//    intent.putExtra("Movie Poster", mMovieItem.getUrl());
//    intent.putExtra("Movie Plot", mMovieItem.getPlot());
//    intent.putExtra("Movie Rating", mMovieItem.getRating());
//    intent.putExtra("Release Date", mMovieItem.getRelease_date());
//    intent.putExtra("Movie ID", mMovieItem.getId());//


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movie, container, false);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            caption = bundle.getString("Movie Title") ;
            url = bundle.getString("Movie Poster");
            plot = bundle.getString("Movie Plot");
            rating = bundle.getDouble("Movie Rating", 0);
            releaseDate = bundle.getString("Release Date");
            movieId = bundle.getString("Movie ID");

            Log.i("Title Bundle", caption);
            Log.i("url Bundle", url);
            Log.i("plot Bundle", plot);
            Log.i("rating Bundle", String.valueOf(rating));
            Log.i("releaseDate Bundle", releaseDate);
            Log.i("movieId Bundle", movieId);
        } else {
            Intent intent = getActivity().getIntent();
            caption = intent.getStringExtra("Movie Title");
            url = intent.getStringExtra("Movie Poster");
            plot = intent.getStringExtra("Movie Plot");
            rating = intent.getDoubleExtra("Movie Rating", 0);
            releaseDate = intent.getStringExtra("Release Date");
            movieId = intent.getStringExtra("Movie ID");
        }


        new FetchTrailersTask(movieId).execute();
        new FetchReviewsTask(movieId).execute();
        movieCaption = (TextView) v.findViewById(R.id.movieCaption);
        moviePoster = (ImageView) v.findViewById(R.id.moviePoster);
        movieRating = (TextView) v.findViewById(R.id.movieRating);
        moviePlot = (TextView) v.findViewById(R.id.moviePlot);
        movieReleaseDate = (TextView) v.findViewById(R.id.releaseDate);
        movieReview = (TextView) v.findViewById(R.id.movieReviews);
        movieReview2 = (TextView) v.findViewById(R.id.movieReview2);
        movieFavourite = (CheckBox) v.findViewById(R.id.movieFavourite);

        btnPlayVideo = (ImageView) v.findViewById(R.id.btnPlayTrailer);
        btnPlayVideo.setOnClickListener(this);
        movieReview.setOnClickListener(this);
        movieReview2.setOnClickListener(this);

        movieCaption.setText(caption);
        moviePlot.setText(plot);
        movieRating.setText(String.valueOf("Average rating - " + rating + "/10"));
        movieReleaseDate.setText("Released " + releaseDate);
        Picasso.with(getContext()).load(url).into(moviePoster);

        favouriteDatabase = getActivity().openOrCreateDatabase("Movies", Context.MODE_PRIVATE, null);

        favouriteDatabase.execSQL("CREATE TABLE IF NOT EXISTS favouriteMovies (movieId VARCHAR, movieURL VARCHAR, movieTitle VARCHAR, moviePlot VARCHAR,  movieRating REAL, movieReleaseDate VARCHAR,  id INTEGER PRIMARY KEY)");


        Log.i("dbpath", favouriteDatabase.getPath());

//        File dbFile = getDatabasePath(String.valueOf(favouriteDatabase));
//        Log.i("db path", dbFile.getAbsolutePath());


        try {
            c = favouriteDatabase.rawQuery("SELECT * FROM favouriteMovies WHERE movieId = " + movieId + " LIMIT 1", null);
        } catch (Error e) {
            e.printStackTrace();
        }

        if (c != null && c.moveToFirst()) {
            do {
                movieFavourite.setChecked(true);
            } while (c.moveToNext());
        }

        movieFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    try {

                        favouriteDatabase.execSQL("INSERT INTO favouriteMovies (movieId, movieURL, movieTitle, moviePlot, movieRating, movieReleaseDate) " +
                                        "VALUES (" +
                                        movieId + "," +
                                        "'" + url + "'" + "," +
                                        "'" + caption + "'" + "," +
                                        "'" + plot + "'" + "," +
                                        rating + "," +
                                        "'" + releaseDate + "'" +
                                        ")"
                        );


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    movieFavourite.setChecked(true);
                } else {
                    try {
                        favouriteDatabase.execSQL("DELETE FROM favouriteMovies WHERE movieId = " + movieId);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    movieFavourite.setChecked(false);

                    PhotoGalleryFragment photoGalleryFragmentVariable = new PhotoGalleryFragment();
                    photoGalleryFragmentVariable.updateItems("");
                }


            }
        });






//        webView = (WebView)findViewById(R.id.movieTrailer);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());






        return v;
    }

    protected boolean isAppInstalled(String packageName) {
        Intent mIntent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        String packageName = "com.google.android.youtube";
        boolean isYoutubeInstalled = isAppInstalled(packageName);

        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnPlayTrailer:
//                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + YOUTUBE_VIDEO_ID));
//                startActivity(intent);

                intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), GOOGLE_API_KEY, YOUTUBE_VIDEO_ID);
                if (isYoutubeInstalled == false) {
                    Toast.makeText(getContext(), "Youtube is not installed. Playing trailer in webview", Toast.LENGTH_LONG).show();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + YOUTUBE_VIDEO_ID));
                    startActivity(intent);
                } else {
                    if (intent != null) {
                        startActivity(intent);
                    }
                }
                break;
            case R.id.movieReviews:
                if (movieReview.getText().equals("No reviews yet")) {
                    Toast.makeText(getContext(), "No reviews yet", Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieReviewURL));
                    startActivity(intent);
                }
            case R.id.movieReview2:
                if (movieReview2.getText().equals("No reviews yet")) {
                    Toast.makeText(getContext(), "No reviews yet", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieReviewURL2));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        }

    }



    public class FetchTrailersTask extends AsyncTask<Void, Void, List<MovieTrailer>> {
        private String mQuery;

        public FetchTrailersTask(String query) {
            mQuery = query;
        }

        @Override
        protected List<MovieTrailer> doInBackground(Void... params) {
            return new MovieFetchr().fetchTrailer(mQuery);
        }

        @Override
        protected void onPostExecute(List<MovieTrailer> trailers) {
            mTrailers = trailers;
            for (int i = 0; i < mTrailers.size(); i++) {
                Log.i("Youtube Trailer key", mTrailers.get(i).getKey());
                YOUTUBE_VIDEO_ID = mTrailers.get(0).getKey();
            }
//            webView.loadUrl("https://www.youtube.com/watch?v="+ mTrailers.get(0).getKey());
        }
    }

    public class FetchReviewsTask extends AsyncTask<Void, Void, List<MovieReview>> {
        private String mQuery;

        public FetchReviewsTask(String query) {
            mQuery = query;
        }

        @Override
        protected List<MovieReview> doInBackground(Void... params) {
            return new MovieFetchr().fetchReview(mQuery);
        }

        @Override
        protected void onPostExecute(List<MovieReview> reviews) {
            mReviews = reviews;
            int i;
//            for (i = 0; i < mReviews.size(); i++) {
//                Log.i("Review URL", mReviews.get(i).getReviewURL());
//
//
//            }

            if (mReviews.size() > 0) {
                movieReview.setText(mReviews.get(0).getAuthor() + "'s review");
                movieReviewURL = mReviews.get(0).getReviewURL();
            } else {
                movieReview.setText("No reviews yet");
            }

            if (mReviews.size() > 1) {
                movieReview2.setText(mReviews.get(1).getAuthor() + "'s review");
                movieReviewURL2 = mReviews.get(1).getReviewURL();
            } else {
                movieReview2.setText("");
            }
//                webView.loadUrl("https://www.youtube.com/watch?v="+ mTrailers.get(0).getKey());
        }
    }
}
