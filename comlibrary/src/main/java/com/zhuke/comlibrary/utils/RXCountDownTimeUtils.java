package com.zhuke.comlibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import com.zhuke.comlibrary.R;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 15653 on 2018/3/12.
 */

public class RXCountDownTimeUtils {
  /*  private long mMillisInFuture = 60;
    private static RXCountDownTimeUtils mRXCountDownTimeUtils;


    public static RXCountDownTimeUtils getIntescens(){
        return mRXCountDownTimeUtils == null ? new RXCountDownTimeUtils() : mRXCountDownTimeUtils;
    }

    public void strtCountDown(final TextView textView, long millisInFuture, final Context context){
        this.mMillisInFuture = millisInFuture;
        Observable.interval(1, TimeUnit.SECONDS)
                .take((int) mMillisInFuture+2)//计时次数
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long integer) {
                        return mMillisInFuture - integer;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        textView.setEnabled(false);
                        textView.setBackgroundColor(Color.WHITE);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {

                    @Override
                    public void onCompleted() {
                        textView.setEnabled(true);
                        textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        textView.setText("重新发送");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        textView.setText("倒计时" + aLong +"s");
                    }
                });
    }*/
}
