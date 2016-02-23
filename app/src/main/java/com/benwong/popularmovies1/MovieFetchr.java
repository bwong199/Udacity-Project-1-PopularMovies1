package com.benwong.popularmovies1;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by benwong on 2016-02-02.
 */
public class MovieFetchr {

    private static final String TAG = "MovieFetchr";

    private static final String API_KEY = "REPLACE YOUR API KEY";

//    private static final String API_KEY = "eba456e9a33bf02c8d10006201c1f08e";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

//    public List<MovieItem> fetchFavourites(){
//        List<MovieItem> items = new ArrayList<>();
//
//        ViewPhotoDetailsActivity  ViewPhotoDetailsActivityVariable = new ViewPhotoDetailsActivity();
//
//        SQLiteDatabase favouriteDatabase = ViewPhotoDetailsActivityVariable.favouriteDatabase;
//
//        Cursor c = favouriteDatabase.rawQuery("SELECT * FROM favouriteMovies", null);
//
//        int idIndex = c.getColumnIndex("movieId");
//
//        int urlIndex = c.getColumnIndex("movieURL");
//
//        c.moveToFirst();
//
//        while(c != null){
//            Log.i("favouriteMovies",c.getString(urlIndex));
//
//            MovieItem item = new MovieItem();
//
//            item.setId(c.getString(idIndex));
//
//            item.setUrl(c.getString(urlIndex));
//
//            PhotoGalleryFragment photoGalleryFragmentVariable = new PhotoGalleryFragment();
//
//            photoGalleryFragmentVariable.mItems.add(item);
//
//            c.moveToNext();
//        }
//
//        return items;
//    }

    public List<MovieItem> fetchItems(String argument) {

        List<MovieItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.themoviedb.org/3/movie/"+argument)
                    .buildUpon()
                    .appendQueryParameter("api_key", API_KEY)
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i("URL", url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return items;
    }

    public List<MovieTrailer> fetchTrailer(String movieId){
        List<MovieTrailer> trailers = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.themoviedb.org/3/movie/"+movieId+"/videos")
                    .buildUpon()
                    .appendQueryParameter("api_key", API_KEY)

                    .build().toString();
//            Log.i("Trailer URL", url);
            String jsonString = getUrlString(url);
//            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseTrailers(trailers, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return trailers;
    }

    public List<MovieReview> fetchReview(String movieId){
        List<MovieReview> reviews = new ArrayList<>();
        try {
            String url = Uri.parse("https://api.themoviedb.org/3/movie/"+movieId+"/reviews")
                    .buildUpon()
                    .appendQueryParameter("api_key", API_KEY)

                    .build().toString();
//            Log.i("Review URL", url);
            String jsonString = getUrlString(url);
//            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseReviews(reviews, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return reviews;
    }

    private void parseItems(List<MovieItem> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray photoJsonArray = jsonBody.getJSONArray("results");

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            MovieItem item = new MovieItem();
            item.setId(photoJsonObject.getString("id"));

            item.setCaption(photoJsonObject.getString("title"));

            item.setRating(photoJsonObject.getDouble("vote_average"));
            item.setPlot(photoJsonObject.getString("overview"));
            item.setRelease_date(photoJsonObject.getString("release_date"));

            item.setUrl("https://image.tmdb.org/t/p/w92/" + photoJsonObject.getString("poster_path"));

            items.add(item);

        }
    }

    private void parseTrailers(List<MovieTrailer> trailers, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray photoJsonArray = jsonBody.getJSONArray("results");

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            MovieTrailer trailer = new MovieTrailer();
            trailer.setId(photoJsonObject.getString("id"));
            trailer.setKey(photoJsonObject.getString("key"));

            trailers.add(trailer);

        }
    }

    private void parseReviews(List<MovieReview> reviews, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray photoJsonArray = jsonBody.getJSONArray("results");

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            MovieReview review = new MovieReview();
            review.setAuthor(photoJsonObject.getString("author"));
            review.setContent(photoJsonObject.getString("content"));
            review.setReviewURL(photoJsonObject.getString("url"));

            reviews.add(review);

        }
    }


}
