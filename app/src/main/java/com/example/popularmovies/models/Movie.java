package com.example.popularmovies.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String poster;
    private String title;
    private String plot;
    private String rating;
    private String release_date;
    private Boolean isRated = false;

    // empty constructor
    @Ignore
    public Movie() {
    }

    // main constructor
    public Movie(int id, String poster, String title, String plot, String rating, String release_date) {
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.plot = plot;
        this.rating = rating;
        this.release_date = release_date;
    }

    // parcelable constructor
    @Ignore
    private Movie(Parcel in) {
        id = in.readInt();
        poster = in.readString();
        title = in.readString();
        plot = in.readString();
        rating = in.readString();
        release_date = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Boolean getRated() {
        return isRated;
    }

    public void setRated(Boolean rated) {
        isRated = rated;
    }

    // tostring gor debugging
    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                "poster='" + poster + '\'' +
                ", title='" + title + '\'' +
                ", plot='" + plot + '\'' +
                ", rating='" + rating + '\'' +
                ", release_date='" + release_date + '\'' +
                '}';
    }

    // not used describe contents
    @Override
    public int describeContents() {
        return 0;
    }

    // write parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(poster);
        dest.writeString(title);
        dest.writeString(plot);
        dest.writeString(rating);
        dest.writeString(release_date);
    }

    // parcelable creator class
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
