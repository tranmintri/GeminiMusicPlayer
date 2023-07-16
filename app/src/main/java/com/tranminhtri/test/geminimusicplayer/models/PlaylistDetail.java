package com.tranminhtri.test.geminimusicplayer.models;

import java.io.Serializable;

public class PlaylistDetail implements Serializable {
    private Song song;
    private Playlist playlist;

    public PlaylistDetail(){

    }

    public PlaylistDetail(Song song, Playlist playlist) {
        this.song = song;
        this.playlist = playlist;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
