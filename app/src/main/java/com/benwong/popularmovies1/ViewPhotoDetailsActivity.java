package com.benwong.popularmovies1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewPhotoDetailsActivity extends AppCompatActivity {

    TextView movieCaption;
    ImageView moviePoster;
    TextView movieRating;
    TextView moviePlot;
    TextView movieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo_details);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Intent intent = getIntent();

        String caption = intent.getStringExtra("Movie Title");
        String url = intent.getStringExtra("Movie Poster");
        String plot = intent.getStringExtra("Movie Plot");
        Double rating = intent.getDoubleExtra("Movie Rating", 0);
        String releaseDate = intent.getStringExtra("Release Date");

        movieCaption = (TextView)findViewById(R.id.movieCaption);
        moviePoster = (ImageView)findViewById(R.id.moviePoster);
        movieRating = (TextView)findViewById(R.id.movieRating);
        moviePlot = (TextView)findViewById(R.id.moviePlot);
        movieReleaseDate = (TextView)findViewById(R.id.releaseDate);

        movieCaption.setText(caption);
        moviePlot.setText(plot);
        movieRating.setText(String.valueOf("Average rating - " + rating));
        movieReleaseDate.setText("Released " + releaseDate);
        Picasso.with(this).load(url).into(moviePoster);



    }

}
