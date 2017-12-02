package com.trpo6.receiptanalyzer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.utils.AppToolbar;
import com.trpo6.receiptanalyzer.utils.AuthInfo;

/**
 * Главное меню
 */
public class MainActivity extends AppCompatActivity {

    private Drawer drawerResult = null;
    /**
     * Запуск главного меню
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = AppToolbar.setToolbar(this, getString(R.string.app_name));
        Drawer drawer = AppToolbar.setMenu(this);

        // проверяем, была ли ранее выполнена авторизация
        if (!AuthInfo.isAuthorized(this)) {
            // Окно логина/регистрации
            Intent intent = new Intent(this, FirstActivity.class);

            // запуск activity
            startActivity(intent);
        }

    }

    public void createTable(View view){
        Intent intent = new Intent("createTable");
        startActivity(intent);
    }

    /**
     * Переход к активности редактирования списка продуктов
     */
    public void addItemsWindow(View view) {
        Intent intent = new Intent("productlist");
        startActivity(intent);
    }

    /**
     * Переход к активности сканирования QR-кода с чека
     */
    public void scanQRcode(View view) {
        Intent intent = new Intent("QRscanner");
        startActivity(intent);
    }

    /**
     * Переход к первой активити, если нажали LogOut
     */
    public void logOut(View view) {
        // стираем старый токен
        AuthInfo.authClear(this);

        // переход к активити логина
        Intent intent = new Intent(this, FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /**
     * Игнорирование нажатия кнопки "назад"
     */
    @Override
    public void onBackPressed() {
        if(drawerResult.isDrawerOpen()){
            drawerResult.closeDrawer();
        }
        else{}
    }
}