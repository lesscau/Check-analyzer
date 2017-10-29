package com.trpo6.receiptanalyzer.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * QR сканер
 */
public class QRscanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;

    /**Запуск окна сканера*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    /**Регистрация себя, как обработчика результатов сканирования и запуск камеры*/
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    /**Остановка камеры при паузе*/
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
    /**Обработка результатов сканирования*/
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        /**Печать результатов сканирования*/
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        /** Отображение результатов сканирования в диалоговом окне*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
}
