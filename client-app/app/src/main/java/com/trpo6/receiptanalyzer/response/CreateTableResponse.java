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

    @SerializedName("table_key")
    @Expose
    private String tableKey;
}
