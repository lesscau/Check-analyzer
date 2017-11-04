package com.trpo6.receiptanalyzer.model;

/**
 * Created by lessc on 03.11.2017.
 */

public class RegistrationBody {
    public String username;
    public String phone;
    public Integer fts_key;
    public String password;

    public RegistrationBody(String username, String phone, Integer fts_key, String password) {
        this.username = username;
        this.phone = phone;
        this.fts_key = fts_key;
        this.password = password;
    }


}
