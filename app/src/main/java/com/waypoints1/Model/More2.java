package com.waypoints1.Model;

public class More2 {
    String image;
    String title;
    String id;

    public More2(String image, String title, String id) {
        this.image = image;
        this.title = title;
        this.id = id;

    }

    public More2(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public More2(String image) {
        this.image=image;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

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
}
