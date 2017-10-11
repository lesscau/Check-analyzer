package com.trpo6.receiptanalyzer;

/**
 * Информация о продукте
 */

public class Product {
    private String name;        // наименование продукта
    private int totalCount;     // количество единиц купленного продукта (не реализовано)
    private int selectedCount;  // количество единиц продукта, выбранных пользователем
    private float price;        // стоимость одной единицы продукта

    Product(String name, int cnt,float price){
        this.name = name;
        this.selectedCount = 0;
        this.totalCount = cnt;
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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
