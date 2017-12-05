package com.trpo6.receiptanalyzer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lessc on 05.12.2017.
 */

public class ConnectTableRequest {
    @SerializedName("table_key")
    @Expose
    private String tableKey;
}
