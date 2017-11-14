package com.lh.api;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.lh.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liaohui on 2017/7/20.
 */

public final class AppApi {

    //    private static String BASE_URL_DEBUG = "http://139.224.199.7:81/";
    private static String BASE_URL_DEBUG = "http://v.juhe.cn/weixin/";
    public static String BASE_URL = "http://api.viimoo.cn/";
    private static OkHttpClient okHttpClient;


    static {
        initOkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static <T> T getApiService(Class<T> cls) {
        if (BuildConfig.DEBUG) {
            return createApi(cls, BASE_URL_DEBUG);
        }
        return createApi(cls, BASE_URL);
    }


    /**
     * 根据传入的baseUrl，和api创建retrofit
     */
    private static <T> T createApi(Class<T> clazz, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }

    private static void initOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (AppApi.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    if (BuildConfig.DEBUG) {
                        // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
                        LoggingInterceptor loggingInterceptor = new LoggingInterceptor(new MyLog());
                        loggingInterceptor.setLevel(LoggingInterceptor.Level.MY_DIY);
                        builder.addInterceptor(loggingInterceptor);
                    }
                    // http://www.jianshu.com/p/93153b34310e
                    Cache cache = new Cache(new File(Constants.PATH_CACHE), 1024 * 1024 * 50);//缓存目录
                    Interceptor cacheInterceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            if (NetworkUtils.isConnected()) {
                                //有网络时只从网络获取
                                request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
                            } else {
                                //无网络时只从缓存中读取
                                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                            }
                            Response response = chain.proceed(request);

                            if (NetworkUtils.isConnected()) {
                                int maxAge = 60 * 60; // 有网络时, 缓存1小时
                                response.newBuilder()
                                        .header("Cache-Control", "public, max-age=" + maxAge)
                                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                        .build();
                            } else {
                                // 无网络时，设置超时为1周
                                int maxStale = 60 * 60 * 24 * 7;
                                // 无网络时，设置超时为3秒
                                response.newBuilder()
                                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                        .removeHeader("Pragma")
                                        .build();
                            }
                            return response;
                        }
                    };
 /*       Interceptor apikey = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder()
//                        .addHeader("apikey", Constants.KEY_API)//这里是添加请求头
                        .build();
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(apikey);*/
                    //设置缓存
                    builder.addNetworkInterceptor(cacheInterceptor);
                    builder.addInterceptor(cacheInterceptor);
                    builder.cache(cache);
                    //设置超时
                    builder.connectTimeout(30, TimeUnit.SECONDS);
                    builder.readTimeout(30, TimeUnit.SECONDS);
                    builder.writeTimeout(30, TimeUnit.SECONDS);
                    //错误重连
                    builder.retryOnConnectionFailure(true);
                    okHttpClient = builder.build();
                }
            }
        }
    }


    private static class MyLog implements LoggingInterceptor.Logger {
        @Override
        public void log(String msg) {
            String tag = "oklog-->";
            if (msg == null || msg.length() == 0)
                return;

            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                LogUtils.e(tag + msg);
            } else {
                while (msg.length() > segmentSize) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    LogUtils.e(tag + logContent);
                }
                LogUtils.e(tag + msg);// 打印剩余日志
            }
//            LogUtils.e("oklog: " + msg);
        }
    }
}
