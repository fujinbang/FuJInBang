package com.fujinbang.internet;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLClassLoader;

/**
 * Created by Administrator on 2016/7/9.
 */
public class FeedBackRequest extends InternetRequest {
    public FeedBackRequest(Context context) {
        super(context);
    }

    public void feedback(String token, String content, final OnFeedBackListener listener) {
        String url = UrlConstant.feedback;
        try {
            JSONObject post = new JSONObject();
            post.put("token", token);
            post.put("content", content);
            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    if (listener != null) listener.onFeedBack(Json2String(object));
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

    private final String Json2String(JSONObject object) {
        return object.optString("desc", "fail");
    }

    public interface OnFeedBackListener {
        void onFeedBack(String desc);

        void onError(String error);
    }
}
