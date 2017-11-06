package com.trpo6.receiptanalyzer.model;

import java.util.ArrayList;

/**
 * Created by lessc on 06.11.2017.
 */

public class Items {
    ArrayList<Item> items = new ArrayList();

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     * Приведение целочисленного результата к десятичной дроби
     */
    public void correctPrice(){
        for (Item item : this.items){
            item.setPrice(item.getPrice()/100);
        }
    }
}
