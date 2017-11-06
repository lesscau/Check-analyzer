package com.trpo6.receiptanalyzer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Информация о продукте
 */

public class Item {
    /** наименование продукта */
    @SerializedName("name")
    @Expose
    private String name;

    /** количество единиц купленного продукта (не реализовано) */
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    /** количество единиц продукта, выбранных пользователем */
    transient private int selectedCount;

    /** стоимость одной единицы продукта */
    @SerializedName("price")
    @Expose
    private float price;

    public Item(String name, int cnt, float price){
        this.name = name;
        this.selectedCount = 0;
        this.quantity = cnt;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
