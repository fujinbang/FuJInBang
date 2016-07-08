package com.fujinbang.presenter;

import android.content.Context;

import com.fujinbang.ui.activity.iactivity.ITagView;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.internet.TagRequest;
import com.fujinbang.presenter.ipresenter.ITagPresenter;

/**
 * Created by Administrator on 2016/5/29.
 */
public class TagPresenter implements ITagPresenter {
    private ITagView view;
    private Context context;
    private TagRequest request;
    private SimpleDataBase db;

    public TagPresenter(ITagView view) {
        this.view = view;
        context = view.getActivityContext();
        request = new TagRequest(context);
        db = new SimpleDataBase(context);
    }

    @Override
    public void uploadTag(String tag) {
        request.addTag(tag, String.valueOf(db.getClientId()), db.getToken());
    }

    @Override
    public void getTag() {
        request.getTag(db.getClientId());
    }
}
