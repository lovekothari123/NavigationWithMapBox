package com.waypoints1.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import io.realm.RealmObject;

/**
 * Created by love on 27-Mar-18.
 */

public class CategoryWaypoints extends RealmObject implements Parcelable  {

    String waypoint_id;
    String waypoint_name;
    double lat;
    double longg;



    public CategoryWaypoints(String waypoint_id, String waypoint_name, double lat, double longg) {
        this.waypoint_id = waypoint_id;
        Log.d("CateWayPoint","id==>"+waypoint_id);
        this.waypoint_name = waypoint_name;
        Log.d("CateWayPoint","name==>"+waypoint_name);
        this.lat = lat;
        Log.d("CateWayPoint","lat==>"+lat);
        this.longg = longg;
        Log.d("CateWayPoint","longg==>"+longg);
    }

    protected CategoryWaypoints(Parcel in) {
        waypoint_id = in.readString();
        waypoint_name = in.readString();
        lat = in.readDouble();
        longg = in.readDouble();
    }

    public static final Creator<CategoryWaypoints> CREATOR = new Creator<CategoryWaypoints>() {
        @Override
        public CategoryWaypoints createFromParcel(Parcel in) {
            return new CategoryWaypoints(in);
        }

        @Override
        public CategoryWaypoints[] newArray(int size) {
            return new CategoryWaypoints[size];
        }
    };

    @Override
    public String toString() {
        return "CategoryWaypoints{" +
                "waypoint_id='" + waypoint_id + '\'' +
                ", waypoint_name='" + waypoint_name + '\'' +
                ", lat=" + lat +
                ", longg=" + longg +
                '}';
    }

    public CategoryWaypoints(){

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(waypoint_id);
        parcel.writeString(waypoint_name);
        parcel.writeDouble(lat);
        parcel.writeDouble(longg);
    }
}
