package com.example.my.recapsule;

public class FollowInfoClass {
    public String title;
    public String text;
    public String id;
    private String mainimg;
    private String froimg;

    public FollowInfoClass() {
    }

    public FollowInfoClass(String id, String title, String text, String mainimg, String froimg) {
        this.title = title;
        this.text = text;
        this.id = id;
        this.mainimg = mainimg;
        this.froimg = froimg;
    }

    public FollowInfoClass(String id, String title, String text) {
        this.title = title;
        this.text = text;
        this.id = id;
    }

    public String getMainimg() {
        return mainimg;
    }

    public void setMainimg(String mainimg) {
        this.mainimg = mainimg;
    }

    public String getFroimg() {
        return froimg;
    }

    public void setFroimg(String froimg) {
        this.froimg = froimg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
