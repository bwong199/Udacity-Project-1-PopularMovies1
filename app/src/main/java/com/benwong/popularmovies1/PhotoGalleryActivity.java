package com.benwong.popularmovies1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by benwong on 2016-02-02.
 */

public class PhotoGalleryActivity extends SingleFragmentActivity
        implements PhotoGalleryFragment.Callbacks
{
    private Fragment fragment;
    private boolean mTwoPane;

    @Override
    protected Fragment createFragment() {
        fragment = PhotoGalleryFragment.newInstance();
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    //    public static Intent newIntent(Context context) {
//        return new Intent(context, PhotoGalleryActivity.class);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);

        if(findViewById(R.id.detail_fragment_container) != null){
            mTwoPane = true;
            Log.i("TwoPane?", String.valueOf(mTwoPane));


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.topRatedMovies) {
            Toast.makeText(getApplicationContext(), "Top-Rated Movies Selected", Toast.LENGTH_LONG).show();
            PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            photoGalleryFragment.updateItems("top-rated");
            return true;
        }

//        if (id == R.id.menu_search) {
//            Intent intent = new Intent(this, SearchActivity.class);
//            startActivity(intent);
//            return true;
//        }
        if (id == R.id.popular) {
            Toast.makeText(getApplicationContext(), "Popular Movies Selected", Toast.LENGTH_LONG).show();
            PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            photoGalleryFragment.updateItems("popular");
            return true;
        }

        if (id == R.id.upcoming) {
            Toast.makeText(getApplicationContext(), "Upcoming Movies Selected", Toast.LENGTH_LONG).show();
            PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            photoGalleryFragment.updateItems("upcoming");
            return true;
        }
        if (id == R.id.now_playing) {
            Toast.makeText(getApplicationContext(), "Movies in Theatre Selected", Toast.LENGTH_LONG).show();
            PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            photoGalleryFragment.updateItems("now_playing");
            return true;
        }

        if (id == R.id.favourites) {
            Toast.makeText(getApplicationContext(), "Favourited Movies Selected", Toast.LENGTH_LONG).show();
            PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            photoGalleryFragment.updateItems("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(MovieItem movieItem) {

    }
}
