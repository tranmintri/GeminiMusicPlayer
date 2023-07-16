package com.tranminhtri.test.geminimusicplayer.models;

public class User_Follows {
    private Artist artist;
    private User user;

    public User_Follows(Artist artist, User user) {
        this.artist = artist;
        this.user = user;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
