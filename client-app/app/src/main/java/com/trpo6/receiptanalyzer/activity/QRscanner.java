package com.trpo6.receiptanalyzer.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.model.Items;
import com.trpo6.receiptanalyzer.utils.InternetConnection;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * QR сканер
 */
public class QRscanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;

    /**Запуск окна сканера*/
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_qrscanner);
        //setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
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
        Log.i("Contents = " + rawResult.getText(),", Format = " + rawResult.getBarcodeFormat().toString());

        /** Результат сканирования */
        String text = rawResult.getText();
        /** Данные для запроса в ФНС (fn, fd, fp) */
        ArrayList<String> fiscal = new ArrayList();
        // Разбор результата сканирования
        String pattern = "(fn=[0-9]+)|(&i=[0-9]+)|(fp=[0-9]+)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        int i = 0;
        Log.i("start finding", "");
        while(m.find()) {
            fiscal.add(text.substring(m.start()+3, m.end()));
            Log.i("find "+i+": ",fiscal.get(i));
            i++;
        }
        // Запрос в ФНС
        getReceipt(fiscal);
    }

    private static class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT = "Отсканируйте QR c чека!";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 20;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }
    }

    /**
     * Запрос к серверу за данными из чека
     * @param fiscal Параметры чека (fn, fd, fp)
     */
    private void getReceipt(final ArrayList<String> fiscal){
        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            ApiService api = RetroClient.getApiService();
            //User user  = new User("89112356232","pass");
            Log.i("token",LoginActivity.KEY);
            final Items _items = new Items();
            //fiscal.get(0),fiscal.get(1),fiscal.get(2)
            Call<Items> call = api.getReceipt(LoginActivity.KEY,fiscal.get(0),fiscal.get(1),fiscal.get(2));
            Log.i("i",call.request().toString());
            /**
             * Enqueue Callback will be call when get response...
             */
            call.enqueue(new Callback<Items>() {
                @Override
                public void onResponse(Call<Items> call, Response<Items> response) {
                    if (response.isSuccessful()) {
                        /**
                         * Got Successfully
                         */
                        //String ans = response.body().toString();
                        Log.i("success", response.body().toString());
                        _items.setItems(response.body().getItems());
                        _items.correctPrice();

                        ProductListActivity.items.addAll(_items.getItems());
                        // Переход к активити отображения продуктов
                        openProductList();

                    } else {
                        Log.e("err0", response.toString());
                    }
                }

                @Override
                public void onFailure(Call<Items> call, Throwable t) {
                    Log.e("err1", t.toString());
                }
            });

        } else {
            Log.e("error","cant connect");
        }

        // В случае ошибки - повторный запуск сканирования
        onResume();
    }

    private void openProductList(){
        // действия, совершаемые после нажатия на кнопку
        // Создаем объект Intent для вызова новой Activity
        Intent intent = new Intent(this, ProductListActivity.class);
        // запуск activity
        startActivity(intent);
    }
}