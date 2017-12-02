package com.trpo6.receiptanalyzer.utils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
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
import com.trpo6.receiptanalyzer.activity.MainActivity;

/**
 * Created by lessc on 01.12.2017.
 */

public class AppToolbar extends AppCompatActivity {

    public static Toolbar mActionBarToolbar;

    public static Toolbar setToolbar(AppCompatActivity app, String title) {
        mActionBarToolbar = (Toolbar) app.findViewById(R.id.toolbar_actionbar);
        if (title.isEmpty()) {
            title = " ";
        }
        mActionBarToolbar.setTitle(title);
        app.setSupportActionBar(mActionBarToolbar);
        if (app.getSupportActionBar() != null) {
            Log.i("toolbar", "not null");
            app.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else
            Log.e("Toolbar", "is null");
        return mActionBarToolbar;
    }

    public static Drawer setMenu(AppCompatActivity app){
        /**
         * Создание пунктов меню
         */
        Drawer drawerResult = new DrawerBuilder()
                .withActivity(app)
                .withToolbar(mActionBarToolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        // На главную
                        new PrimaryDrawerItem()
                                .withName(R.string.toMain)
                                .withIcon(FontAwesome.Icon.faw_home)
                                .withIdentifier(1),
                        // История
                        new PrimaryDrawerItem()
                                .withName(R.string.history)
                                .withIcon(FontAwesome.Icon.faw_history)
                                .withIdentifier(2),
                        new DividerDrawerItem(),
                        //Выход
                        new SecondaryDrawerItem()
                                .withName(R.string.logout)
                                .withIcon(FontAwesome.Icon.faw_sign_out)
                                .withIdentifier(3)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1) {
                            //Intent intent = new Intent(app, MainActivity.class);
                            // запуск activity
                            //startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 3) {
                            //MainActivity.logOut(view);
                            return true;
                        }
                        return false;
                    }

                })
                .build();
        return drawerResult;
    }
}
