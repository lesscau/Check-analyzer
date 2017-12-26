package com.trpo6.receiptanalyzer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Информация о продукте
 */

public class Item {
    /**
     * наименование продукта
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * количество единиц купленного продукта
     */
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    /**
     * количество единиц продукта, выбранных пользователем
     */
    transient private int selectedCount;

    /**
     * стоимость одной единицы продукта
     */
    @SerializedName("price")
    @Expose
    private float price;

    /**
     * Инициализурет поля {@link Item#name}, {@link Item#selectedCount}, {@link Item#quantity}, {@link Item#price}
     */
    public Item(String name, int cnt, float price) {
        this.name = name;
        this.selectedCount = 0;
        this.quantity = cnt;
        this.price = price;
    }

    /**
     * Возвращает наименование продукта
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает наименование продукта
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает количество продуктов, выбранных пользователем
     */
    public int getSelectedCount() {
        return selectedCount;
    }

    /**
     * Устанавливает количество продуктов, выбранных пользователем
     */
    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    /**
     * Возвращает цену единицы продукта
     */
    public float getPrice() {
        return price;
    }

    /**
     * Устанавливает цену единицы продукта
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Возвращает количество единиц купленного продукта
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Устанавливает количество единиц купленного продукта
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static class deletedItem {
        /**
         * наименование продукта
         */
        @SerializedName("product_name")
        @Expose
        private String name;

        /**
         * стоимость одной единицы продукта
         */
        @SerializedName("price")
        @Expose
        private int price;

        public deletedItem(String name, float price) {
            this.name = name;
            this.price = (int) price * 100;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "deletedItem{" +
                    "name='" + name + '\'' +
                    ", price=" + price +
                    '}';
        }
    }

    public static class addedItem {
        /**
         * наименование продукта
         */
        @SerializedName("product_name")
        @Expose
        private String name;

        /**
         * стоимость одной единицы продукта
         */
        @SerializedName("price")
        @Expose
        private int price;

        /**
         * количество единиц купленного продукта
         */
        @SerializedName("count")
        @Expose
        private int count;

        public addedItem(String name, float price, int count) {
            this.name = name;
            this.price = (int) price * 100;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public float getPrice() {
            return price;
        }

        public int getCount() {
            return count;
        }
    }
}
