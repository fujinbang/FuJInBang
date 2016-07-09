package com.fujinbang.presenter;

import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.internet.FeedBackRequest;
import com.fujinbang.ui.activity.FeedbackOnlineActivity;

/**
 * Created by Administrator on 2016/7/9.
 */
public class FeedBackPresenter {
    private FeedbackOnlineActivity view;
    private SimpleDataBase db;

    public FeedBackPresenter(FeedbackOnlineActivity view) {
        this.view = view;
        db = new SimpleDataBase(view);
    }

    public void feedback(String content) {
        if (content != "")
            new FeedBackRequest(view).feedback(db.getToken(), content, new FeedBackRequest.OnFeedBackListener() {
                @Override
                public void onFeedBack(String desc) {
                    view.showToast("反馈成功");
                }

                @Override
                public void onError(String error) {
                }
            });
        else
            view.showToast("内容不能为空");
    }
}
