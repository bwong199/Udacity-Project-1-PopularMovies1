package com.benwong.popularmovies1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by benwong on 2016-02-03.
 */
public class MovieItem implements Parcelable{



    private static final long serialVersionUID = 1L;

    private String mCaption;
    private String mId;
    private String mUrl;
    private String plot;
    private Double rating;
    private String release_date;
    private boolean favourite;

    public MovieItem() {
    }

    protected MovieItem(Parcel in) {
        mCaption = in.readString();
        mId = in.readString();
        mUrl = in.readString();
        plot = in.readString();
        release_date = in.readString();
        favourite = in.readByte() != 0;
        mTitle = in.readString();
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(mCaption);
        out.writeString(mId);
        out.writeString(mUrl);
        out.writeString(plot);
        out.writeDouble(rating);
        out.writeString(release_date);

    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    private String mTitle;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return mCaption;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }


    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


}
