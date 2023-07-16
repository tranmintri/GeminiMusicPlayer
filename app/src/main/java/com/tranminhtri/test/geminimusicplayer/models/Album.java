package com.tranminhtri.test.geminimusicplayer.models;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {
    private String id;
    private String name;
    private String Image;
    private int year;
    private Artist artist;


    public Album() {
    }

    public Album(String id) {
        this.id = id;
    }

    public Album(String id, String name, String image, int year, Artist artist) {
        this.id = id;
        this.name = name;
        Image = image;
        this.year = year;
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}

