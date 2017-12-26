package com.trpo6.receiptanalyzer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lessc on 25.12.2017.
 */

public class ItemsAck {

    @SerializedName("items")
    @Expose
    private List<ItemAck> items = new ArrayList<>();

    public List<ItemAck> getItems() {
        return items;
    }

    public void setItems(List<ItemAck> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ItemsAck{\n" +
                "items=" + items +
                "}\n";
    }

    public static class ItemAck {

        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("temp_username")
        @Expose
        private String tempUsername;
        @SerializedName("quantity")
        @Expose
        private Integer quantity;
        @SerializedName("id")
        @Expose
        private Integer id;

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTempUsername() {
            return tempUsername;
        }

        public void setTempUsername(String tempUsername) {
            this.tempUsername = tempUsername;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ItemAck{" +
                    "price=" + price +
                    ", name='" + name + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}
