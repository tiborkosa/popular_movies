package com.example.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReview implements Parcelable {

    private String author;
    private String content;

    /**
     * MovieReview's constructor
     * @param author of the review
     * @param content of the review
     */
    public MovieReview(String author, String content) {
        this.author = author;
        this.content = content;
    }

    /**
     * Constructor using parcelable
     * @param in parcel of the movie
     */
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

    // not used
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write the MovieReview to parcel
     * @param dest parcel
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }

    // parcelable creator class
    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {

        /**
         * From parcel to POJO
         * @param source
         * @return
         */
        @Override
        public MovieReview createFromParcel(Parcel source) {
            return new MovieReview(source);
        }

        /**
         * creating the MovieReview array
         * @param size of the array to be created
         * @return
         */
        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}
