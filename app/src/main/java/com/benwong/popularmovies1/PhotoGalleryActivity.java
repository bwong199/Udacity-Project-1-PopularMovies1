package com.benwong.popularmovies1;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by benwong on 2016-02-02.
 */

public class PhotoGalleryActivity extends SingleFragmentActivity{
    private Fragment fragment;
    @Override
    protected Fragment createFragment() {
        fragment = PhotoGalleryFragment.newInstance();
        return fragment;
    }

//    public static Intent newIntent(Context context) {
//        return new Intent(context, PhotoGalleryActivity.class);
//    }

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
            Toast.makeText(getApplicationContext(), "Top Rated Movie selected", Toast.LENGTH_LONG).show();
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
            PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            photoGalleryFragment.updateItems("popular");
            return true;
        }

        if (id == R.id.upcoming) {
            PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            photoGalleryFragment.updateItems("upcoming");
            return true;
        }
        if (id == R.id.now_playing) {
            PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            photoGalleryFragment.updateItems("now_playing");
            return true;
        }

        if (id == R.id.favourites) {
            PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            photoGalleryFragment.updateItems("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
