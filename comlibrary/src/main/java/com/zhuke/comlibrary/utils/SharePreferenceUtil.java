package com.zhuke.comlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.zhuke.comlibrary.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

public class SharePreferenceUtil {

    private static final String EASYHIN = "xingrong";

    public static SharedPreferences getSharedPreferences() {
        return BaseApplication.getInstance().getSharedPreferences(EASYHIN, Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return false;
        }
        return preferences.getBoolean(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return;
        }
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putString(String key, String value) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return;
        }
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return "";
        }
        return preferences.getString(key, "");
    }

    public static void putInt(String key, int value) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return;
        }
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void addInt(String key, int value){
        putInt(key, getInt(key)+value);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getInt(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return;
        }
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLong(String key) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return 0L;
        }
        return preferences.getLong(key, 0L);
    }

    public static void putStringSet(String key, Set<String> set) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return;
        }
        Editor editor = preferences.edit();
        editor.putStringSet(key, set);
        editor.apply();
    }

    public static Set<String> getStringSet(String key) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return new HashSet<>();
        }
        return preferences.getStringSet(key, new HashSet<String>());
    }

    public static void remove(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences == null) {
            return;
        }
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void addStringToSet(String value, String key) {
        Set<String> set = getStringSet(key);
        set.add(value);
        putStringSet(key, set);
    }
    //序列化用户对象
    public static String serialize(Object object){
        String serStr = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try{
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null)
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (byteArrayOutputStream != null)
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return serStr;
    }

    //反序列化用户对象
    public static Object deSerialization(String str) {
        Object o = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try{
            String redStr = java.net.URLDecoder.decode(str, "UTF-8");
            byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            o = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null)
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (byteArrayInputStream != null)
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return o;
    }
}
