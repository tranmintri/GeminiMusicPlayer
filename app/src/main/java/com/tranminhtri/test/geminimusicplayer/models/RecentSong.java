package com.tranminhtri.test.geminimusicplayer.models;

import java.sql.Timestamp;

public class RecentSong {
    private Song song;
    private User user;
    private Timestamp dateListen;

    public RecentSong(Song song, User user, Timestamp dateListen) {
        this.song = song;
        this.user = user;
        this.dateListen = dateListen;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getDateListen() {
        return dateListen;
    }

    public void setDateListen(Timestamp dateListen) {
        this.dateListen = dateListen;
    }
}
