package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.model.RegistrationBody;
import com.trpo6.receiptanalyzer.model.RegistrationResponse;
import com.trpo6.receiptanalyzer.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /** Временный код для тестирования соединения с сервером по нажатию кнопки История */
        Button download = (Button) findViewById(R.id.historyButton);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Checking Internet Connection
                 */
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    ApiService api = RetroClient.getApiService();
                    //User user  = new User("89112356232","pass");
                    RegistrationBody body = new RegistrationBody("myLogin","+79112734540",590307,"sdfj");
                    Call<RegistrationResponse> call = api.registerUser(body);
                    Log.i("i",call.request().toString());
                    /**
                     * Enqueue Callback will be call when get response...
                     */
                    call.enqueue(new Callback<RegistrationResponse>() {
                        @Override
                        public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                            if (response.isSuccessful()) {
                                /**
                                 * Got Successfully
                                 */
                                //String ans = response.body().toString();
                                Log.i("success", response.toString());
                            } else {
                                Log.e("err0", response.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                            Log.e("err1", t.toString());
                        }
                    });

                } else {
                    Log.e("error","cant connect");
                }

            }
        });

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