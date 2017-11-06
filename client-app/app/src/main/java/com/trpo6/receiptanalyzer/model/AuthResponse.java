package com.trpo6.receiptanalyzer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lessc on 06.11.2017.
 */

public class AuthResponse {
    public String getToken() {
        return token;
    }

    @SerializedName("token")
    @Expose
    private String token;
}
