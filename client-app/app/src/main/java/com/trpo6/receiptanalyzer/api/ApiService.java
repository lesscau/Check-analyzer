package com.trpo6.receiptanalyzer.api;

import com.trpo6.receiptanalyzer.model.Item;
import com.trpo6.receiptanalyzer.model.ItemsSync;
import com.trpo6.receiptanalyzer.model.SignUpBody;
import com.trpo6.receiptanalyzer.response.AuthResponse;
import com.trpo6.receiptanalyzer.response.CreateTableResponse;
import com.trpo6.receiptanalyzer.model.Items;
import com.trpo6.receiptanalyzer.model.ConfirmSignUpBody;
import com.trpo6.receiptanalyzer.response.DisconnectFromTableResponse;
import com.trpo6.receiptanalyzer.response.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Интерфейс для реализации запросов
 */

public interface ApiService {

    String APIv1 = "/api/v1.0";

    /**
     * Получение токена
     * @param auth токен
     * @return
     */
    @GET(APIv1 + "/token")
    Call<AuthResponse> getToken(@Header("Authorization") String auth);

    /**
     * Регистрация нового пользователя
     * @param signUpBody данные для регистрации
     * @return
     */
    @POST(APIv1 + "/fts/users")
    Call<RegistrationResponse> signUp(@Body SignUpBody signUpBody);

    /**
     * Код подтверждения для регистрации нового пользователя
     * @param confirmSignUpBody код подтверждения из sms
     * @return
     */
    @POST(APIv1 + "/users")
    Call<RegistrationResponse> registerUser(@Body ConfirmSignUpBody confirmSignUpBody);

    /**
     * Создание стола
     * @param auth токен
     * @return
     */
    @POST(APIv1 + "/tables")
    Call<CreateTableResponse> createTable(@Header("Authorization") String auth);

    /**
     * Подключение к столу
     * @param auth токен
     * @param createTableResponse код стола
     * @return
     */
    @POST(APIv1 + "/tables/users")
    Call<DisconnectFromTableResponse> connectTable(@Header("Authorization") String auth, @Body CreateTableResponse createTableResponse);

    /**
     * Проверка на подключение к столу
     * @param auth токен
     * @return
     */
    @GET(APIv1 + "/tables/me")
    Call<CreateTableResponse> getTable(@Header("Authorization") String auth);

    /**
     * Отключение от стола
     * @param auth токен
     * @return
     */
    @DELETE(APIv1+"/tables/users")
    Call<DisconnectFromTableResponse> disconnectFromTable(@Header("Authorization") String auth);

    /**
     * Выбранное количество кажого товара каждым пользователем
     * @param auth токен
     * @param itemsSync
     * @return
     */
    @POST(APIv1 + "/tables/sync")
    Call<String> syncData(@Header("Authorization") String auth, @Body ItemsSync itemsSync);

    /**
     * Количество свободных товаров
     * @param auth
     * @return
     */
    @GET(APIv1 + "tables/ack")
    Call<Items> ackData(@Header("Authorization") String auth);

    /**
     * Список продуктов из чека
     * @param auth токен
     * @param fn
     * @param fd
     * @param fp
     * @return
     */
    @GET(APIv1 + "/fts/receipts")
    Call<Items> getReceipt(@Header("Authorization") String auth,
                           @Query("fn") String fn, @Query("fd") String fd, @Query("fp") String fp);

    /** Список продуктов из текущего стола */
    @GET(APIv1 + "/products")
    Call<Items> getTableProducts(@Header("Authorization") String auth);

    /**
     * Удаление продукта из списка стола
     * @param auth
     * @param deletedItem удаляемый продукт
     * @return
     */
    @HTTP(method = "DELETE", path = APIv1 + "/products", hasBody = true)
    Call<String> deleteProductFromTable(@Header("Authorization") String auth, @Body Item.deletedItem deletedItem);

    /**
     * Добавление нового продукта
     * @param auth
     * @param addedItem
     * @return
     */
    @POST(APIv1 + "/products")
    Call<String> addProductToTable(@Header("Authorization") String auth, @Body Item.addedItem addedItem);
}
