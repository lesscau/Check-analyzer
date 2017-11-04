package com.trpo6.receiptanalyzer.api;

import com.trpo6.receiptanalyzer.model.Item;
import com.trpo6.receiptanalyzer.model.RegistrationBody;
import com.trpo6.receiptanalyzer.model.RegistrationResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by User on 27.10.2017.
 */

public interface ApiService {
    @GET("")
    Call<ArrayList<Item>> products();

    @POST("/api/v1.0/users")
    Call<RegistrationResponse> registerUser(@Body RegistrationBody registrationBody);
}
