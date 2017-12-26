package com.trpo6.receiptanalyzer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lessc on 25.12.2017.
 */

public class TotalUserPrice {
    @SerializedName("users")
    @Expose
    private List<TotalUser> users = new ArrayList<>();

    public List<TotalUser> getUsers() {
        return users;
    }

    public void setUsers(List<TotalUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "TotalUserPrice{" +
                "users=" + users +
                '}';
    }

    public static class TotalItem {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("quantity")
        @Expose
        private Integer quantity;
        @SerializedName("temp_username")
        @Expose
        private String tempUsername;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getTempUsername() {
            return tempUsername;
        }

        public void setTempUsername(String tempUsername) {
            this.tempUsername = tempUsername;
        }

    }

    public static class Total {

        @SerializedName("temp_username")
        @Expose
        private String tempUsername;
        @SerializedName("total")
        @Expose
        private Integer total;

        public String getTempUsername() {
            return tempUsername;
        }

        public void setTempUsername(String tempUsername) {
            this.tempUsername = tempUsername;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "Total{" +
                    "tempUsername='" + tempUsername + '\'' +
                    ", total=" + total +
                    '}';
        }
    }


    public static class TotalUser {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("total")
        @Expose
        private List<Total> total = null;

        public String getUsername() {
            return username;
        }

        public void setUsername(String id) {
            this.username = username;
        }

        public List<Total> getTotal() {
            return total;
        }

        public void setTotal(List<Total> total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "TotalUser{" +
                    "id=" + username +
                    ", total=" + total +
                    '}';
        }
    }
}
