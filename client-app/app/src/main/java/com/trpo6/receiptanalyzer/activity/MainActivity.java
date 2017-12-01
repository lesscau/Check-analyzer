package com.trpo6.receiptanalyzer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.utils.AuthInfo;

/**
 * Главное меню
 */
public class MainActivity extends AppCompatActivity {

    //private FloatingNavigationView mFloatingNavigationView;
    private Drawer drawerResult = null;
    /**
     * Запуск главного меню
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        if(getSupportActionBar() != null){
            Log.i("toolbar","not null");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else
            Log.e("Toolbar","is null");

        drawerResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mActionBarToolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                    new PrimaryDrawerItem()
                        .withName(R.string.toMain)
                        .withIcon(FontAwesome.Icon.faw_home)
                        .withIdentifier(1),
                    new PrimaryDrawerItem()
                        .withName(R.string.history)
                        .withIcon(FontAwesome.Icon.faw_history)
                        .withIdentifier(2),
                    new DividerDrawerItem(),
                    new SecondaryDrawerItem()
                        .withName(R.string.logout)
                        .withIcon(FontAwesome.Icon.faw_sign_out)
                        .withIdentifier(3)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier()==3){
                            logOut(view);
                            return true;
                        }
                        return false;
                    }

                })
                .build();

        // проверяем, была ли ранее выполнена авторизация
        if (!AuthInfo.isAuthorized(this)) {
            // Окно логина/регистрации
            Intent intent = new Intent(this, FirstActivity.class);

            // запуск activity
            startActivity(intent);
        }

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