package com.waypoints1.NavigationMap.SearchFilter;

/**
 * Created by love on 2/26/2018.
 */

public class SearchFilterModel {

    String name;
    double lat;
    double longg;


    public SearchFilterModel(String name, double lat, double longg) {
        this.name = name;
        this.lat = lat;
        this.longg = longg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
