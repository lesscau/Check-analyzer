package com.trpo6.receiptanalyzer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

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

    public static void showErrorResponseBody(@NonNull Context context, Response response){
        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
            Log.i("Failure responce:", response.toString()+jObjError.getString("message"));
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
