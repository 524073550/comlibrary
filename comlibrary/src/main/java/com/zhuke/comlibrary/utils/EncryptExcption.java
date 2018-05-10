package com.zhuke.comlibrary.utils;

/**
 * Created by duanlei on 16/7/11.
 */
public class EncryptExcption extends Exception {
    private String message;

    public EncryptExcption(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
