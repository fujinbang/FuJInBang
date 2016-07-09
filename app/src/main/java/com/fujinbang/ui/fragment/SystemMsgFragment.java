package com.fujinbang.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fujinbang.R;

/**
 * Created by VITO on 2016/7/8.
 * 管理员发布系统消息界面
 */
public class SystemMsgFragment extends Fragment implements View.OnClickListener{
    protected ImageView back;
    protected Button submit;
    protected EditText content;
    protected boolean canSubmit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_msg, container, false);
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

                }
                break;
            default:
                break;
        }
    }
}
