package com.tranminhtri.test.geminimusicplayer.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;


public class Song implements Parcelable {
    private String id;
    private String name;
    private String image;
    private Artist artist;
    private int duration;
    private int numListen;
    private String filePath;
    private String lyric;

    public Song() {
    }

    public Song(String id) {
        this.id = id;
    }

    public Song(String id, String name, String image, Artist artist, int duration, int numListen, String filePath, String lyric) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.artist = artist;
        this.duration = duration;
        this.numListen = numListen;
        this.filePath = filePath;
        this.lyric = lyric;
    }

    protected Song(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        duration = in.readInt();
        numListen = in.readInt();
        filePath = in.readString();
        lyric = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public int getNumListen() {
        return numListen;
    }

    public void setNumListen(int numListen) {
        this.numListen = numListen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(duration);
        dest.writeInt(numListen);
        dest.writeString(filePath);
        dest.writeString(lyric);
    }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", artist=" + artist +
                ", duration=" + duration +
                ", numListen=" + numListen +
                ", filePath='" + filePath + '\'' +
                ", lyric='" + lyric + '\'' +
                '}';
    }
}
