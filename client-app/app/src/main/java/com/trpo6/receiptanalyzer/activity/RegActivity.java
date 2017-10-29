package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.activity.MainActivity;
/**
 * Окно регистрации
 * */
public class RegActivity extends AppCompatActivity {
    /**Запуск окна регистрации*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
    }
    /**Действия, совершаемые после нажатия на кнопку*/
    public void menuOpen(View view) {
        /**Объект Intent для вызова новой Activity*/
        Intent intent = new Intent(this, MainActivity.class);
        /**Запуск activity*/
        startActivity(intent);
    }
}
