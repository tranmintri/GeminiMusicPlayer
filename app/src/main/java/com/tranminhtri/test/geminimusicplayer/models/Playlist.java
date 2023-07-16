package com.tranminhtri.test.geminimusicplayer.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Playlist implements  Serializable{
    private String id;
    private String name;
    private Date createDate;
    private User user;

    public Playlist(String id, String name, Date createDate, User user) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.user = user;
    }

    public Playlist() {
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

