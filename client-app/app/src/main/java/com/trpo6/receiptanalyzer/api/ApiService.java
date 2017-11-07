package com.trpo6.receiptanalyzer.api;

import com.trpo6.receiptanalyzer.model.AuthResponse;
import com.trpo6.receiptanalyzer.model.Items;
import com.trpo6.receiptanalyzer.model.ConfirmSignUpBody;
import com.trpo6.receiptanalyzer.model.RegistrationResponse;
import com.trpo6.receiptanalyzer.model.SignUpBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Интерфейс для реализации запросов
 */

public interface ApiService {

    String APIv1 = "/api/v1.0";

    @GET(APIv1 + "/token")
    Call<AuthResponse> getToken(@Header("Authorization") String auth);

    @POST(APIv1 + "/fts/users")
    Call<RegistrationResponse> signUp(@Body SignUpBody signUpBody);

    @POST(APIv1 + "/users")
    Call<RegistrationResponse> registerUser(@Body ConfirmSignUpBody confirmSignUpBody);

    @GET(APIv1 + "/fts/receipts")
    Call<Items> getReceipt(@Header("Authorization") String auth,
                           @Query("fn") String fn, @Query("fd") String fd, @Query("fp") String fp);
}
