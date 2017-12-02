package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.utils.AppToolbar;

public class CreateTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);
        Toolbar toolbar = AppToolbar.setToolbar(this,"Код вашего стола");
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
