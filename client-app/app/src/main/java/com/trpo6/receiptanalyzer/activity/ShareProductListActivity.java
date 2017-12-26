package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.mikepenz.materialdrawer.Drawer;
import com.shawnlin.numberpicker.NumberPicker;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.model.Item;
import com.trpo6.receiptanalyzer.model.ItemsAck;
import com.trpo6.receiptanalyzer.model.ItemsSync;
import com.trpo6.receiptanalyzer.utils.AppToolbar;
import com.trpo6.receiptanalyzer.utils.AuthInfo;
import com.trpo6.receiptanalyzer.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareProductListActivity extends AppCompatActivity {

    ApiService api;
    /**Список продуктов*/
    private final static ArrayList<Item> sharedProductListItems = ProductListActivity.producListItems;
    private static Map<Item,ArrayList<NumberPicker>> countItemMap = new LinkedHashMap<>();
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_product_list);

        api = RetroClient.getApiService();
        Toolbar toolbar = AppToolbar.setToolbar(this, AuthInfo.getTableKey());
        Drawer drawer = AppToolbar.setMenu(this);

        final ExpandingList expandingList = (ExpandingList) findViewById(R.id.expanding_list_main);

        // заполнение списка продуктов
        for (final Item productListItem : sharedProductListItems) {
            ArrayList<NumberPicker> numberPickerArrayList = new ArrayList<>();

            final ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);
            TextView list_item = (TextView) item.findViewById(R.id.list_item_title);
            list_item.setText(productListItem.getName());
            item.createSubItems(ProductListActivity.tempUsers.size());
            // заполнение подсписка пользователей
            for(int i = 0; i<ProductListActivity.tempUsers.size(); ++i){
                View subItem = item.getSubItemView(i);
                final TextView tw = (TextView) subItem.findViewById(R.id.sub_title);
                tw.setText(ProductListActivity.tempUsers.get(i));

                NumberPicker np = (NumberPicker) subItem.findViewById(R.id.user_number_picker);
                np.setMaxValue(productListItem.getQuantity());
                // initialization by 0 request
                ItemsSync.SyncUserItem syncUserItem = new ItemsSync.SyncUserItem(0, tw.getText().toString(),productListItem.getName());
                syncRequest(new ItemsSync(syncUserItem));
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        ItemsSync.SyncUserItem syncUserItem = new ItemsSync.SyncUserItem(newVal, tw.getText().toString(),productListItem.getName());
                        ItemsSync itemsSync = new ItemsSync(syncUserItem);

                        Log.i("Items sync: ",itemsSync.toString());

                        syncRequest(itemsSync);
                    }
                });
                numberPickerArrayList.add(np);
            }
            countItemMap.put(productListItem,numberPickerArrayList);
            item.setIndicatorColorRes(R.color.material_drawer_primary);
            item.setIndicatorIcon(getResources().getDrawable(R.drawable.ic_expand_more_white_24dp));
        }

        timer = new Timer();
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                Call<ItemsAck> call = api.ackData(AuthInfo.getKey());
                if (!NetworkUtils.checkConnection(getApplicationContext())) {
                    Log.e("error", "can not connect");
                    return;
                }
                call.enqueue(new Callback<ItemsAck>() {
                    @Override
                    public void onResponse(Call<ItemsAck> call, Response<ItemsAck> response) {
                        if (!response.isSuccessful()) {
                            NetworkUtils.showErrorResponseBody(getApplicationContext(), response);
                            return;
                        }
                        ItemsAck itemsAck = response.body();
                        Log.i("Ack: ",itemsAck.toString());
                        ItemsAck.ItemAck itemAck;
                        Item item;
                        for(int i = 0; i<itemsAck.getItems().size();++i) {
                            itemAck = itemsAck.getItems().get(i);
                            item = sharedProductListItems.get(i);
                            for (int j = 0; j < ProductListActivity.tempUsers.size(); ++j)
                                countItemMap.get(item).get(j).setMaxValue(countItemMap.get(item).get(j).getValue() + itemAck.getQuantity());
                        }
                    }
                    @Override
                    public void onFailure(Call<ItemsAck> call, Throwable t) {

                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 3000);
    }

    void syncRequest(final ItemsSync itemsSync){
        Call<String> call = api.syncData(AuthInfo.getKey(), itemsSync);
        if (!NetworkUtils.checkConnection(getApplicationContext())) {
            Log.e("error", "can not connect");
            return;
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    NetworkUtils.showErrorResponseBody(getApplicationContext(), response);
                    return;
                }
                Log.i("Items sync2: ", itemsSync.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("err1", t.toString());
            }
        });
    }

    public void openTotal(View view){
        Intent intent = new Intent(this,TotalActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        super.onBackPressed();
    }
}
