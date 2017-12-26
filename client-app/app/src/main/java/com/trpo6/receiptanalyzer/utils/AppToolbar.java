package com.trpo6.receiptanalyzer.utils;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.activity.FirstActivity;
import com.trpo6.receiptanalyzer.activity.HistoryActivity;
import com.trpo6.receiptanalyzer.activity.MainActivity;
import com.trpo6.receiptanalyzer.activity.ProductListActivity;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.response.DisconnectFromTableResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public static Drawer setMenu(final AppCompatActivity app){
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(app)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(AuthInfo.getName())
                                .withIcon(new IconicsDrawable(app)
                                        .icon(FontAwesome.Icon.faw_user)
                                        .color(Color.WHITE)
                                        .sizeDp(15))
                )
                .build();

        /**
         * Создание пунктов меню
         */

        Drawer drawerResult = new DrawerBuilder()
                .withActivity(app)
                .withToolbar(mActionBarToolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .withSliderBackgroundColorRes(R.color.md_white_1000)
                .addDrawerItems(
                        // На главную
                        new PrimaryDrawerItem()
                                .withName(R.string.toMain)
                                .withIcon(FontAwesome.Icon.faw_home)
                                .withIdentifier(1),

                        // История
                        new PrimaryDrawerItem()
                                .withName("История")
                                .withIcon(FontAwesome.Icon.faw_history)
                                .withIdentifier(2),

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
                            final Intent mainIntent = new Intent(app, MainActivity.class);
                            if (!NetworkUtils.checkConnection(app)) {
                                Log.e("error", "can not connect");
                                return false;
                            }

                            ApiService api = RetroClient.getApiService();
                            Call<DisconnectFromTableResponse> call = api.disconnectFromTable(AuthInfo.getKey());
                            call.enqueue(new Callback<DisconnectFromTableResponse>() {
                                @Override
                                public void onResponse(Call<DisconnectFromTableResponse> call, Response<DisconnectFromTableResponse> response) {
                                    if (!response.isSuccessful()){
                                        NetworkUtils.showErrorResponseBody(app,response);
                                        return;
                                    }
                                    Log.i("table resp",response.toString());
                                    AuthInfo.keyTableClear(app);
                                    Intent intent = new Intent(app,MainActivity.class);
                                    app.startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<DisconnectFromTableResponse> call, Throwable t) {
                                    Log.e("err1", t.toString());
                                }
                            });
                        }

                        if (drawerItem.getIdentifier() == 2){
                            app.startActivity(new Intent(app, HistoryActivity.class));
                        }

                        if (drawerItem.getIdentifier() == 3) {
                            // стираем старый токен
                            AuthInfo.authClear(app);

                            // переход к активити логина
                            final Intent intent = new Intent(app, FirstActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            app.startActivity(intent);

                            return true;
                        }
                        return false;
                    }
                })
                .build();
        return drawerResult;
    }
}
