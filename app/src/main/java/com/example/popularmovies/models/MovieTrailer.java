package com.example.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieTrailer implements Parcelable {

    private int id;
    private String name;
    private String key;
    private String type;

    public MovieTrailer(int id, String name, String key, String type) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
    }

    private MovieTrailer(Parcel p){
        id = p.readInt();
        name = p.readString();
        key = p.readString();
        type = p.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * For debugging
     * @return
     */
    @Override
    public String toString() {
        return "MovieTrailer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(type);
    }

    public final static Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>(){

        @Override
        public MovieTrailer createFromParcel(Parcel source) {
            return new MovieTrailer(source);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
}
