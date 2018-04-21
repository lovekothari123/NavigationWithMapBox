package com.waypoints1.NavigationMap;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by love on 3/12/2018.
 */

public class OfflineImagePathModel extends RealmObject implements Parcelable {

 String OfflineImagePath;
 int OfflineImageid;
 String OfflineImageName;

 public OfflineImagePathModel(){


 }

 public OfflineImagePathModel(String path){
     this.OfflineImagePath =path;
 }


    public OfflineImagePathModel(Parcel in) {
        OfflineImagePath = in.readString();
        OfflineImageid = in.readInt();
        OfflineImageName = in.readString();
    }

    public static final Creator<OfflineImagePathModel> CREATOR = new Creator<OfflineImagePathModel>() {
        @Override
        public OfflineImagePathModel createFromParcel(Parcel in) {
            return new OfflineImagePathModel(in);
        }

        @Override
        public OfflineImagePathModel[] newArray(int size) {
            return new OfflineImagePathModel[size];
        }
    };

    public OfflineImagePathModel(String path, int id, String imagename_) {

        this.OfflineImagePath =path;
        this.OfflineImageName=imagename_;
        this.OfflineImageid=id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(OfflineImagePath);
        parcel.writeInt(OfflineImageid);
        parcel.writeString(OfflineImageName);
    }


    public String getOfflineImagePath() {
        return OfflineImagePath;
    }

    public void setOfflineImagePath(String offlineImagePath) {
        OfflineImagePath = offlineImagePath;
    }

    public int getOfflineImageid() {
        return OfflineImageid;
    }

    public void setOfflineImageid(int offlineImageid) {
        OfflineImageid = offlineImageid;
    }

    public String getOfflineImageName() {
        return OfflineImageName;
    }

    public void setOfflineImageName(String offlineImageName) {
        OfflineImageName = offlineImageName;
    }

    public static Creator<OfflineImagePathModel> getCREATOR() {
        return CREATOR;
    }
}
