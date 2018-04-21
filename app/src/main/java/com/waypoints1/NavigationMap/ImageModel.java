package com.waypoints1.NavigationMap;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by love on 3/10/2018.
 */

public class ImageModel  extends RealmObject implements Parcelable {

    private int category_id_pin;
    private String categoty_name_pin;
    private String categoty_image_pin;

   public ImageModel(){

   }

    public ImageModel(int category_id_pin, String categoty_name_pin, String categoty_image_pin) {
        this.category_id_pin = category_id_pin;
        this.categoty_name_pin = categoty_name_pin;
        this.categoty_image_pin = categoty_image_pin;
    }



    @Override
    public String toString() {
        return "ImageModel{" +
                "category_id_pin=" + category_id_pin +
                ", categoty_name_pin='" + categoty_name_pin + '\'' +
                ", categoty_image_pin='" + categoty_image_pin + '\'' +
                '}';
    }

    protected ImageModel(Parcel in) {
        category_id_pin = in.readInt();
        categoty_name_pin = in.readString();
        categoty_image_pin = in.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(category_id_pin);
        parcel.writeString(categoty_name_pin);
        parcel.writeString(categoty_image_pin);

        }


    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public int getCategory_id_pin() {
        return category_id_pin;
    }

    public void setCategory_id_pin(int category_id_pin) {
        this.category_id_pin = category_id_pin;
    }

    public String getCategoty_name_pin() {
        return categoty_name_pin;
    }

    public void setCategoty_name_pin(String categoty_name_pin) {
        this.categoty_name_pin = categoty_name_pin;
    }

    public String getCategoty_image_pin() {
        return categoty_image_pin;
    }

    public void setCategoty_image_pin(String categoty_image_pin) {
        this.categoty_image_pin = categoty_image_pin;
    }
}
