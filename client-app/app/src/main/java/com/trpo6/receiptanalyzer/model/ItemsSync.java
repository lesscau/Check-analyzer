package com.trpo6.receiptanalyzer.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.trpo6.receiptanalyzer.utils.AuthInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lessc on 24.12.2017.
 */

public class ItemsSync {

    @SerializedName("sync_data")
    @Expose
    private SyncUserItem syncData;

    public ItemsSync(SyncUserItem syncData) {
        this.syncData = syncData;
        this.syncData.setTempUsername((syncData.getTempUsername().equals(AuthInfo.getName())) ? "" : syncData.getTempUsername());
    }

    public SyncUserItem getSyncData() {
        return syncData;
    }

    public void setSyncData(SyncUserItem syncData) {
        this.syncData = syncData;
    }

    @Override
    public String toString() {
        return "ItemsSync{" +
                "syncData=" + syncData +
                '}';
    }

    static public class SyncUserItem {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("temp_username")
        @Expose
        private String tempUsername;
        @SerializedName("product_name")
        @Expose
        private String productName;

        public SyncUserItem(Integer count, String tempUsername, String productName) {
            this.count = count;
            this.tempUsername = tempUsername;
            this.productName = productName;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getTempUsername() {
            return tempUsername;
        }

        public void setTempUsername(String tempUsername) {
            this.tempUsername = tempUsername;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        @Override
        public String toString() {
            return "SyncUserItems{" +
                    "count=" + count +
                    ", tempUsername='" + tempUsername + '\'' +
                    ", productName='" + productName + '\'' +
                    '}';
        }
    }

}
