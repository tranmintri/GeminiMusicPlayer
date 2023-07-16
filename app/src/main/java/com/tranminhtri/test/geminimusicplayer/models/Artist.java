package com.tranminhtri.test.geminimusicplayer.models;

import java.io.Serializable;
import java.util.List;

public class Artist implements  Serializable{
    private String id;
    private String name;
    private String image;
    private String country;

    public Artist() {
    }

    public Artist(String id, String name, String image, String country) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.country = country;
    }

    public Artist(String id) {
        this.id = id;
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
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
