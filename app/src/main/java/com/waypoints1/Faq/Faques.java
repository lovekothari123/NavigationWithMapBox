package com.waypoints1.Faq;


import retrofit2.Call;
import retrofit2.http.GET;

public interface Faques {
    @GET("faq")
    Call<FaqDetailsModel> faqueslist();
}