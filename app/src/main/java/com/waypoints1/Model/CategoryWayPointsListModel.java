package com.waypoints1.Model;

/**
 * Created by love on 28-Mar-18.
 */

public class CategoryWayPointsListModel {

    String waypoint_id;
    String waypoint_name;
    double lat;
    double longg;
    String image;


    public CategoryWayPointsListModel(String waypoint_id, String waypoint_name, double lat, double longg) {
        this.waypoint_id = waypoint_id;
        this.waypoint_name = waypoint_name;
        this.lat = lat;
        this.longg = longg;

    }

    @Override
    public String toString() {
        return "CategoryWayPointsListModel{" +
                "waypoint_id='" + waypoint_id + '\'' +
                ", waypoint_name='" + waypoint_name + '\'' +
                ", lat=" + lat +
                ", longg=" + longg +
                '}';
    }



    public String getWaypoint_id() {
        return waypoint_id;
    }

    public void setWaypoint_id(String waypoint_id) {
        this.waypoint_id = waypoint_id;
    }

    public String getWaypoint_name() {
        return waypoint_name;
    }

    public void setWaypoint_name(String waypoint_name) {
        this.waypoint_name = waypoint_name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongg() {
        return longg;
    }

    public void setLongg(double longg) {
        this.longg = longg;
    }
}
