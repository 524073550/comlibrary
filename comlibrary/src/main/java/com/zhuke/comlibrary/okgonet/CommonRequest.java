package com.zhuke.comlibrary.okgonet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.zhuke.comlibrary.bean.UserEntity;
import com.zhuke.comlibrary.content.UrlConstants;
import com.zhuke.comlibrary.utils.DesUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 请求base类
 */
public class CommonRequest {
    private static final String CODE_SUCCESS = "0000";
    private static final String CODE_TOKEN = "5555";//token失效
    protected String action;
    protected Activity activity;

    public CommonRequest(Activity activity, String action) {
        this.action = action;
        this.activity = activity;
    }

    public void request(Map<String, Object> params, final OnResultListener onResultListener) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
//            UserEntity entity = XRApplication.getInstance().getUserEntity();
            UserEntity entity = new UserEntity();
            if (!params.containsKey("userId")) {
                params.put("userId", entity == null ? "" : entity.getUserId());
            }
            if (!params.containsKey("token")) {
                params.put("token", entity == null ? "" : entity.getToken());
            }
            // map.put("platform", "Android");
//            map.put("deviceno", CommonRequest.getDeviceId(this));
            if (!params.containsKey("platform")) {
                params.put("platform", entity == null ? "" : "Android");
            }
            if (!params.containsKey("deviceno")) {
//                params.put("deviceno", entity == null ? "" :CommonRequest.getDeviceId(XRApplication.getInstance().getApplicationContext()));
            }
            String s = new Gson().toJson(params);
            LogUtils.e("[" + action + "]request-->" + s.toString());
            String encrypt = DesUtils.encrypt(s);
            OkGo.post(UrlConstants.SERVER_PATH + action)
                    .params("params", encrypt)
                    //                    .params("data", s)

                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, okhttp3.Response response) {
                            try {
                                if (onResultListener != null) {
                                    JSONObject object = new JSONObject(s);
                                    String code = object.optString("code");
                                    String msg = object.optString("msg");
                                    String data = object.optString("data");
                                    if (!TextUtils.isEmpty(data) && !"null".equals(data)) {
                                        data = DesUtils.decrypt(object.optString("data"));
                                    }
                                    try {
                                        LogUtils.e("[" + action + "]response--> code:" + code + "; msg:" + msg + "; data:" + data);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (code.equals(CODE_SUCCESS)) {
                                        onResultListener.onSuccess(data, msg);
                                    } else if (code.equals(CODE_TOKEN)) {
//                                        activity.startActivity(new Intent(activity, LoginActivity.class));
                                        List<Class> classes = new ArrayList<>();
//                                        classes.add(LoginActivity.class);
//                                        ActivityManager.getInstance().popOtherActivity(classes);
//                                        XRApplication.getInstance().setUserEntity(null);
                                    } else {
                                        onResultListener.onFailure(code, msg);
                                    }
                                }
                            } catch (Exception decryptExcption) {
                                decryptExcption.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Call call, okhttp3.Response response, Exception e) {
                            super.onError(call, response, e);
                            if (onResultListener != null) {
                                onResultListener.onFailure("9999", "无法连接到服务器，请检查网络");
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String getVersionName(Context context) {
        try {
            String pkName = context.getPackageName();
            return context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public interface OnResultListener {
        void onSuccess(String data, String desc);

        void onFailure(String errorCode, String errorDesc);
    }

}
