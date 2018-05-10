package com.zhuke.comlibrary;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.zhuke.comlibrary.base.BaseActivity;
import com.zhuke.comlibrary.bean.UserEntity;
import com.zhuke.comlibrary.http.Interceptor.Transformer;
import com.zhuke.comlibrary.http.RetrofitManage;
import com.zhuke.comlibrary.http.Subscriber.CommonObserver;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RXCountDownTimeUtils.getIntescens().strtCountDown(mQqqq, 60, this);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中....");
        RetrofitManage.getInstance().getService().setPass(new HashMap<String, Object>())
                .compose(Transformer.<UserEntity>switchSchedulers(progressDialog))
                .subscribe(new CommonObserver<UserEntity>(progressDialog) {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(UserEntity userEntity) {

                    }
                });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }


    // 处理网络状态结果
    @Override
    public void onNetChange(boolean netWorkState) {
        super.onNetChange(netWorkState);
//        textView.setText(netWorkState ? "有网络" : "无网络");
        showToast(netWorkState ? "有网络" : "无网络");
    }





    public void onViewClicked() {
        showToast("aaa");
    }
}
