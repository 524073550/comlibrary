package com.zhuke.comlibrary.content;

/**
 * Created by lx on 2016/9/13.
 */
public class UrlConstants {


    public static final String SERVER_PATH = "http://wuhanxingrong.vicp.io:8886/";// 服务器地址 测试
//        public static final String SERVER_PATH = "http://xgfapi.istarcredit.com:8886/";// 阿里服务器地址
//        public static final String SERVER_PATH = "http://192.168.0.111:8886/";// 服务器地址 汪娟
    //    public static final String SERVER_PATH = "http://192.168.0.25:8886/";// 服务器地址 胡亚丽
//        public static final String SERVER_PATH = "http://192.168.0.23:8886/";// 服务器地址 王才睿
    //    public static final String SERVER_PATH = "http://192.168.0.42:8886/";// 服务器地址 成剑

    public static String BASE_PATH = "http://xrjf.oss-cn-shanghai.aliyuncs.com/";//阿里云图片地址

    public static String verifyUrl(String url) {
        if (url != null) {
            if (url.startsWith("http")) {
                return url;
            } else if (url.startsWith("file")) {
                return url;
            } else {
                return BASE_PATH + url.replace(";", "");
            }
        }
        return null;
    }
}
