package com.trpo6.receiptanalyzer.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lessc on 05.12.2017.
 */

public class DisconnectFromTableResponse {
    @SerializedName("table_id")
    @Expose
    String table_id;
    @SerializedName("user_id")
    @Expose
    String user_id;
}
