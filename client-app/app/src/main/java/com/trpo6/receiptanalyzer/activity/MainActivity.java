package com.trpo6.receiptanalyzer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.response.CreateTableResponse;
import com.trpo6.receiptanalyzer.utils.AppToolbar;
import com.trpo6.receiptanalyzer.utils.AuthInfo;
import com.trpo6.receiptanalyzer.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Главное меню
 */
public class MainActivity extends AppCompatActivity {

    private Drawer drawerResult = null;
    private ApiService api;
    /**
     * Запуск главного меню
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = AppToolbar.setToolbar(this, getString(R.string.app_name));
        Drawer drawer = AppToolbar.setMenu(this);
        api = RetroClient.getApiService();
        // проверяем, была ли ранее выполнена авторизация
        if (!AuthInfo.isAuthorized(this)) {
            // Окно логина/регистрации
            Intent intent = new Intent(this, FirstActivity.class);
            // запуск activity
            startActivity(intent);
        }
        // проверяем, подключен ли пользователь к столу
        connectToTable();
    }

    /**
     * Подключение к созданному столу
     * @param view
     */
    public void connectTable(View view){
        Intent intent = new Intent("connectTable");
        startActivity(intent);
    }

    /**
     * Создание нового стола
     * @param view
     */
    public void createTable(View view){
        if (!NetworkUtils.checkConnection(getApplicationContext())) {
            Log.e("error", "can not connect");
            return;
        }

        Call<CreateTableResponse> call = api.createTable(AuthInfo.getKey());
        call.enqueue(new Callback<CreateTableResponse>() {
            @Override
            public void onResponse(Call<CreateTableResponse> call, Response<CreateTableResponse> response) {
                Log.i("table resp",response.toString()+response.body());
                if(!response.isSuccessful()) {
                    NetworkUtils.showErrorResponseBody(getApplicationContext(),response);
                    return;
                }
                String tableKey = response.body().getTableKey();
                AuthInfo.keyTableSave(getApplicationContext(),tableKey);
                Log.i("tableCode",tableKey);
                Intent intent = new Intent("createTable");
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<CreateTableResponse> call, Throwable t) {
                Log.e("err1", t.toString());
            }
        });
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
     * Переход к редактрованию стола, если юзер подключен к столу
     */
    private void connectToTable(){
        // проверяем, подключен ли пользователь к столу
        // если подключен - открываем список стола
        if(AuthInfo.isConnectedToTable(this)){
            Intent intent = new Intent("productlist");
            startActivity(intent);
        }
        Call<CreateTableResponse> call = api.getTable(AuthInfo.getKey());
        call.enqueue(new Callback<CreateTableResponse>() {
            @Override
            public void onResponse(Call<CreateTableResponse> call, Response<CreateTableResponse> response) {
                Log.i("table resp",response.toString()+response.body());
                if(!response.isSuccessful()) {
                    NetworkUtils.showErrorResponseBody(getApplicationContext(),response);
                    return;
                }
                String tableKey = response.body().getTableKey();
                AuthInfo.keyTableSave(getApplicationContext(),tableKey);
                Log.i("tableCode",tableKey);
                Intent intent = new Intent("productlist");
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<CreateTableResponse> call, Throwable t) {
                Log.e("err1", t.toString());
            }
        });
    }

    /**
     * Игнорирование нажатия кнопки "назад"
     */
    @Override
    public void onBackPressed() {}
}