package com.waypoints1.NavigationMap;

/**
 * Created by love on 3/8/2018.
 */

public class Offline_image_Model {

    String Offline_iamge;


    public Offline_image_Model(String offline_iamge) {
        Offline_iamge = offline_iamge;
    }

    @Override
    public String toString() {
        return "Offline_image_Model{" +
                "Offline_iamge='" + Offline_iamge + '\'' +
                '}';
    }

    public String getOffline_iamge() {
        return Offline_iamge;
    }

    public void setOffline_iamge(String offline_iamge) {
        Offline_iamge = offline_iamge;
    }
}
