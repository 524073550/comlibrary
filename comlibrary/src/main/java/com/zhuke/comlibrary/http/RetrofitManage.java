package com.zhuke.comlibrary.http;

import com.zhuke.comlibrary.content.ContentUrl;
import com.zhuke.comlibrary.http.Interceptor.EncryptInterceptor;
import com.zhuke.comlibrary.http.Interceptor.InterceptorUtil;
import com.zhuke.comlibrary.http.service.Service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 15653 on 2018/3/15.
 */

public class RetrofitManage {
    private static RetrofitManage mRetrofitManage;
    private final Service mService;

    public RetrofitManage() {
        OkHttpClient mOkHttpClient=new OkHttpClient.Builder()
                .connectTimeout(ContentUrl.HTTP_TIME, TimeUnit.SECONDS)
                .readTimeout(ContentUrl.HTTP_TIME, TimeUnit.SECONDS)
                .writeTimeout(ContentUrl.HTTP_TIME, TimeUnit.SECONDS)
                .addInterceptor(InterceptorUtil.HeaderInterceptor())
                .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
//                .addInterceptor(new EncryptInterceptor())//加密解密拦截器
                .build();
        Retrofit mRetrofit=new Retrofit.Builder()
                .baseUrl(ContentUrl.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .client(mOkHttpClient)
                .build();
        mService = mRetrofit.create(Service.class);

    }

    public static RetrofitManage getInstance() {
        if (mRetrofitManage == null) {
            synchronized (RetrofitManage.class) {
                if (mRetrofitManage == null) {
                    return mRetrofitManage = new RetrofitManage();
                }
            }
        }
        return mRetrofitManage;
    }

    public Service getService(){
        return mService;
    }

}
