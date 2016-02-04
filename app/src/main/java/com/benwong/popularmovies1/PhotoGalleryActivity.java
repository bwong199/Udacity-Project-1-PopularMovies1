package com.benwong.popularmovies1;

import android.support.v4.app.Fragment;

/**
 * Created by benwong on 2016-02-02.
 */

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

//    public static Intent newIntent(Context context) {
//        return new Intent(context, PhotoGalleryActivity.class);
//    }


}
