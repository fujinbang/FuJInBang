package com.fujinbang.ui.activity.iactivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import com.fujinbang.helplist.HelpMsg;

import java.util.List;

/**
 * Created by Administrator on 2016/5/29.
 */
public interface IHelpListFragment {
    void setHelpList(List<HelpMsg> list);

    Context getFragmentContext();

    Fragment getFragment();

    void hideProgressBar();

    void showProgressBar();

    void updateListContent(boolean isShowProgress);

    void clearList();

    void updateAvatar(int id, Bitmap avatar);

    void updatePos(double x, double y);

    void showToast(String str);

    void addHelpMsg(HelpMsg msg);

    void completeRefresh();
}
