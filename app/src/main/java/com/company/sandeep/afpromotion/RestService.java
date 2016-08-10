package com.company.sandeep.afpromotion;

import pojo.AFResponse;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by chsan_000 on 8/9/2016.
 */
public interface RestService {
    String ENDPOINT = "https://afpromotion.herokuapp.com/";

    @GET("/api")
    void getFeed(Callback<AFResponse> callback);
}
