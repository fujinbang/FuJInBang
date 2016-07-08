package com.fujinbang.presenter;

import android.content.Context;
import android.util.Log;

import com.fujinbang.ui.activity.iactivity.ISettingEmailView;
import com.fujinbang.ui.activity.iactivity.ISettingPhoneNumView;
import com.fujinbang.ui.activity.iactivity.ISettingUserNameView;
import com.fujinbang.ui.activity.iactivity.ISettingView;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.internet.UserMsgUpload;
import com.fujinbang.presenter.ipresenter.ISettingPresenter;

/**
 * Created by Administrator on 2016/5/29.
 */
public class SettingPresenter implements ISettingPresenter {

    private Context context;
    private ISettingView view;
    private UserMsgUpload userMsgUpload;
    private SimpleDataBase db;
    private UserMsgUpload.OnUserUploadListener listener;

    public SettingPresenter(ISettingView view) {
        this.view = view;
        this.context = view.getActivityContext();
        userMsgUpload = new UserMsgUpload(context);
        db = new SimpleDataBase(context);
        listener = new uploadListener();
    }

    @Override
    public void uploadUserName() {
        if (view instanceof ISettingUserNameView) {
            String userName = ((ISettingUserNameView) view).getUserName();
            userMsgUpload.uploadName(db.getToken(), userName, listener);
        }
    }

    @Override
    public void uploadEmail() {
        if (view instanceof ISettingEmailView) {
            String email = ((ISettingEmailView) view).getEmail();
            userMsgUpload.uploadEmail(db.getToken(), email, listener);
        }
    }

    @Override
    public void uploadPhoneNum() {
        if (view instanceof ISettingPhoneNumView) {
            String phoneNum = ((ISettingPhoneNumView) view).getPhoneNum();
            userMsgUpload.uploadPhoneNum(db.getToken(), phoneNum, listener);
        }
    }

    class uploadListener implements UserMsgUpload.OnUserUploadListener {
        @Override
        public void OnSuccess(String desc) {
            view.finishActivity();
        }

        @Override
        public void OnError(String desc) {
            Log.e("zy", "save user msg error:" + desc);
            view.showToast("更新信息失败！");
        }
    }
}