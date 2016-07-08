package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fujinbang.R;
import com.fujinbang.ui.activity.iactivity.ITagView;
import com.fujinbang.presenter.ipresenter.ITagPresenter;
import com.fujinbang.presenter.TagPresenter;
import com.fujinbang.ui.view.TagFlowLayout;

public class TagActivity extends BaseActivity implements ITagView {

    private ImageView iv_back;
    private TextView tv_manager;
    private TagFlowLayout userTagFlowLayout, systemTagFlowLayout;
    private ITagPresenter presenter;

    private boolean isDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        presenter = new TagPresenter(this);
        initBackButton();
        initManagerButton();
        initTagFlowLayout();
    }

    private final void initBackButton() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagActivity.this.finish();
            }
        });
    }

    private final void initManagerButton() {
        tv_manager = (TextView) findViewById(R.id.tv_manager);
        tv_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDelete) {
                    userTagFlowLayout.setMode(TagFlowLayout.MODE_NORMAL);
                    userTagFlowLayout.addTag("+");
                } else {
                    userTagFlowLayout.removeTag("+");
                    userTagFlowLayout.setMode(TagFlowLayout.MODE_DELETE);
                }
                isDelete = !isDelete;
            }
        });
    }

    private final void initTagFlowLayout() {
        userTagFlowLayout = (TagFlowLayout) findViewById(R.id.tfl_tag);
        userTagFlowLayout.setMode(TagFlowLayout.MODE_NORMAL);
        userTagFlowLayout.setOnClickTagViewListener(mUserListener);
        userTagFlowLayout.addTag("跑腿");
        userTagFlowLayout.addTag("作业");
        userTagFlowLayout.addTag("陪聊");
        userTagFlowLayout.addTag("快递");
        userTagFlowLayout.addTag("占位");
        userTagFlowLayout.addTag("抢课");
        userTagFlowLayout.addTag("+");

        systemTagFlowLayout = (TagFlowLayout) findViewById(R.id.tfl_system);
        systemTagFlowLayout.setOnClickTagViewListener(mSystemListener);
        systemTagFlowLayout.setMode(TagFlowLayout.MODE_GRAY);
        systemTagFlowLayout.addTag("电脑");
        systemTagFlowLayout.addTag("约");
        systemTagFlowLayout.addTag("APP");
        systemTagFlowLayout.addTag("帅");

        presenter.uploadTag("跑腿");
        presenter.uploadTag("作业");
        presenter.getTag();
    }

    TagFlowLayout.onClickTagViewListener mUserListener = new TagFlowLayout.onClickTagViewListener() {
        @Override
        public void onClick(View v, String TagName) {
            if (isDelete) {
                userTagFlowLayout.removeView(v);
                systemTagFlowLayout.addTag(TagName);
            } else if (TagName == "+") {

            }
        }
    };

    TagFlowLayout.onClickTagViewListener mSystemListener = new TagFlowLayout.onClickTagViewListener() {
        @Override
        public void onClick(View v, String TagName) {
            if (!isDelete) {
                userTagFlowLayout.removeTag("+");
                systemTagFlowLayout.removeTag(v);
                userTagFlowLayout.addTag(TagName);
                userTagFlowLayout.addTag("+");
            }
        }
    };

    public final static void startActivity(Context context) {
        context.startActivity(new Intent(context, TagActivity.class));
    }

    @Override
    public void showNewTag(String str) {
        systemTagFlowLayout.addTag(str);
    }

    @Override
    public Context getActivityContext() {
        return this;
    }
}
