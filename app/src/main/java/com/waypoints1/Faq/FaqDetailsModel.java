package com.waypoints1.Faq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by love on 3/20/2018.
 */

public class FaqDetailsModel {



    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<DatumFaques> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DatumFaques> getData() {
        return data;
    }

    public void setData(List<DatumFaques> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
