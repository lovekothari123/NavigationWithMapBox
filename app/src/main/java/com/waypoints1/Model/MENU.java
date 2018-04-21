package com.waypoints1.Model;

/**
 * Created by jack94 on 05-09-2017.
 */

public class MENU {
    private String title;
    private int icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public MENU(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }
}
