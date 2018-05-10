package com.zhuke.comlibrary.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

/**
 * Created by Administrator - PC on 2017/10/25.
 * 为与百度地图搭配，写了方向感应器
 * 利用加速度传感器和地磁场传感器确认方向
 *
 * 使用方法：初始化后调用start();
 * setCallBackDirection()监听角度回调
 * 结束使用：shop()
 */

public class DirectionSensorUtil {

    private Context context;
    private SensorManager mSensorManager;//感应器管理

    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器

    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];

    private float oldDirection = 0.0f;
    private float newDirection;

    private boolean isStart = false;

    public DirectionSensorUtil(Context context){
        this.context = context;
        // 实例化传感器管理者
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        // 初始化加速度传感器
        accelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 初始化地磁场传感器
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    public void start(){
        // 注册监听
        mSensorManager.registerListener(new MySensorEventListener(),
                accelerometer, Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(new MySensorEventListener(), magnetic,
                Sensor.TYPE_MAGNETIC_FIELD);

        isStart = true;
    }


    public void shop(){
        // 解除注册
        mSensorManager.unregisterListener(new MySensorEventListener());
        isStart = false;
    }

    public boolean isStart(){
        return isStart;
    }

    // 计算方向
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
        newDirection = (float) Math.toDegrees(values[0]);
        if(Math.abs(newDirection - oldDirection) >= 1.0){
//            LogUtils.e("Math.abs(newDirection - oldDirection)n-->"+Math.abs(newDirection - oldDirection));
            oldDirection = newDirection;
            if(callBack != null){
                callBack.callBackDirection(newDirection);
            }
        }


        /*//打印方向
        if (newDirection >= -5 && newDirection < 5) {
            LogUtils.i("正北");
        } else if (newDirection >= 5 && newDirection < 85) {
            // Log.i(TAG, "东北");
            LogUtils.i("东北");
        } else if (newDirection >= 85 && newDirection <= 95) {
            // Log.i(TAG, "正东");
            LogUtils.i("正东");
        } else if (newDirection >= 95 && newDirection < 175) {
            // Log.i(TAG, "东南");
            LogUtils.i("东南");
        } else if ((newDirection >= 175 && newDirection <= 180)
                || (newDirection) >= -180 && newDirection< -175) {
            // Log.i(TAG, "正南");
            LogUtils.i("正南");
        } else if (newDirection >= -175 && newDirection < -95) {
            // Log.i(TAG, "西南");
            LogUtils.i("西南");
        } else if (newDirection >= -95 && newDirection < -85) {
            // Log.i(TAG, "正西");
            LogUtils.i("正西");
        } else if (newDirection >= -85 && newDirection < -5) {
            // Log.i(TAG, "西北");
            LogUtils.i("西北");
        }*/
    }

    class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values;
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticFieldValues = event.values;
            }
            new Handler().postAtTime(new Runnable() {
                @Override
                public void run() {
                    calculateOrientation();
                }
            },200);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }

    }

    CallBack callBack;

    public void setCallBackDirection(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void callBackDirection(float direction);
    }


}
