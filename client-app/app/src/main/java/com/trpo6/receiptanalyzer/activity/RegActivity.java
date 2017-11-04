package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.model.RegistrationBody;
import com.trpo6.receiptanalyzer.model.RegistrationResponse;
import com.trpo6.receiptanalyzer.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegActivity extends AppCompatActivity {

    private EditText username;
    private EditText phone;
    private EditText password;
    private EditText ftsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        username = (EditText) findViewById(R.id.usernameTextEdit);
        phone = (EditText) findViewById(R.id.phoneTextEdit);
        password = (EditText) findViewById(R.id.passwordTextEdit);
        ftsKey = (EditText) findViewById(R.id.fts_keyTextEdit);
    }

    /** действия, совершаемые после нажатия на кнопку */
    public void menuOpen(View view) {
        // Проверяем, все ли поля заполнены
        String _username = username.getText().toString();
        String _phone = phone.getText().toString();
        String _ftsKey = ftsKey.getText().toString();
        String _password = password.getText().toString();
        if (_username.length() == 0 | _phone.length() == 0 | _ftsKey.length() == 0 | _password.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please fill in all fields ", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        // Создаем объект Intent для вызова новой Activity
        final Intent intent = new Intent(this, MainActivity.class);
        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            ApiService api = RetroClient.getApiService();
            //User user  = new User("89112356232","pass");
            RegistrationBody body = new RegistrationBody(_username,_phone,Integer.parseInt(_ftsKey),_password);
            Call<RegistrationResponse> call = api.registerUser(body);
            Log.i("i",username.getText().toString()+phone.getText().toString()+
                    ftsKey.getText().toString()+password.getText().toString());
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
                        Log.i("success", response.toString());

                        // запуск activity
                        startActivity(intent);
                    } else {
                        Log.e("err0", response.toString());
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Error: "+response.message(), Toast.LENGTH_LONG);
                        toast.show();
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
}
