package com.fujinbang.internet;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/6.
 */
public abstract class InternetRequest {

    private final static String TAG = "InternetRequest";

    private RequestQueue mRequestQueue;

    public InternetRequest(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * 从网络上获取一段json
     *
     * @param url
     * @param listener
     */
    protected final void getJsonObject(String url, @Nullable JSONObject postJson, final OnResponseListener<JSONObject> listener) {
        final JsonObjectRequest request = new JsonObjectRequest(url, postJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null)
                    Log.i(TAG, jsonObject.toString());
                if (listener != null && jsonObject != null) {
                    listener.onSuccess(jsonObject);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (listener != null && volleyError != null) {
                    listener.onError(volleyError.toString(), 0);
                }
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String je = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(je), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException var3) {
                    return Response.error(new ParseError(var3));
                } catch (JSONException var4) {
                    return Response.error(new ParseError(var4));
                }
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000,//默认超时时间
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//对于请求失败之后的请求，并不会隔相同的时间去请求Server，不会以线性的时间增长去请求，而是一个曲线增长，一次比一次长，如果backoff因子是2，当前超时为3，即下次再请求隔6S​
        mRequestQueue.add(request);
    }

    protected final void postFile(String url, byte[] file, String fileName, String token, final OnResponseListener<JSONObject> listener) {
        String[] key = {"token", "key", "scope"};
        String[] value = new String[key.length];
        value[0] = token;
        value[1] = fileName;
        value[2] = "fujinbang:" + fileName;
        FileRequest request = new FileRequest(url, key, value, fileName, file, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                if (listener != null) listener.onSuccess(object);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (listener != null) listener.onError(volleyError.toString(), 0);
            }
        });
        mRequestQueue.add(request);
    }

    class FileRequest extends Request<JSONObject> {

        private String[] mKey, mValue;
        private byte[] mFile;
        private String mFileName;
        Response.Listener<JSONObject> mListener;
        private String BOUNDARY = "---------8888888888888"; //数据分隔线
        private String MULTIPART_FORM_DATA = "multipart/form-data";

        public FileRequest(String url, String[] key, String[] value, String fileName, byte[] file, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(-1, url, errorListener);
            mKey = key;
            mValue = value;
            mListener = listener;
            mFile = file;
            mFileName = fileName;
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String je = new String(response.data, "UTF-8");
                return Response.success(new JSONObject(je), HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException var3) {
                return Response.error(new ParseError(var3));
            } catch (JSONException var4) {
                return Response.error(new ParseError(var4));
            }
        }

        @Override
        public String getBodyContentType() {
            return MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int N = mKey.length;
            for (int i = 0; i < N; i++) {
                String key = mKey[i];
                String value = mValue[i];

                StringBuffer sb = new StringBuffer();
                /*第一行:"--" + boundary + "\r\n" ;*/
                sb.append("--" + BOUNDARY);
                sb.append("\r\n");
                /*第二行:"Content-Disposition: form-data; name="参数的名称"" + "\r\n" ;*/
                sb.append("Content-Disposition: form-data;");
                sb.append("name=\"");
                sb.append(key);
                sb.append("\"");
                sb.append("\r\n");
                /*第三行:"\r\n" ;*/
                sb.append("\r\n");
                /*第四行:"参数的值" + "\r\n" ;*/
                sb.append(value);
                sb.append("\r\n");
                try {
                    bos.write(sb.toString().getBytes("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //写入二进制文件
            StringBuffer sb = new StringBuffer();
            sb.append("--" + BOUNDARY);
            sb.append("\r\n");
            //第二行:"Content-Disposition: form-data; name="参数的名称"" + "\r\n" ;
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"");
            sb.append(mFileName);
            sb.append("\"\r\n");
            //第三行:"Content-Type: application/octet-stream\r\n" ;
            sb.append("Content-Type: application/octet-stream\r\n");
            //第四行
            sb.append("Content-Transfer-Encoding: binary\r\n");
            //第五行
            sb.append("\r\n");
            try {
                bos.write(sb.toString().getBytes("utf-8"));
                bos.write(mFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*结尾行:"--" + boundary + "--" + "\r\n" ;*/
            String endLine = "--" + BOUNDARY + "--" + "\r\n";
            try {
                bos.write(endLine.toString().getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v("zy", "=====formText====\n" + bos.toString());
            return bos.toByteArray();
        }

        @Override
        protected void deliverResponse(JSONObject object) {
            mListener.onResponse(object);
        }
    }

    /**
     * 从网络上获取一张图片
     *
     * @param url
     * @param maxWidth  图片最大宽度
     * @param maxHeight 图片最大长度
     * @param listener
     */
    protected final void getBitmap(String url, int maxWidth, int maxHeight, final OnResponseListener<Bitmap> listener) {
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                if (listener != null && bitmap != null) {
                    listener.onSuccess(bitmap);
                }
            }
        }, maxWidth, maxHeight, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (listener != null && volleyError != null) {
                    listener.onError(volleyError.toString(), 0);
                }
            }
        });
        mRequestQueue.add(request);
    }

    public interface OnResponseListener<T> {
        void onSuccess(T object);

        void onError(String desc, int statusCode);
    }
}
