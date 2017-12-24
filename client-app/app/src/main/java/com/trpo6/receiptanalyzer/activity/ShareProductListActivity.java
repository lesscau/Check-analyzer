package com.trpo6.receiptanalyzer.activity;

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
import com.trpo6.receiptanalyzer.model.ItemsSync;
import com.trpo6.receiptanalyzer.utils.AppToolbar;
import com.trpo6.receiptanalyzer.utils.AuthInfo;
import com.trpo6.receiptanalyzer.utils.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareProductListActivity extends AppCompatActivity {

    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_product_list);

        api = RetroClient.getApiService();
        Toolbar toolbar = AppToolbar.setToolbar(this, AuthInfo.getTableKey());
        Drawer drawer = AppToolbar.setMenu(this);

        ExpandingList expandingList = (ExpandingList) findViewById(R.id.expanding_list_main);

        //final Map<String,ArrayList<ItemsSync>> userProducts = new HashMap<>();

        ArrayList<ItemsSync.ItemSync> itemSyncs = new ArrayList<>();
        for(int i = 0; i < ProductListActivity.producListItems.size(); ++i){
            Item item = ProductListActivity.producListItems.get(i);
            ItemsSync.ItemSync itemSync = new ItemsSync.ItemSync(item.getName(),i,item.getQuantity());
            itemSyncs.add(itemSync);
        }

        final ItemsSync itemsSync = new ItemsSync();
        for(String user: ProductListActivity.tempUsers){
            Log.i("SyncUserItems",user+" "+itemsSync.toString());
            itemsSync.addSyncData(new ItemsSync.SyncUserItems(user, itemSyncs));
        }

        // заполнение списка продуктов
        for (final Item productListItem : ProductListActivity.producListItems) {
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
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        String name = tw.getText().toString();

                        itemsSync.setItemsByUser(name,productListItem.getName(),newVal);
                        //according to server api
                        if(name.equals(AuthInfo.getName())) name = "null";

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
                                Log.i("Sync: ","success");
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("err1", t.toString());
                            }
                        });
                    }
                });

            }
            item.setIndicatorColorRes(R.color.material_drawer_primary);
            item.setIndicatorIcon(getResources().getDrawable(R.drawable.ic_expand_more_white_24dp));
        }

    }
}
