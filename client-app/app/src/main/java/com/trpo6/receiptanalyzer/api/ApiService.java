package com.trpo6.receiptanalyzer.api;

import com.trpo6.receiptanalyzer.model.Item;
import com.trpo6.receiptanalyzer.model.AuthResponse;
import com.trpo6.receiptanalyzer.model.RegistrationBody;
import com.trpo6.receiptanalyzer.model.RegistrationResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Интерфейс для реализации запросов
 */

public interface ApiService {
    @GET("")
    Call<ArrayList<Item>> products();
    String APIv1 = "/api/v1.0";

    @GET(APIv1 + "/token")
    Call<AuthResponse> getToken(@Header("Authorization") String auth);

    @POST(APIv1 + "/users")
    Call<RegistrationResponse> registerUser(@Body RegistrationBody registrationBody);
}
