package com.trpo6.receiptanalyzer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.trpo6.receiptanalyzer.model.Item;
import com.trpo6.receiptanalyzer.adapter.ProductAdapter;
import com.trpo6.receiptanalyzer.R;

import java.util.ArrayList;
public class ProductListActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";

    ProductAdapter adapter;
    static ArrayList<Item> items = new ArrayList();
    RecyclerView productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productList = (RecyclerView) findViewById(R.id.productList);
        adapter = new ProductAdapter(items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        productList.setAdapter(adapter);
        productList.setLayoutManager(layoutManager);
        productList.setItemAnimator(itemAnimator);
    }


    // Обработчик кнопки добавления продукта
    public void add(View view){
        Log.i(TAG,"go to add");
        // Получение названия
        EditText productEditText = (EditText) findViewById(R.id.addProduct);
        String product = productEditText.getText().toString();
        if(product.isEmpty() && items.contains(product)) return;

        // Получение общего количества
        EditText countEditText = (EditText) findViewById(R.id.addCount);
        String strCount = countEditText.getText().toString();
        if (strCount.equals("")) return;
        int count = Integer.parseInt(strCount);

        // Получение цены
        EditText priceEditText = (EditText) findViewById(R.id.addPrice);
        String strPrice = priceEditText.getText().toString();
        if (strPrice.equals("")) return;
        float price = Float.parseFloat(strPrice);

        // Добавление продукта в список
        items.add(new Item(product,count,price));
        productEditText.setText("");
        countEditText.setText("");
        priceEditText.setText("");
        adapter.notifyItemInserted(items.size()-1);
    }
}