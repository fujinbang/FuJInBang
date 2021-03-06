package com.fujinbang.internet;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.fujinbang.internet.InternetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.Format;

/**
 * Created by Administrator on 2016/5/19.
 */
public class UserMsgUpload extends InternetRequest {
    public UserMsgUpload(Context context) {
        super(context);
    }

    public void uploadUserMsg(String token, String key, Object value, final OnUserUploadListener listener) {

        String url = UrlConstant.modifyUserInfo;
        try {
            JSONObject post = new JSONObject();
            post.put("token", token);
            post.put(key, value);
            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    if (listener != null) listener.OnSuccess(parseJson(object));
                }

                @Override
                public void onError(String desc, int statusCode) {
                    if (listener != null) listener.OnError(desc);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUploadToken(String fileName, final OnUserUploadListener listener) {
        String url = UrlConstant.token;
        try {
            JSONObject post = new JSONObject();
            post.put("filename", fileName);
            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    if (listener != null) listener.OnSuccess(object.optString("token"));
                }

                @Override
                public void onError(String desc, int statusCode) {
                    if (listener != null) listener.OnError(desc);
                }
            });
        } catch (JSONException e) {
            if (listener != null) listener.OnError(e.toString());
        }
    }

    public void uploadAuthentication(String phoneNum, String password, final OnUserUploadListener listener) {
        String url = UrlConstant.login;
        try {
            JSONObject post = new JSONObject();
            post.put("phonenum", phoneNum);
            post.put("password", password);
            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    if (listener != null) listener.OnSuccess(parseJson2code(object));
                }

                @Override
                public void onError(String desc, int statusCode) {
                    if (listener != null) listener.OnError(desc);
                }
            });
        } catch (JSONException e) {
            if (listener != null) listener.OnError(e.toString());
        }
    }

    public void uploadSex(String token, boolean isMan, OnUserUploadListener listener) {
        uploadUserMsg(token, "gender", isMan ? "male" : "female", listener);
    }

    public void uploadName(String token, String userName, OnUserUploadListener listener) {
        uploadUserMsg(token, "nickname", userName, listener);
    }

    public void uploadLocation(String token, String location, OnUserUploadListener listener) {
        uploadUserMsg(token, "location", location, listener);
    }

    public void uploadPhoneNum(String token, String phoneNum, OnUserUploadListener listener) {
        uploadUserMsg(token, "phonenum", phoneNum, listener);
    }

    public void uplaodCity(String token, String city, OnUserUploadListener listener) {
        uploadUserMsg(token, "city", city, listener);
    }

    public void uploadEmail(String token, String email, OnUserUploadListener listener) {
        uploadUserMsg(token, "email", email, listener);
    }

    public void uploadPassword(String token, String psd, OnUserUploadListener listener) {
        uploadUserMsg(token, "password", psd, listener);
    }

    public void uploadPushID(String token, String pushId, OnUserUploadListener listener) {
        uploadUserMsg(token, "clientid", pushId, listener);
    }

    public interface OnUserUploadListener {
        void OnSuccess(String desc);

        void OnError(String desc);
    }

    private static final byte[] bitmap2Byte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            baos.flush();
            baos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            Log.e("zy", "bitmap2Byte error:" + e);
        }
        return null;
    }

    private String parseJson2code(JSONObject object) {
        int code = object.optInt("code", 0);
        if (code == 1) {
            return "验证成功";
        } else if (code == 403) {
            return "密码不正确";
        } else if (code == 404) {
            return "用户不存在";
        } else {
            return "验证失败";
        }
    }

    private String parseJson(JSONObject object) {
        return object.optString("desc", "更新用户信息失败");
    }
}
