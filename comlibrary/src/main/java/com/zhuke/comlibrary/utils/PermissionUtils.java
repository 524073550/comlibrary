package com.zhuke.comlibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;


import com.zhuke.comlibrary.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 6.0以上授权工具类
 * Created by luwei on 2016/4/7.
 */
public class PermissionUtils {
    public static final int REQUEST_CODE_CAMERA = 20001;//相机权限
    public static final int REQUEST_CODE_RECORD_AUDIO = 20002;//麦克风权限
    public static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 20003;//存储权限
    public static final int REQUEST_CODE_LOCATION = 20004;//位置权限
    public static final int REQUEST_CODE_MORE = 20005;//多个权限
    public static final int REQUEST_CODE_READ_PHONE_STATE = 20006;//电话权限

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;//麦克风权限
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;//相机权限
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;//存储权限
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;//电话权限
    public static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;//位置权限

    private static List<String> successPermissionCount = new ArrayList<>();
    private static List<String> failPermissionCount = new ArrayList<>();

    /**
     * 请求权限
     *
     * @param mActivity
     * @param permissions
     * @return
     */
    public static boolean checkPermission(int requestCode, Activity mActivity, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上必须用户手动授权
            if (permissions != null) {
                List<String> requestPermissionCount = getRequestPermissionList(mActivity, permissions);
                if (requestPermissionCount != null && requestPermissionCount.size() > 0) {
                    mActivity.requestPermissions(requestPermissionCount.toArray(new String[0]), requestCode);
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        return true;

    }

    public static boolean checkPermission(int requestCode, Fragment mFragment, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上必须用户手动授权
            if (permissions != null) {
                List<String> requestPermissionCount = getRequestPermissionList(mFragment.getActivity(), permissions);
                if (requestPermissionCount != null && requestPermissionCount.size() > 0) {
                    mFragment.requestPermissions(requestPermissionCount.toArray(new String[0]), requestCode);
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        return true;

    }

    /**
     * 获取需要去申请权限的权限列表
     *
     * @param permissions
     * @return
     */
    public static List<String> getRequestPermissionList(Activity mActivity, String... permissions) {
        successPermissionCount.clear();
        failPermissionCount.clear();
        List<String> reequestPermissionCount = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                reequestPermissionCount.add(permission);
            } else {
                successPermissionCount.add(permission);
            }
        }
        return reequestPermissionCount;
    }

    public static void onRequestPermissionsResult(@NonNull String permissions[], @NonNull int[] grantResults, OnGrantSuccessListener listener) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                failPermissionCount.add(permissions[i]);
                switch (permissions[i]) {
                    case PERMISSION_READ_PHONE_STATE:
                        if (TextUtils.isEmpty(stringBuilder.toString())) {
                            stringBuilder.append("电话");
                        } else {
                            stringBuilder.append("、电话");
                        }
                        break;
                    case PERMISSION_CAMERA:
                        if (TextUtils.isEmpty(stringBuilder.toString())) {
                            stringBuilder.append("相机");
                        } else {
                            stringBuilder.append("、相机");
                        }
                        break;
                    case PERMISSION_LOCATION:
                        if (TextUtils.isEmpty(stringBuilder.toString())) {
                            stringBuilder.append("定位");
                        } else {
                            stringBuilder.append("、定位");
                        }
                        break;
                    case PERMISSION_READ_EXTERNAL_STORAGE:
                        if (TextUtils.isEmpty(stringBuilder.toString())) {
                            stringBuilder.append("储存");
                        } else {
                            stringBuilder.append("、储存");
                        }
                        break;
                    case PERMISSION_RECORD_AUDIO:
                        if (TextUtils.isEmpty(stringBuilder.toString())) {
                            stringBuilder.append("麦克风");
                        } else {
                            stringBuilder.append("、麦克风");
                        }
                        break;
                }
                if (!TextUtils.isEmpty(stringBuilder.toString())) {
                    Toast.makeText(BaseApplication.getInstance(), stringBuilder.toString() + "权限需要您先授权", Toast.LENGTH_LONG).show();
                }
            } else {
                successPermissionCount.add(permissions[i]);
            }
        }
        if (listener != null) {
            listener.onGrantSuccess(successPermissionCount.toArray(new String[0]));
            listener.onGrantFail(failPermissionCount.toArray(new String[0]));
        }
    }


    public interface OnGrantSuccessListener {
        void onGrantSuccess(String[] permissions);//成功获取的权限

        void onGrantFail(String[] permissions);//获取失败的权限
    }

}
