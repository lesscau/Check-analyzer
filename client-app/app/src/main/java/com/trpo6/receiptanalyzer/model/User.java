package com.trpo6.receiptanalyzer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 28.10.2017.
 */

public class User {

    /** логин */
    @SerializedName("loginPhone")
    @Expose
    private String name;

    /** пароль */
    @SerializedName("smsPass")
    @Expose
    private String pass;


    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }


}
