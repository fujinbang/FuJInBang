package com.fujinbang.internet;

import com.fujinbang.internet.IRequest.IAvatarUpload;
import com.fujinbang.internet.IRequest.IHelpListRequest;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/8.
 */
public class AvatarUpload {
    IAvatarUpload upload;

    private final IAvatarUpload getUpload() {
        if (upload == null) {
            upload = getRetrofit().create(IAvatarUpload.class);
        }
        return upload;
    }

    public Observable<String> uploadAvatar(String token, String fileName, String key, File avatar) {
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), avatar);
        MultipartBody.Part bitmap = MultipartBody.Part.createFormData("file", fileName, photoRequestBody);
        RequestBody Token = RequestBody.create(null, token);
        RequestBody Key = RequestBody.create(null, key);
        RequestBody Scope = RequestBody.create(null, "fujinbang:" + key);
        return getUpload().uploadAvatar(Token, Key, Scope, bitmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<HashMap<String, Object>, String>() {
                    @Override
                    public String call(HashMap<String, Object> hashMap) {
                        return parseJson(hashMap);
                    }
                });
    }

    private final Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstant.qiniuUpload)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    private final String parseJson(HashMap<String, Object> hashMap) {
        if (hashMap.containsKey("key")) {
            return "上传成功！";
        } else if (hashMap.containsKey("error")) {
            return hashMap.get("error").toString();
        }
        return "";
    }
}
