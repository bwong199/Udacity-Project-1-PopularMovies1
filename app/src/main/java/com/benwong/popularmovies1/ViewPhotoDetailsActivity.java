package com.benwong.popularmovies1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewPhotoDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    PhotoGalleryFragment variable = new PhotoGalleryFragment();

    private List<MovieItem> mMovies = variable.mItems;


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

    private String GOOGLE_API_KEY = "AIzaSyCAmyNN7EIvsE1wwubJh0E5ItpS9ydVFTo";
    private String YOUTUBE_VIDEO_ID = "";
    private String movieReviewURL;
    private String movieReviewURL2;
    private ImageView btnPlayVideo;
    private CheckBox movieFavourite;
    Cursor c;

    private SQLiteDatabase favouriteDatabase ;

//    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo_details);
        Intent intent = getIntent();
        final String caption = intent.getStringExtra("Movie Title");
        final String url = intent.getStringExtra("Movie Poster");
        final String plot = intent.getStringExtra("Movie Plot");
        final Double rating = intent.getDoubleExtra("Movie Rating", 0);
        final String releaseDate = intent.getStringExtra("Release Date");
        final String movieId = intent.getStringExtra("Movie ID");
        new FetchTrailersTask(movieId).execute();
        new FetchReviewsTask(movieId).execute();
        movieCaption = (TextView) findViewById(R.id.movieCaption);
        moviePoster = (ImageView) findViewById(R.id.moviePoster);
        movieRating = (TextView) findViewById(R.id.movieRating);
        moviePlot = (TextView) findViewById(R.id.moviePlot);
        movieReleaseDate = (TextView) findViewById(R.id.releaseDate);
        movieReview = (TextView) findViewById(R.id.movieReviews);
        movieReview2 = (TextView) findViewById(R.id.movieReview2);
        movieFavourite = (CheckBox) findViewById(R.id.movieFavourite);

        favouriteDatabase =  this.openOrCreateDatabase("Movies", MODE_PRIVATE, null);

        favouriteDatabase.execSQL("CREATE TABLE IF NOT EXISTS favouriteMovies (movieId VARCHAR, movieURL VARCHAR, movieTitle VARCHAR, moviePlot VARCHAR,  movieRating REAL, movieReleaseDate VARCHAR,  id INTEGER PRIMARY KEY)");


        Log.i("dbpath", favouriteDatabase.getPath());

//        File dbFile = getDatabasePath(String.valueOf(favouriteDatabase));
//        Log.i("db path", dbFile.getAbsolutePath());



        try {
            c = favouriteDatabase.rawQuery("SELECT * FROM favouriteMovies WHERE movieId = " + movieId + " LIMIT 1", null);
        } catch(Error e){
            e.printStackTrace();
        }

        if(c != null && c.moveToFirst()){
            do{
                movieFavourite.setChecked(true);
            } while(c.moveToNext());
        }

        movieFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    try {

                        favouriteDatabase.execSQL("INSERT INTO favouriteMovies (movieId, movieURL, movieTitle, moviePlot, movieRating, movieReleaseDate) " +
                                "VALUES (" +
                                    movieId + "," +
                                     "'" + url +"'" + ","+
                                        "'" +   caption+ "'" +","+
                                        "'" +  plot + "'" +","+
                                         rating +","+
                                        "'" +  releaseDate + "'" +
                                ")"
                        );


                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    movieFavourite.setChecked(true);
                } else {
                    try {
                        favouriteDatabase.execSQL("DELETE FROM favouriteMovies WHERE movieId = " + movieId );

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    movieFavourite.setChecked(false);

                    PhotoGalleryFragment photoGalleryFragmentVariable = new PhotoGalleryFragment();
                    photoGalleryFragmentVariable.updateItems("");
                }


            }
        });


        btnPlayVideo = (ImageView) findViewById(R.id.btnPlayTrailer);


        btnPlayVideo.setOnClickListener(this);
        movieReview.setOnClickListener(this);
        movieReview2.setOnClickListener(this);
//        webView = (WebView)findViewById(R.id.movieTrailer);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());

        movieCaption.setText(caption);
        moviePlot.setText(plot);
        movieRating.setText(String.valueOf("Average rating - " + rating + "/10"));
        movieReleaseDate.setText("Released " + releaseDate);
        Picasso.with(this).load(url).into(moviePoster);


//        for (int i = 0; i < mTrailers.size(); i++) {
//            Log.i("Trailer key in onCreate", mTrailers.get(i).getKey());
//            Log.i("Trailer key in onCreate", mTrailers.get(0).getKey());
//        }
//
//        Log.i("mTrailer size", String.valueOf(mTrailers.size()));


    }

    @Override
    public void onClick(View v) {

        String packageName = "com.google.android.youtube";
        boolean isYoutubeInstalled = isAppInstalled(packageName);

        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnPlayTrailer:
                intent = YouTubeStandalonePlayer.createVideoIntent(this, GOOGLE_API_KEY, YOUTUBE_VIDEO_ID);
                if (isYoutubeInstalled == false) {
                    Toast.makeText(getApplicationContext(), "Youtube is not installed. Playing trailer in webview", Toast.LENGTH_LONG).show();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + YOUTUBE_VIDEO_ID));
                    startActivity(intent);
                } else {
                    if (intent != null) {
                        startActivity(intent);
                    }
                }
                break;
            case R.id.movieReviews:
                if (movieReview.getText() == "No reviews yet") {
                    Toast.makeText(getApplicationContext(), "No reviews yet", Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieReviewURL));
                    startActivity(intent);
                }
            case R.id.movieReview2:
                if (movieReview2.getText() == "") {
                    Toast.makeText(getApplicationContext(), "No reviews yet", Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieReviewURL2));
                    startActivity(intent);
                }
        }

    }

    protected boolean isAppInstalled(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
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
//            for (int i = 0; i < mTrailers.size(); i++) {
//                Log.i("Youtube Trailer key", mTrailers.get(i).getKey());
//                YOUTUBE_VIDEO_ID = mTrailers.get(0).getKey();
//            }
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
