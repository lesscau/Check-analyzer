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
import com.trpo6.receiptanalyzer.response.RegistrationResponse;
import com.trpo6.receiptanalyzer.model.SignUpBody;
import com.trpo6.receiptanalyzer.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText phone;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        username = (EditText) findViewById(R.id.usernameTextEdit);
        phone = (EditText) findViewById(R.id.phoneTextEdit);
        email = (EditText) findViewById(R.id.emailTextEdit);
        password = (EditText) findViewById(R.id.passwordTextEdit);
    }

    static SignUpBody signUpBody;
    /** действия, совершаемые после нажатия на кнопку */
    public void menuOpen(View view) {
        // Проверяем, все ли поля заполнены
        String _username = username.getText().toString();
        String _phone = phone.getText().toString();
        //String _ftsKey = ftsKey.getText().toString();
        String _email = email.getText().toString();
        String _password = password.getText().toString();
        if (_username.length() == 0 | _phone.length() == 0 | _email.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please fill in all fields ", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        // Создаем объект Intent для вызова новой Activity
        final Intent intent = new Intent(this, SignUpConfirmActivity.class);
        /**
         * Checking Internet Connection
         */
        if (NetworkUtils.checkConnection(getApplicationContext())) {
            ApiService api = RetroClient.getApiService();
            //User user  = new User("89112356232","pass");
            //ConfirmSignUpBody body = new ConfirmSignUpBody(_username,_phone,Integer.parseInt(_ftsKey),_password);
            signUpBody = new SignUpBody(_username,_email,_phone,_password);
            //Call<RegistrationResponse> call = api.registerUser(body);
            Call<RegistrationResponse> call = api.signUp(signUpBody);
            Log.i("i",username.getText().toString()+phone.getText().toString()+
                    email.getText().toString());
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
                        Log.i("success", response.message().toString());

                        // запуск activity
                        startActivity(intent);
                    } else {
                        NetworkUtils.showErrorResponseBody(getApplicationContext(),response);
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
