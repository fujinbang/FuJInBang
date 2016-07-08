package com.fujinbang.presenter.ipresenter;

import com.fujinbang.helplist.HelpMsg;

/**
 * Created by Administrator on 2016/5/29.
 */
public interface IHelpListPresenter {
    void joinHelpList(int helpId);

    void getHelpList(int num);

    void showDetail(HelpMsg msg);
}
