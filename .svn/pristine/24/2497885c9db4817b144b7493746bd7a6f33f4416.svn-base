package com.fujinbang.internet;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/6/9.
 */
public class SignRequest extends InternetRequest {
    public SignRequest(Context context) {
        super(context);
    }

    public void getSignInfo(String token, final OnSignListener listener) {
        String url = UrlConstant.getSign;
        try {
            JSONObject post = new JSONObject();
            post.put("token", token);
            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    if (listener != null) listener.onSuccess(getDays(object), getDate(object));
                }

                @Override
                public void onError(String desc, int statusCode) {
                    if (listener != null) listener.onError(desc);
                }
            });
        } catch (JSONException e) {
            if (listener != null) listener.onError(e.toString());
        }
    }

    public final void signIn(String token, final OnSignListener listener) {
        String url = UrlConstant.sign;
        try {
            JSONObject post = new JSONObject();
            post.put("token", token);
            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    if (listener != null) listener.onSuccess(getDays(object), getDesc(object));
                }

                @Override
                public void onError(String desc, int statusCode) {
                    if (listener != null) listener.onError(desc);
                }
            });
        } catch (JSONException e) {
            if (listener != null) listener.onError(e.toString());
        }
    }

    private final String getDesc(JSONObject object) {
        return object.optString("desc");
    }

    private final int getDays(JSONObject object) {
        return object.optInt("days", 0);
    }

    private final String getDate(JSONObject object) {
        return object.optString("lastsignday");
    }

    public interface OnSignListener {
        void onSuccess(int days, String dateOrDesc);

        void onError(String desc);
    }
}
