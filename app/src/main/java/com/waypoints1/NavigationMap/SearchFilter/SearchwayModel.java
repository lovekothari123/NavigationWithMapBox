package com.waypoints1.NavigationMap.SearchFilter;

/**
 * Created by love on 3/15/2018.
 */

public class SearchwayModel {


    private double lat;
    private double lng;
    private String address;
    private int id;
    private String waypoint_id;
    private String pulsing;
    private int category_id;
    private String name;
    private String state;
    private String country;
    private String email;
    private String additional_info;
    private String expire_date;
    private int status;
    private int never_expire;
    private String created_at;
    private String updated_at;
    private int status_value;



    public SearchwayModel(double lat, double lng, String address, String waypoint_id, String pulsing, int category_id, String name, String state, String country, String email, String additional_info, String expire_date, int status, int never_expire, String created_at, String updated_at, int status_value) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.id = id;
        this.waypoint_id = waypoint_id;
        this.pulsing = pulsing;
        this.category_id = category_id;
        this.name = name;
        this.state = state;
        this.country = country;
        this.email = email;
        this.additional_info = additional_info;
        this.expire_date = expire_date;
        this.status = status;
        this.never_expire = never_expire;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status_value = status_value;

    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWaypoint_id() {
        return waypoint_id;
    }

    public void setWaypoint_id(String waypoint_id) {
        this.waypoint_id = waypoint_id;
    }

    public String getPulsing() {
        return pulsing;
    }

    public void setPulsing(String pulsing) {
        this.pulsing = pulsing;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNever_expire() {
        return never_expire;
    }

    public void setNever_expire(int never_expire) {
        this.never_expire = never_expire;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getStatus_value() {
        return status_value;
    }

    public void setStatus_value(int status_value) {
        this.status_value = status_value;
    }
}




