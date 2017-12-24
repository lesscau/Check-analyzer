package com.trpo6.receiptanalyzer.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lessc on 04.12.2017.
 */

public class CreateTableResponse {
    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    @SerializedName("table_key")
    @Expose
    private String tableKey;
}