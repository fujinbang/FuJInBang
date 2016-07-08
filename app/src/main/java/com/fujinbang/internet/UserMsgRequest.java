package com.fujinbang.internet;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.fujinbang.internet.InternetRequest;
import com.fujinbang.setting.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/19.
 */
public class UserMsgRequest extends InternetRequest {
    public UserMsgRequest(Context context) {
        super(context);
    }

    public void getUserMsg(String token, final OnUserListener listener) {
        String url = UrlConstant.queryUserInfo;
        try {
            JSONObject post = new JSONObject();
            post.put("token", token);

            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    if (listener != null)
                        listener.onSucess(parseJson(object));
                }

                @Override
                public void onError(String desc, int statusCode) {
                    if (listener != null)
                        listener.onError(desc);
                }
            });
        } catch (JSONException e) {
            Log.e("serMsgRequest", e.toString());
            if (listener != null) listener.onError(e.toString());
        }
    }

    public void getUserAvator(int id, int width, int height, final OnUserListener listener) {
        String url = UrlConstant.qiniu + id + ".png";
        getBitmap(url, width, height, new OnResponseListener<Bitmap>() {
            @Override
            public void onSuccess(Bitmap object) {
                if (listener != null) listener.onAvatar(object);
            }

            @Override
            public void onError(String desc, int statusCode) {
                if (listener != null) listener.onError(desc + statusCode);
            }
        });
    }

    public void getUserMsg(int id, final OnUserListener listener) {
        String url = UrlConstant.queryUserInfo;
        try {
            JSONObject post = new JSONObject();
            post.put("id", id);

            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    if (listener != null)
                        listener.onSucess(parseJson(object));
                }

                @Override
                public void onError(String desc, int statusCode) {
                    if (listener != null)
                        listener.onError(desc);
                }
            });
        } catch (JSONException e) {
            Log.e("serMsgRequest", e.toString());
            if (listener != null) listener.onError(e.toString());
        }
    }

    public interface OnUserListener {
        void onSucess(User user);

        void onAvatar(Bitmap Avatar);

        void onError(String error);
    }

    private static User parseJson(JSONObject object) {
        User user = new User();

        JSONObject userMsg = object.optJSONObject("data");
        if (userMsg != null) {
            user.setIsMan(userMsg.optString("gender", "male").equals("male"));
            user.setId(userMsg.optInt("id", 0));
            user.setUserName(userMsg.optString("nickName", "无"));
            user.setPhoneNum(userMsg.optString("phoneNum", "无"));
            user.setRegDate(userMsg.optString("regDate", "无"));
            user.setScore(userMsg.optInt("score", 0));
            user.setStatus(userMsg.optInt("status", 0));
            user.setArea(userMsg.optString("city", "无"));
            user.setEmail(userMsg.optString("email", "无"));
        }

        return user;
    }
}
