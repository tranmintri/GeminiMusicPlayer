package com.tranminhtri.test.geminimusicplayer.models;

import java.io.Serializable;

public class AlbumDetails implements Serializable {

    private Song song;
    private  Album album;

    public AlbumDetails() {
    }

    public AlbumDetails(Song song, Album album) {
        this.song = song;
        this.album = album;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
