package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.trpo6.receiptanalyzer.R;

/**
 * Стартовая страница
 */
public class FirstActivity extends AppCompatActivity {

    /**Запуск стартовой страницы*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }
    /**Переход к окну входа*/
    public void loginOpen(View view) {
        // действия, совершаемые после нажатия на кнопку
        // Создаем объект Intent для вызова новой Activity
        Intent intent = new Intent(this, LoginActivity.class);
        // запуск activity
        startActivity(intent);
    }
    /**Переход к окну регистрации*/
    public void regOpen(View view) {
        // действия, совершаемые после нажатия на кнопку
        // Создаем объект Intent для вызова новой Activity
        Intent intent = new Intent(this, SignUpActivity.class);
        // запуск activity
        startActivity(intent);
    }
}
