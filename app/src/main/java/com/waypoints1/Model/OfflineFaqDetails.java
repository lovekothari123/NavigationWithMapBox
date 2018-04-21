package com.waypoints1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by love on 3/19/2018.
 */

public class OfflineFaqDetails extends RealmObject implements Parcelable {
    private String faqTitle;
    private String faqDetails;



    protected OfflineFaqDetails(Parcel in) {
        faqTitle = in.readString();
    }

   public OfflineFaqDetails(){

    }


    public OfflineFaqDetails(String question,String faqDetails) {
        this.faqDetails = faqDetails;
        this.faqTitle = question;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(faqTitle);
        dest.writeString(String.valueOf(faqDetails));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfflineFaqDetails> CREATOR = new Creator<OfflineFaqDetails>() {
        @Override
        public OfflineFaqDetails createFromParcel(Parcel in) {
            return new OfflineFaqDetails(in);
        }

        @Override
        public OfflineFaqDetails[] newArray(int size) {
            return new OfflineFaqDetails[size];
        }
    };

    public String getFaqTitle() {
        return faqTitle;
    }

    public void setFaqTitle(String faqTitle) {
        this.faqTitle = faqTitle;
    }

    public String getFaqDetails() {
        return faqDetails;
    }

    public void setFaqDetails(String faqDetails) {
        this.faqDetails = faqDetails;
    }

    public static Creator<OfflineFaqDetails> getCREATOR() {
        return CREATOR;
    }




}
