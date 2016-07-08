package com.fujinbang.presenter;

import android.graphics.Bitmap;

import com.fujinbang.ui.activity.iactivity.IUserView;
import com.fujinbang.internet.UserMsgRequest;
import com.fujinbang.presenter.ipresenter.IUserPresenter;
import com.fujinbang.setting.User;

/**
 * Created by Administrator on 2016/6/7.
 */
public class UserPresenter implements IUserPresenter {

    private IUserView view;
    /**
     * 网络请求:获取用户的信息
     */
    private UserMsgRequest request;

    public UserPresenter(IUserView view) {
        this.view = view;
        request = new UserMsgRequest(view.getActivityContext());
    }

    @Override
    public void updateUserMsg() {
        final int id = getId();
        request.getUserMsg(id, listener);
        request.getUserAvator(id, 300, 300, listener);
    }

    UserMsgRequest.OnUserListener listener = new UserMsgRequest.OnUserListener() {
        @Override
        public void onSucess(User user) {
            view.showUserName(user.getUserName());
            view.showArea(user.getArea());
            view.showSex(user.isMan() ? "男" : "女");
        }

        @Override
        public void onAvatar(Bitmap Avatar) {
            view.showAvatar(Avatar);
        }

        @Override
        public void onError(String error) {
            view.showToast("获取用户信息失败！");
        }
    };

    private final int getId() {
        return view.getUserId();
    }
}
