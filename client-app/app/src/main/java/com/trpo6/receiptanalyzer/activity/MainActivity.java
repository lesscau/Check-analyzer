package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.model.User;
import com.trpo6.receiptanalyzer.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Переход к активности редактирования списка продуктов */
    public void addItemsWindow(View view){
        Intent intent = new Intent("productlist");
        startActivity(intent);
    }

    /** Переход к активности сканирования QR-кода с чека */
    public void scanQRcode(View view){
        Intent intent = new Intent("QRscanner");
        startActivity(intent);
    }
}