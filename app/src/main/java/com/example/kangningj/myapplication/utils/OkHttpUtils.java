package com.example.kangningj.myapplication.utils;

import com.example.kangningj.myapplication.callpackage.HttpCallBack;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kangningj on 2017/2/8.
 */

public class OkHttpUtils {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    public static void getStringResponse(final HttpCallBack callBack, final String url) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = null;
                try {
                    response = mOkHttpClient.newCall(request).execute();
                    callBack.sloveString(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
