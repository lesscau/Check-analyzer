package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.model.Items;
import com.trpo6.receiptanalyzer.utils.AppToolbar;
import com.trpo6.receiptanalyzer.utils.AuthInfo;
import com.trpo6.receiptanalyzer.utils.InternetConnection;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);
        TextView textView = (TextView) findViewById(R.id.tableCodeTextView);
        textView.setText(AuthInfo.getTableKey());
        Toolbar toolbar = AppToolbar.setToolbar(this,"Код стола");
        Drawer drawer = AppToolbar.setMenu(this);
    }

    /**
     * Переход к активности редактирования списка продуктов
     */
    public void addItemsWindow(View view) {
        Intent intent = new Intent("productlist");
        startActivity(intent);
    }


}
