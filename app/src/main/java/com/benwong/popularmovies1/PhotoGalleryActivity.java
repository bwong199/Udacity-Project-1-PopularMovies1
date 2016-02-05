package com.benwong.popularmovies1;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by benwong on 2016-02-02.
 */

public class PhotoGalleryActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
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

            new PhotoGalleryFragment().updateItems("top-rated");
            return true;
        }

//        if (id == R.id.menu_search) {
//            Intent intent = new Intent(this, SearchActivity.class);
//            startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

}
