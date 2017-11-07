package com.trpo6.receiptanalyzer.model;

/**
 * Created by lessc on 07.11.2017.
 */

public class SignUpBody {
    private String name;
    private String email;
    private String phone;
    transient private String password;

    public SignUpBody(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
