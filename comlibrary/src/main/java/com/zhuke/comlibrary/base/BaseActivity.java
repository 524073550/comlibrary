package com.zhuke.comlibrary.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuke.comlibrary.R;
import com.zhuke.comlibrary.broadcastreceiver.NetBroadcastReceiver;
import com.zhuke.comlibrary.utils.ActivityUtils;
import com.zhuke.comlibrary.utils.DisplayUtil;
import com.zhuke.comlibrary.utils.MyToast;
import com.zhuke.comlibrary.utils.StatusBarUtils;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 15653 on 2017/9/28.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, NetBroadcastReceiver.NetChangeListener {
    protected InputMethodManager inputMethodManager;
    public static NetBroadcastReceiver.NetChangeListener netEvent;// 网络状态改变监听事件

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        netEvent = this;
        openNetBroadcastReceiver();
        ActivityUtils.addActivity(this);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorAccent));
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
    }

    protected abstract int getLayoutID();

    protected abstract void initView();

    /**
     * 监听网络状态
     */
    public void openNetBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(new NetBroadcastReceiver(), filter);
    }


    public void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, DisplayUtil.getMobileHeight(getApplicationContext()) / 2);
        toast.show();
    }

    protected ProgressDialog progressBar;

    protected void showProgressBar(String message) {
        try {
            progressBar = new ProgressDialog(this);
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setMessage(message);
            progressBar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void dismissProgressBar() {
        try {
            if (progressBar != null && progressBar.isShowing()) {
                progressBar.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题内容
     *
     * @param title
     */
    public void setTitle(String title) {
        TextView mTitle = (TextView) findViewById(R.id.base_tv_title);
        mTitle.setText(title == null ? "" : title);

    }

    /**
     * @param isShow   是否显示
     * @param listener 点击监听
     */
    public void setLeftIV(boolean isShow, View.OnClickListener listener) {
        ImageView mBack = (ImageView) findViewById(R.id.base_iv_back);
        mBack.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (listener == null) {
            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            mBack.setOnClickListener(listener);
        }
    }

    /**
     * 添加右边图标
     *
     * @param resId
     * @param listener
     */
    protected void enableRightImage(int resId, View.OnClickListener listener) {
        ImageView rightIV = (ImageView) findViewById(R.id.base_iv_right1);
        rightIV.setImageResource(resId);
        rightIV.setOnClickListener(listener);
        rightIV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (isFastClick()) {
            return;
        }
    }


    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            MyToast.show(this, "再按一次退出App");
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityUtils.removeAllActivity();
            System.exit(0);
        }
    }


    private static long lastClickTime;

    /**
     * 防止点击频率
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long result = time - lastClickTime;
        if (result < 500 && result > 0) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 网络状态改变时间监听
     *
     * @param netWorkState true有网络，false无网络
     */
    @Override
    public void onNetChange(boolean netWorkState) {

    }


    @Override
    protected void onDestroy() {
        // Activity销毁时，提示系统回收
        System.gc();
        // 移除Activity
        ActivityUtils.removeActivity(this);
        super.onDestroy();
    }

}
