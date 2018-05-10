package com.zhuke.comlibrary.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @explain 键盘状态监听器
 * 使用方法：
KeyboardStatusUtil.getInstance()
.register(this)
.setVisibilityListener(new KeyboardStatusUtil.OnVisibilityListener() {
    @Override public void onVisibilityChanged(boolean isVisibility) {
        //滚动视图需要配合android:windowSoftInputMode="adjustResize" 使用
        ScrollView scrollView = (ScrollView) findViewById(R.id.sv);
        if (isVisibility) {
        scrollView.smoothScrollTo(0, scrollView.getHeight());
    }
}
});
 * @author feisher.qq:458079442
 * @time 2018/3/7 13:29.
 */
public class KeyboardStatusUtil<T> {
    private static final int SOFT_KEY_BOARD_MIN_HEIGHT = 100;
    private OnVisibilityListener mVisibilityListener;

    boolean keyboardVisible = false;

    private static class SingletonHolder {
        private static final KeyboardStatusUtil INSTANCE = new KeyboardStatusUtil();
    }

    private KeyboardStatusUtil() {

    }

    public static final KeyboardStatusUtil getInstance() {

        return SingletonHolder.INSTANCE;
    }

    public KeyboardStatusUtil register(T t) {
        if (t instanceof Activity ) {
            registerView(((Activity)t).getWindow().getDecorView().findViewById(android.R.id.content));
        } if (t instanceof Fragment) {
            registerView(((Fragment)t).getView());
        } if (t instanceof View) {
            registerView((View) t);
        }
        return this;
    }

    public KeyboardStatusUtil registerView(final View v) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                v.getWindowVisibleDisplayFrame(r);

                int heightDiff = v.getRootView().getHeight() - (r.bottom - r.top);
                // if more than 100 pixels, its probably a keyboard...
                if (heightDiff > SOFT_KEY_BOARD_MIN_HEIGHT) {
                    if (!keyboardVisible) {
                        keyboardVisible = true;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(true);
                        }
                    }
                } else {
                    if (keyboardVisible) {
                        keyboardVisible = false;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(false);
                        }
                    }
                }
            }
        });

        return this;
    }

    public KeyboardStatusUtil setVisibilityListener(OnVisibilityListener listener) {
        mVisibilityListener = listener;
        return this;
    }

    public interface OnVisibilityListener {
        void onVisibilityChanged(boolean isVisibility);
    }

}