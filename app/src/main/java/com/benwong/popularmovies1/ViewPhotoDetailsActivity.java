package com.benwong.popularmovies1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewPhotoDetailsActivity extends AppCompatActivity {

    TextView movieCaption;
    ImageView moviePoster;
    TextView movieRating;
    TextView moviePlot;
    TextView movieReleaseDate;
    List<MovieTrailer> mTrailers = new ArrayList<>();
    List<MovieReview> mReviews = new ArrayList<>();
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo_details);



        Intent intent = getIntent();

        String caption = intent.getStringExtra("Movie Title");
        String url = intent.getStringExtra("Movie Poster");
        String plot = intent.getStringExtra("Movie Plot");
        Double rating = intent.getDoubleExtra("Movie Rating", 0);
        String releaseDate = intent.getStringExtra("Release Date");
        String movieId = intent.getStringExtra("Movie ID");

        new FetchTrailersTask(movieId).execute();
        new FetchReviewsTask(movieId).execute();

        movieCaption = (TextView)findViewById(R.id.movieCaption);
        moviePoster = (ImageView)findViewById(R.id.moviePoster);
        movieRating = (TextView)findViewById(R.id.movieRating);
        moviePlot = (TextView)findViewById(R.id.moviePlot);
        movieReleaseDate = (TextView)findViewById(R.id.releaseDate);
        webView = (WebView)findViewById(R.id.movieTrailer);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());



        movieCaption.setText(caption);
        moviePlot.setText(plot);
        movieRating.setText(String.valueOf("Average rating - " + rating));
        movieReleaseDate.setText("Released " + releaseDate);
        Picasso.with(this).load(url).into(moviePoster);


        for(int i = 0;i < mTrailers.size(); i++ ){
            Log.i("Trailer key in onCreate", mTrailers.get(i).getKey());
            Log.i("Trailer key in onCreate", mTrailers.get(0).getKey());
        }

        Log.i("mTrailer size", String.valueOf(mTrailers.size()));

        Toast.makeText(getApplicationContext(), movieId, Toast.LENGTH_LONG).show();

    }


    public  class FetchTrailersTask extends AsyncTask<Void,Void,List<MovieTrailer>> {
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
            }
            webView.loadUrl("https://www.youtube.com/watch?v="+ mTrailers.get(0).getKey());

        }


    }

    public  class FetchReviewsTask extends AsyncTask<Void,Void,List<MovieReview>> {
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
            for (int i = 0; i < mReviews.size(); i++) {
                Log.i("Review URL", mReviews.get(i).getReviewURL());
            }
//                webView.loadUrl("https://www.youtube.com/watch?v="+ mTrailers.get(0).getKey());

        }
    }

}
