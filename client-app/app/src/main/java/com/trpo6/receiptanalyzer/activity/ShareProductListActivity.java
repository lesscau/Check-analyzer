package com.trpo6.receiptanalyzer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.mikepenz.materialdrawer.Drawer;
import com.shawnlin.numberpicker.NumberPicker;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.model.Item;
import com.trpo6.receiptanalyzer.utils.AppToolbar;
import com.trpo6.receiptanalyzer.utils.AuthInfo;

public class ShareProductListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_product_list);

        Toolbar toolbar = AppToolbar.setToolbar(this, AuthInfo.getTableKey());
        Drawer drawer = AppToolbar.setMenu(this);

        ExpandingList expandingList = (ExpandingList) findViewById(R.id.expanding_list_main);
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.user_number_picker);

        // заполнение списка продуктов
        for (Item productListItem : ProductListActivity.producListItems) {
            ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);
            TextView list_item = (TextView) item.findViewById(R.id.list_item_title);
            list_item.setText(productListItem.getName());
            item.createSubItems(ProductListActivity.tempUsers.size());
            // заполнение подсписка пользователей
            for(int i = 0; i<ProductListActivity.tempUsers.size(); ++i){
                View subItem = item.getSubItemView(i);
                ((NumberPicker) subItem.findViewById(R.id.user_number_picker)).setMaxValue(productListItem.getQuantity());
                ((TextView) subItem.findViewById(R.id.sub_title)).setText(ProductListActivity.tempUsers.get(i));
            }
            item.setIndicatorColorRes(R.color.material_drawer_primary_dark);
            item.setIndicatorIcon(getResources().getDrawable(R.drawable.ic_expand_more_white_24dp));

        }


        //item.createSubItems(5);


        //get a sub item View
    }
}
