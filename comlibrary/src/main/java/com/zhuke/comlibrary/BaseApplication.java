package com.zhuke.comlibrary;

import android.app.Application;
import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.MemoryCookieStore;

/**
 * Created by 15653 on 2018/3/12.
 */

public class BaseApplication extends Application {
    private static BaseApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        OkGo.init(instance);
        OkGo.getInstance()
                //               .debug("OkGo", Level.INFO, true)
                .setCookieStore(new MemoryCookieStore()).setCertificates();
        LogUtils.configAllowLog = true;//日志输出开关，发布时关闭
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
