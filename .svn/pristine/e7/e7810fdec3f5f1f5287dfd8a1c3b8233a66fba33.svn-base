package com.fujinbang.internet;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/29.
 */
public class TagRequest extends InternetRequest {
    public TagRequest(Context context) {
        super(context);
    }

    public void getTag(int id) {
        String url = UrlConstant.label + "?host=" + id;
        getJsonObject(url, null, new OnResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject object) {
                Log.i("zy", object.toString());
            }

            @Override
            public void onError(String desc, int statusCode) {
                Log.e("zy", desc);
            }
        });
    }

    public void addTag(String tag, String id, String token) {
        String url = UrlConstant.label;
        try {
            JSONObject post = new JSONObject();
            post.put("token", token);
            post.put("id", id);
            post.put("labelname", tag);
            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    Log.i("zy", object.toString());
                }

                @Override
                public void onError(String desc, int statusCode) {
                    Log.e("zy", desc);
                }
            });
        } catch (JSONException e) {
            Log.e("zy", e.toString());
        }
    }
}
