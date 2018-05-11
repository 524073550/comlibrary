package com.zhuke.comlibrary.http.ConverterFactory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.zhuke.comlibrary.utils.DesUtils;

import org.json.JSONObject;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by 15653 on 2018/5/11.
 */

public class DesConverterFactory extends Converter.Factory {

    public static DesConverterFactory create() {
        return create(new Gson());
    }

    public static DesConverterFactory create(Gson gson) {
        return new DesConverterFactory(gson);
    }

    private final Gson gson;

    private DesConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DesGsonBodyConverter<>(adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
    private static class DesGsonBodyConverter<T> implements Converter<ResponseBody, T> {
        private final TypeAdapter<T> adapter;

        DesGsonBodyConverter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            String temp = value.string();
//            temp = new String(Base64.decode(temp, Base64.DEFAULT));
            JSONObject object = null;
            String data1 = null;
            try {
                object = new JSONObject(temp);
                String code = object.optString("code");
                String msg = object.optString("msg");
                String data = object.optString("data");
                data1 = DesUtils.decrypt(object.optString("data"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return adapter.fromJson(data1);
        }
    }

}
