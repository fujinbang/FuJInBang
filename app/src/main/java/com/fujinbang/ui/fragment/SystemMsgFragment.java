package com.fujinbang.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.internet.HttpConnRequest;
import com.fujinbang.internet.UrlConstant;

import org.json.JSONObject;

/**
 * Created by VITO on 2016/7/8.
 * 管理员发布系统消息界面
 */
public class SystemMsgFragment extends Fragment implements View.OnClickListener{
    protected ImageView back;
    protected Button submit;
    protected EditText content;
    protected boolean canSubmit;
    protected Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_msg, container, false);
        mContext = getContext();
        back = (ImageView) view.findViewById(R.id.fragment_system_msg_back);
        submit = (Button) view.findViewById(R.id.fragment_system_msg_submit);
        content = (EditText) view.findViewById(R.id.fragment_system_msg_content);
        canSubmit = true;
        back.setOnClickListener(this);
        submit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_system_msg_back:
                getActivity().finish();
                break;
            case R.id.fragment_system_msg_submit:
                if (canSubmit){
                    canSubmit = false;
                    Toast.makeText(mContext, "正在发布中，请稍候...", Toast.LENGTH_LONG).show();
                    submitSystemMsg();
                }
                break;
            default:
                break;
        }
    }

    private void submitSystemMsg() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try {
                    JSONObject object = new JSONObject();
                    object.put("message", params[0]);
                    result = HttpConnRequest.request(UrlConstant.broadcastSystemMsg, "POST", object);
                }catch (Exception e){
                    e.printStackTrace();
                    result = null;
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.has("code") && object.getInt("code") == 1){
                            Toast.makeText(mContext, "发布系统消息成功！", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {
                            Toast.makeText(mContext, "发布系统消息失败！请重试", Toast.LENGTH_SHORT).show();
                            canSubmit = true;
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        canSubmit = true;
                    }
                } else {
                    Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT).show();
                    canSubmit = true;
                }
            }
        }.execute(content.getText().toString());
    }
}
