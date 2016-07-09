package com.fujinbang.conversation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by VITO on 2016/6/3.
 * 群组头像ViewGroup
 */
public class GroupAvatarView extends ViewGroup {

    public GroupAvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GroupAvatarView(Context context) {
        super(context);
    }

    public GroupAvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /**
         * 遍历所有childView根据ViewGroup的宽和高对子控件进行布局
         */
        int childCount = getChildCount();
        int width;
        int height;
        int parentWidth = getMeasuredWidth();
        int parentHeight = getMeasuredHeight();
        switch (childCount) {
            case 1:
                width = parentWidth;
                height = parentHeight;
                getChildAt(0).layout(l, t, l + width, t + height);
                break;
            case 2:
                width = parentWidth / 2;
                height = parentHeight / 2;
                getChildAt(0).layout(l, parentHeight / 4, l + width, parentHeight / 4 + height);
                getChildAt(1).layout(l + width, parentHeight / 4, l + width + width, parentHeight / 4 + height);
                break;
            case 3:
                width = parentWidth / 2;
                height = parentHeight / 2;
                getChildAt(0).layout(parentWidth / 4, t, parentWidth / 4 + width, t + height);
                getChildAt(1).layout(l, parentHeight / 2, l + width, parentHeight / 2 + height);
                getChildAt(2).layout(l + width, parentHeight / 2, l + width + width, parentHeight / 2 + height);
                break;
            case 4:
                width = parentWidth / 2;
                height = parentHeight / 2;
                getChildAt(0).layout(l, t, l + width, t + height);
                getChildAt(1).layout(l + width, t, l + width + width, t + height);
                getChildAt(2).layout(l, t + height, l + width, t + height + height);
                getChildAt(3).layout(l + width, t + height, l + width + width, t + height + height);
                break;
            default:
                break;
        }
    }
}
