package com.fujinbang.ui.activity.iactivity;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/6/7.
 */
public interface IUserView {
    void showUserName(String name);

    void showSex(String sex);

    void showArea(String area);

    void showDistance(String distance);

    void showDescription(String desc);

    void showPickNum(String pickNum);

    void showPostNum(String postNum);

    void showAvatar(Bitmap avatar);

    Context getActivityContext();

    int getUserId();

    void showToast(String str);
}