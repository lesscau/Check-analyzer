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
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.utils.AppToolbar;
import com.trpo6.receiptanalyzer.utils.AuthInfo;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = AppToolbar.setToolbar(this, "История");
        Drawer drawer = AppToolbar.setMenu(this);

        ExpandingList expandingList = (ExpandingList) findViewById(R.id.history_expanding_list);

        // заполнение списка продуктов
        for (int i = 0; i < 10; ++i) {
            ExpandingItem item = expandingList.createNewItem(R.layout.history_expanding_layout);
            TextView list_item = (TextView) new TextView(this);
            list_item.setText("table " + i);
            item.createSubItems(ProductListActivity.tempUsers.size());
            Log.i("history subitems: ",""+ProductListActivity.tempUsers.size());
            // заполнение подсписка пользователей
            for (int j = 0; j < ProductListActivity.tempUsers.size(); ++j) {
                View subItem = item.getSubItemView(j);
                final TextView tw = (TextView) subItem.findViewById(R.id.history_username_text_view);
                tw.setText(ProductListActivity.tempUsers.get(j));
            }
            item.setIndicatorColorRes(R.color.material_drawer_primary);
            item.setIndicatorIcon(getResources().getDrawable(R.drawable.ic_expand_more_white_24dp));
        }

    }
}
