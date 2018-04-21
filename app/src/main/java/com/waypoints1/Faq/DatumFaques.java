package com.waypoints1.Faq;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class DatumFaques extends RealmObject implements Parcelable, Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private String answer;


    public DatumFaques(Integer id, String question, String answer) {
        this.id = id;
        Log.d("FAQ","DatumFaues==>"+id);
        this.question = question;
        this.answer = answer;
    }


    public DatumFaques(){

    }


    protected DatumFaques(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        question = in.readString();
        answer = in.readString();
    }

    public static final Creator<DatumFaques> CREATOR = new Creator<DatumFaques>() {
        @Override
        public DatumFaques createFromParcel(Parcel in) {
            return new DatumFaques(in);
        }

        @Override
        public DatumFaques[] newArray(int size) {
            return new DatumFaques[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(question);
        parcel.writeString(answer);
    }
}


