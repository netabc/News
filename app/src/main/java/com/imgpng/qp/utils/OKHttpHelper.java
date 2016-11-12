package com.imgpng.qp.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by qp on 2016/11/12.
 */

public class OKHttpHelper {
    /**
     * 创建线程安全的单例对象
     * @return
     */
    public static OkHttpClient getClient(){
        return HttpBuilder.INSTANCE;
    }
    private static final class HttpBuilder{

        private final static OkHttpClient INSTANCE = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }
}
