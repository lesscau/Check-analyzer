package com.trpo6.receiptanalyzer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс для инициализации Retrofit
 */

public class RetroClient {
    /** Base URL */
    private static final String ROOT_URL =  "http://10.0.3.2:5000";

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
