package com.example.kangningj.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kangningj.myapplication.activity.MyApplication;


/**
 * Created by Tfx on 2016/12/2.
 */

public class CacheUtils {
    private static final String SP_NAME = "goods";
    private static Context context = MyApplication.getAppContext();

    public static void putString(String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
        Log.d("存入cart","finished");
    }

    public static String getString(String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
}
