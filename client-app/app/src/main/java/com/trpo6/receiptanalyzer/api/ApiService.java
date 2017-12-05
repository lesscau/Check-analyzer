package com.trpo6.receiptanalyzer.api;

import com.trpo6.receiptanalyzer.model.ConnectTableRequest;
import com.trpo6.receiptanalyzer.response.AuthResponse;
import com.trpo6.receiptanalyzer.response.CreateTableResponse;
import com.trpo6.receiptanalyzer.model.Items;
import com.trpo6.receiptanalyzer.model.ConfirmSignUpBody;
import com.trpo6.receiptanalyzer.response.DisconnectFromTableResponse;
import com.trpo6.receiptanalyzer.response.RegistrationResponse;
import com.trpo6.receiptanalyzer.model.SignUpBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Интерфейс для реализации запросов
 */

public interface ApiService {

    String APIv1 = "/api/v1.0";

    /** Получение токена */
    @GET(APIv1 + "/token")
    Call<AuthResponse> getToken(@Header("Authorization") String auth);

    /** Регистрация нового пользователя */
    @POST(APIv1 + "/fts/users")
    Call<RegistrationResponse> signUp(@Body SignUpBody signUpBody);

    /** Код подтверждения для регистрации нового пользователя */
    @POST(APIv1 + "/users")
    Call<RegistrationResponse> registerUser(@Body ConfirmSignUpBody confirmSignUpBody);

    /** Создание стола */
    @POST(APIv1 + "/tables")
    Call<CreateTableResponse> createTable(@Header("Authorization") String auth);

    /** Подключение к столу */
    @POST(APIv1 + "/tables/users")
    Call<DisconnectFromTableResponse> connectTable(@Header("Authorization") String auth, @Body CreateTableResponse createTableResponse);

    /** Проверка на подключение к столу */
    @GET(APIv1 + "/tables/me")
    Call<CreateTableResponse> getTable(@Header("Authorization") String auth);

    /** Отключение от стола */
    @DELETE(APIv1+"/tables/users")
    Call<DisconnectFromTableResponse> disconnectFromTable(@Header("Authorization") String auth);

    /** Список продуктов из чека */
    @GET(APIv1 + "/fts/receipts")
    Call<Items> getReceipt(@Header("Authorization") String auth,
                           @Query("fn") String fn, @Query("fd") String fd, @Query("fp") String fp);
}
