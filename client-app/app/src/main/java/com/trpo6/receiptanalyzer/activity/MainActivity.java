package com.trpo6.receiptanalyzer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.trpo6.receiptanalyzer.R;

/**
 * Главное меню
 */
public class MainActivity extends AppCompatActivity {
    /**Запуск главного меню*/
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
    /** Переход к первой активити, если нажали LogOut */
    public void logOut(View view){
        Intent intent = new Intent(this, FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {}
}