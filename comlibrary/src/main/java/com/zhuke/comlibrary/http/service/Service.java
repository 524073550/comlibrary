package com.zhuke.comlibrary.http.service;

import com.zhuke.comlibrary.bean.A;
import com.zhuke.comlibrary.bean.HttpResult;
import com.zhuke.comlibrary.bean.LoginBean;
import com.zhuke.comlibrary.bean.UserEntity;
import com.zhuke.comlibrary.content.ContentUrl;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 15653 on 2018/3/15.
 */

public interface Service {

    @FormUrlEncoded
    @POST(ContentUrl.LOGINACTION)
    Observable<UserEntity> login(@FieldMap Map<String,Object> map);//注意要加上HttpResult的泛型具体类

    @FormUrlEncoded
    @POST(ContentUrl.LOGINACTION)
    Observable<UserEntity> setPass(@FieldMap Map<String,Object> map);//注意要加上HttpResult的泛型具体类
}
