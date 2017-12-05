package com.trpo6.receiptanalyzer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.response.AuthResponse;

import org.json.JSONObject;

import java.security.PublicKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 27.10.2017.
 */

public class NetworkUtils {
    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public static boolean checkConnection(@NonNull Context context) {
        return ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    /** Снэкбар для отображения сообщения об ошибке ответа сервера */
    public static void showErrorResponseBody(@NonNull Context context, Response response){
        try {
            if(!AuthInfo.getName().isEmpty() && !AuthInfo.getPassword().isEmpty() &&
            (response.code()==401 || response.message().toString().equals("Unauthorized access"))){
                getToken(context,AuthInfo.getName(),AuthInfo.getPassword());
                return;
            }

            JSONObject jObjError = new JSONObject(response.errorBody().string());
            Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
            Log.i("Failure responce:", response.toString()+jObjError.getString("message"));
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /** Запрос на обновление токена */
    private static void getToken(@NonNull final Context context, final String name, final String password){
        ApiService api = RetroClient.getApiService();

        String authorizationString = "Basic " + Base64.encodeToString(
                (name+":"+password).getBytes(),Base64.NO_WRAP);
        Call<AuthResponse> call = api.getToken(authorizationString);
        Log.i("i",call.request().toString());
        /**
         * Enqueue Callback will be call when get response...
         */
        call.enqueue(new Callback<AuthResponse>(){
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    /**
                     * Token Got Successfully - Open main menu
                     */
                    Log.i("success", response.toString());

                    // Сохранение токена
                    String token = "Bearer "+response.body().getToken();
                    AuthInfo.authSave(context,name,password,token);
                    Log.i("token:", token);

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        Log.i("Failure responce:", response.toString()+jObjError.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("err1", t.toString());
                Toast toast = Toast.makeText(context,
                        "Error: "+t.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
