package com.example.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReview implements Parcelable {

    private String author;
    private String content;

    public MovieReview(String author, String content) {
        this.author = author;
        this.content = content;
    }

    private MovieReview(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * for debugging
     *
     * @return
     */
    @Override
    public String toString() {
        return "MovieReview{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }

    // parcelable creator class
    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {

        @Override
        public MovieReview createFromParcel(Parcel source) {
            return new MovieReview(source);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}
