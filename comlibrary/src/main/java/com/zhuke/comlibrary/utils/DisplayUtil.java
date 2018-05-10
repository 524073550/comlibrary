package com.zhuke.comlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;

import java.lang.reflect.Method;

/**
 * @author gyw
 * @version 1.0
 * @time: 2015-6-5 上午11:44:21
 * @fun: 屏幕工具类
 */
public class DisplayUtil {

    public static final String TAG = "DisplayUtil";

    /**
     * get screen height of this cellphone
     *
     * @param context
     * @return
     */
    public static int getMobileHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels; // 得到高度
        return height;
    }

    /**
     * get screen width of this cellphone
     *
     * @param context
     * @return
     */
    public static int getMobileWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels; // 得到宽度
        return width;

    }

    /**
     * 根据手机的分辨率dp 转成px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率px(像素) 转成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    // 获取状态栏高度
    public static int getStatusBar(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);///取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
        int top = rect.top;////状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度
        View v = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);///获得根视图
        int top2 = v.getTop();///状态栏标题栏的总高度,所以标题栏的高度为top2-top
        int width = v.getWidth();///视图的宽度,这个宽度好像总是最大的那个
        int height = v.getHeight();////视图的高度，不包括状态栏和标题栏
        return top;
    }

    /**
     * 获取底部虚拟键盘的高度
     */
    public static int getBottomKeyboardHeight(Activity activity) {
        int screenHeight = getAccurateScreenDpi(activity)[1];
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightDifference = screenHeight - dm.heightPixels;
        return heightDifference;
    }

    /**
     * 获取精确的屏幕大小
     */
    public static int[] getAccurateScreenDpi(Activity activity) {
        int[] screenWH = new int[2];
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class<?> c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            screenWH[0] = dm.widthPixels;
            screenWH[1] = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWH;
    }
}
