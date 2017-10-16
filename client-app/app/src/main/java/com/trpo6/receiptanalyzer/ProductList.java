package com.trpo6.receiptanalyzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatProperty;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
public class ProductList extends AppCompatActivity {

     private static final String TAG = "MyApp";

    ProductAdapter adapter;
    ArrayList<Product> products = new ArrayList();

    ListView productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // будет переделано в работу с БД
        if(products.size()==0){
            products.add(new Product("Картофель", 10, 5));
            products.add(new Product("Чай",  10, 5));
            products.add(new Product("Яйца",  10, 5));
            products.add(new Product("Молоко", 10, 5));
            products.add(new Product("Макароны",  10, 5));
        }

        productList = (ListView) findViewById(R.id.productList);
        adapter = new ProductAdapter(this, R.layout.list_item, products);
        productList.setAdapter(adapter);
    }

    // Обработчик кнопки добавления продукта
    public void add(View view){
        Log.i(TAG,"go to add");
        // Получение названия
        EditText productEditText = (EditText) findViewById(R.id.addProduct);
        String product = productEditText.getText().toString();
        if(product.isEmpty() && products.contains(product)) return;

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
        products.add(new Product(product,count,price));
        productEditText.setText("");
        countEditText.setText("");
        priceEditText.setText("");
        adapter.notifyDataSetChanged();
    }

    // Обработчик кнопки удаления продукта
    public void remove(View view) {
        products.remove(productList.getPositionForView(view));
        adapter.notifyDataSetChanged();
    }
}