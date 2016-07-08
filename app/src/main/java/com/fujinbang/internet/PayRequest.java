package com.fujinbang.internet;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/12.
 */
public class PayRequest extends InternetRequest {
    public PayRequest(Context context) {
        super(context);
    }

    public void getPrepareId(int fee, String desc, final OnPayListener listener) {
        String url = "http://120.24.240.199:6789/pay/wx/unified";
        JSONObject post = new JSONObject();
        try {
            post.put("body", desc);
            post.put("total_fee", fee);
            getJsonObject(url, post, new OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject object) {
                    if (listener != null)
                        listener.onSuccess(json2PrepayId(object), json2Sign(object), json2NonceStr(object));
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

    public interface OnPayListener {
        void onSuccess(String prepareId, String sign, String nonce_str);

        void onError(String desc);
    }

    private final String json2PrepayId(JSONObject object) {
        return object.optString("prepayid");
    }

    private final String json2Sign(JSONObject object) {
        return object.optString("sign");
    }

    private final String json2NonceStr(JSONObject object) {
        return object.optString("nonce_str");
    }
}
