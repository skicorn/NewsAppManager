package com.example.newsappmanager.model;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class News implements Serializable {
    private String imageID;
    private String image;
    private String title;
    private String context;
    private String user;
    private String category;
    private String time;
    private String id;
    private String view;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public News(String id, String title, String image, String context, String user, String view, String category, String time, String imgID) {
        this.image = image;
        this.title = title;
        this.context = context;
        this.user = user;
        this.id = id;
        this.view = view;
        this.category = category;
        this.time = time;
        this.imageID = imgID;
    }


    public News(String image, String title, String context, String user, String view) {
        this.image = image;
        this.title = title;
        this.context = context;
        this.user = user;
        this.view = view;
    }

    public News(String image, String title, String context, String user) {
        this.image = image;
        this.title = title;
        this.context = context;
        this.user = user;
//        this.time = time;
//        this.category = category;
//        this.view = view;
    }
    public  News(){}
}