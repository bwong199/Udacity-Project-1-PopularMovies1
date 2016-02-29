package com.benwong.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by benwong on 2016-02-24.
 */
public class MovieActivity extends SingleFragmentActivity{
    private static final String EXTRA_MOVIE_ID =
            "com.benwong.popularmovies1.movie_id";

    public static Intent newIntent(Context packageContext, String movieId) {
        Intent intent = new Intent(packageContext, MovieActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
//        String movieId = (String) getIntent()
//                .getSerializableExtra(EXTRA_MOVIE_ID);
        return new MovieFragment();
    }
}
