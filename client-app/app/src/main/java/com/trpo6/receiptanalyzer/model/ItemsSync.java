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
        private List<SyncUserItems> syncData = null;

    public ItemsSync() {
        this.syncData = new ArrayList<>();
    }

    public List<SyncUserItems> getSyncData() {
            return syncData;
        }

        public void addSyncData(SyncUserItems syncUserItems){
            syncData.add(syncUserItems);
        }

        public void setSyncData(List<SyncUserItems> syncData) {
            this.syncData = syncData;
        }

        public void setItemsByUser(String username, String productname, Integer newcount){
            for (SyncUserItems items : syncData)
                if(items.getTempUsername().equals(username) || username.equals(AuthInfo.getName())) {
                    for (ItemSync itemsSync : items.getItems())
                        if (itemsSync.getName().equals(productname)) {
                            itemsSync.setCount(newcount);
                            break;
                        }
                    break;
                }
        }


    @Override
    public String toString() {
        return "ItemsSync{" +
                "syncData=" + syncData.toString() +
                '}';
    }

    static public class SyncUserItems {

            @SerializedName("temp_username")
            @Expose
            private String tempUsername;
            @SerializedName("items")
            @Expose
            private List<ItemSync> items = null;

            public SyncUserItems(String tempUsername, List<ItemSync> items) {
                if (tempUsername.equals(AuthInfo.getName()))
                    this.tempUsername = "null";
                else
                    this.tempUsername = tempUsername;
                this.items = items;
            }

            public String getTempUsername() {
                return tempUsername;
            }

            public void setTempUsername(String tempUsername) {
                this.tempUsername = tempUsername;
            }

            public List<ItemSync> getItems() {
                return items;
            }

            public void setItems(List<ItemSync> items) {
                this.items = items;
            }


            @Override
            public String toString() {
                return "SyncUserItems{" +
                        "tempUsername='" + tempUsername + '\'' +
                        ", items=" + items.toString() +
                        '}';
            }
        }

        static public class ItemSync {
            private transient String name;

            /**
             * номер продукта
             */
            @SerializedName("product_id")
            @Expose
            private Integer product_id;

            /**
             * количество единиц купленного продукта (не реализовано)
             */
            @SerializedName("count")
            @Expose
            private Integer count;

            public ItemSync(String name, Integer product_id) {
                this.name = name;
                this.product_id = product_id;
                this.count = 0;
            }

            public void setCount(Integer count) {
                Log.i("Set count ", ""+count);
                this.count = count;
            }

            public String getName() {
                return name;
            }

            @Override
            public String toString() {
                return "ItemSync{" +
                        "name='" + name + '\'' +
                        ", product_id=" + product_id +
                        ", count=" + count +
                        '}';
            }
        }
}
