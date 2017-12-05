package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.Toast;

import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.response.CreateTableResponse;
import com.trpo6.receiptanalyzer.response.DisconnectFromTableResponse;
import com.trpo6.receiptanalyzer.utils.AuthInfo;
import com.trpo6.receiptanalyzer.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnectTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_table);

        final TextInputEditText tableCode = findViewById(R.id.tableCodeTextInput);
        Button nextButton = findViewById(R.id.connectTableNextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = tableCode.getText().toString();
                if (code.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Введите код стола",Toast.LENGTH_SHORT);
                    return;
                }
                final CreateTableResponse request = new CreateTableResponse();
                request.setTableKey(code);
                ApiService api = RetroClient.getApiService();
                Call<DisconnectFromTableResponse> call = api.connectTable(AuthInfo.getKey(),request);
                call.enqueue(new Callback<DisconnectFromTableResponse>() {
                    @Override
                    public void onResponse(Call<DisconnectFromTableResponse> call, Response<DisconnectFromTableResponse> response) {
                        Log.i("table resp",response.toString()+response.body());
                        if(!response.isSuccessful()) {
                            NetworkUtils.showErrorResponseBody(getApplicationContext(),response);
                            return;
                        }

                        AuthInfo.keyTableSave(getApplicationContext(),request.getTableKey());
                        Log.i("tableCode",""+request.getTableKey());
                        Intent intent = new Intent("productlist");
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<DisconnectFromTableResponse> call, Throwable t) {
                        Log.e("err1", t.toString());
                    }
                });
            }
        });
    }
}
