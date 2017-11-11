package com.trpo6.receiptanalyzer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Класс для получения, хранения и редактирования
 * информаци об авторизации
 */

public class AuthInfo{
    /** Token string */
    private static String key = "";

    public static void setKey(String key) {
        AuthInfo.key = key;
    }

    public static String getKey() {

        return key;
    }

    private static final String path = "user_auth";

    /**
     * Проверяет, авторизован ли текущий пользователь
     * @param context
     * @return
     */
    public static boolean isAuthorized(Context context) {
        SharedPreferences sp = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        key = sp.getString("token","");
        if (key.length()==0)
            return false;
        return true;
    }

    /** Сохранение данных об авторизации */
    public static void authSave(Context context, String token) {
        SharedPreferences sp =  context.getSharedPreferences(path,Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("token", token);
        setKey(token);
        e.commit();
    }

    /** Удаление данных об авторизации */
    public static void authClear(Context context) {
        // стираем старый токен
        SharedPreferences sp = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("token","");
        e.commit();
    }
}
