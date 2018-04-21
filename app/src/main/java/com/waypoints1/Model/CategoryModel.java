package com.waypoints1.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import io.realm.RealmList;
import io.realm.RealmObject;

public class CategoryModel extends RealmObject implements Parcelable {


    int id;
    String name;
    String image;

//    ArrayList<CategoryWaypoints> waypointsdata;
    RealmList<CategoryWaypoints> waypointsdata;

    public CategoryModel() {

    }

    public CategoryModel(int id, String name, String image, RealmList<CategoryWaypoints> waypointsdata) {
        this.id = id;
        Log.d("Not_CAt", "get_cat_==id===Model=>"+id);

        this.name = name;
        Log.d("Not_CAt", "get_cat_==name===Model=>"+name);

        this.image = image;
        Log.d("Not_CAt", "get_cat_==image===Model=>"+image);

        this.waypointsdata=waypointsdata;
    }


    protected CategoryModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public RealmList<CategoryWaypoints> getWaypointsdata() {
        return waypointsdata;
    }

    public void setWaypointsdata(RealmList<CategoryWaypoints> waypointsdata) {
        this.waypointsdata = waypointsdata;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(image);
    }
}