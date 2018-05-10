package com.zhuke.comlibrary.http.Interceptor;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.zhuke.comlibrary.utils.DecryptExcption;
import com.zhuke.comlibrary.utils.DesUtils;
import com.zhuke.comlibrary.utils.EncryptExcption;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by 15653 on 2018/5/10.
 */

public class EncryptInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //这个是请求的url，也就是咱们前面配置的baseUrl
        String url = request.url().toString();
        //这个是请求方法
        String method = request.method();
        long t1 = System.nanoTime();
        try {
            request = encrypt(request);//模拟的加密方法
        } catch (EncryptExcption encryptExcption) {
            encryptExcption.printStackTrace();
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        try {
            response = decrypt(response);
        } catch (DecryptExcption decryptExcption) {
            decryptExcption.printStackTrace();
        }
        return response;
    }

    private Response decrypt(Response response) throws IOException, DecryptExcption {
        if (response.isSuccessful()) {
            //the response data
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String string = buffer.clone().readString(charset);
            //解密方法，需要自己去实现
            String bodyString = DesUtils.decrypt(string);
            ResponseBody responseBody = ResponseBody.create(contentType, bodyString);
            response = response.newBuilder().body(responseBody).build();
        }
        return response;
    }

    private Request encrypt(Request request) throws IOException, EncryptExcption {
        //获取请求body，只有@Body 参数的requestBody 才不会为 null
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            okio.Buffer buffer = new okio.Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }

            String string = buffer.readString(charset);

            //模拟加密的方法，这里调用大家自己的加密方法就可以了
            String encryptStr = DesUtils.encrypt(string);

            Map<String, Object> map = new HashMap<>();
            map.put("params", encryptStr);
            String s = new Gson().toJson(map);
            LogUtils.e(s);
            RequestBody body = MultipartBody.create(contentType, s);
            request = request.newBuilder()
                    .post(body)
                    .build();

        }
        return request;

    }

}
