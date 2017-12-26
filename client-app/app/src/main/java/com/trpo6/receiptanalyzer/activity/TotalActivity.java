package com.trpo6.receiptanalyzer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.model.TotalUserPrice;
import com.trpo6.receiptanalyzer.utils.AppToolbar;
import com.trpo6.receiptanalyzer.utils.AuthInfo;
import com.trpo6.receiptanalyzer.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalActivity extends AppCompatActivity {
    private TotalUserPrice totalUserPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        Toolbar toolbar = AppToolbar.setToolbar(this, AuthInfo.getTableKey());
        Drawer drawer = AppToolbar.setMenu(this);

        ApiService api = RetroClient.getApiService();
        Call<TotalUserPrice> call = api.getTotalComputation(AuthInfo.getKey());

        final TableLayout tableLayout = findViewById(R.id.total_table_layout);

        totalUserPrice = new TotalUserPrice();
        call.enqueue(new Callback<TotalUserPrice>() {
                         @Override
                         public void onResponse(Call<TotalUserPrice> call, Response<TotalUserPrice> response) {
                             if(!response.isSuccessful()) {
                                 Log.i("Total ","code "+response.code()+" "+response.body());
                                 NetworkUtils.showErrorResponseBody(getApplicationContext(),response);
                                 return;
                             }
                             Log.i("Total result: ",response.body().toString());
                             totalUserPrice.setUsers(response.body().getUsers());
                                fillTableLayout(tableLayout,totalUserPrice);
                         }

                         @Override
                         public void onFailure(Call<TotalUserPrice> call, Throwable t) {
                             Log.e("err1", t.toString());
                         }
                     });
    }

    void fillTableLayout(TableLayout tableLayout, TotalUserPrice totalUserPrice){
        for(TotalUserPrice.TotalUser totalUser : totalUserPrice.getUsers())
            for (TotalUserPrice.Total total : totalUser.getTotal()){
                TableRow tableRow = new TableRow(this);
                TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
                TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

                // set user text view
                TextView user = new TextView(this);
                user.setTextSize(28);
                user.setTextColor(getResources().getColor(R.color.material_drawer_primary));
                user.setLayoutParams(params1);
                user.setPadding(2,5,50,5);
                user.setText((total.getTempUsername().equals(""))?AuthInfo.getName() : total.getTempUsername());

                // set price text view
                TextView price = new TextView(this);
                price.setTextSize(28);
                price.setTextColor(getResources().getColor(R.color.material_drawer_primary_dark));
                price.setLayoutParams(params1);
                price.setPadding(0,5,2,5);
                price.setText(Float.toString((float)total.getTotal()/100));

                //add text views to the table
                tableRow.addView(user,0);
                tableRow.addView(price,1);
                tableRow.setLayoutParams(params2);
                tableLayout.addView(tableRow);
            }
    }
}
