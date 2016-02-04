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

    private static final String API_KEY = "873a05bbf1bd105ba14e62bc0de94a63";

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

    public List<MovieItem> fetchItems() {

        List<MovieItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.themoviedb.org/3/movie/popular")
                    .buildUpon()
                    .appendQueryParameter("api_key", API_KEY)

                    .build().toString();
//            String url = Uri.parse("https://api.flickr.com/services/rest/")
//                    .buildUpon()
//                    .appendQueryParameter("method", "flickr.photos.getRecent")
//                    .appendQueryParameter("api_key", API_KEY)
//                    .appendQueryParameter("format", "json")
//                    .appendQueryParameter("nojsoncallback", "1")
//                    .appendQueryParameter("extras", "url_s")
//                    .build().toString();


            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return items;
    }

    private void parseItems(List<MovieItem> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray photoJsonArray = jsonBody.getJSONArray("results");


//        for(int i = 0; i < photoJsonArray.length(); i++){
//
//            JSONObject jsonPart = photoJsonArray.getJSONObject(i);
//
//            Log.i("title", jsonPart.getString("title"));
//            Log.i(TAG, "Title: " + jsonPart.getString("title"));
//        }


        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            MovieItem item = new MovieItem();
            item.setId(photoJsonObject.getString("id"));
//            item.setTitle(photoJsonObject.getString("title"));
            item.setCaption(photoJsonObject.getString("title"));
//            if (!photoJsonObject.has("url_s")) {
//                continue;
//            }
            item.setRating(photoJsonObject.getDouble("vote_average"));
            item.setPlot(photoJsonObject.getString("overview"));
            item.setRelease_date(photoJsonObject.getString("release_date"));


            item.setUrl("https://image.tmdb.org/t/p/w92/" + photoJsonObject.getString("poster_path"));
//            item.setOwner(photoJsonObject.getString("owner"));

            items.add(item);
            Log.i("MovieItemTitle", String.valueOf(items.get(i).getCaption()));
//            Log.i("MovieItemID", String.valueOf(items.get(i).getId()));
//            Log.i("MovieItemURL", String.valueOf(items.get(i).getUrl()));
//            Log.i("MovieRating", String.valueOf(items.get(i).getRelease_date()));

        }
    }


}
