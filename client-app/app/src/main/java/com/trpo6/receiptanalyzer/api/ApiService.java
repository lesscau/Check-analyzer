package com.trpo6.receiptanalyzer.api;

import com.trpo6.receiptanalyzer.model.Item;
import com.trpo6.receiptanalyzer.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by User on 27.10.2017.
 */

public interface ApiService {
    @GET("")
    Call<ArrayList<Item>> products();

    @GET("/login")
    //Call<String> login(@Field("loginPhone") String loginPhone, @Field("smsPass") String smsPass);
    Call <User> login (@Query("username") String username, @Query("password") String password);
}
