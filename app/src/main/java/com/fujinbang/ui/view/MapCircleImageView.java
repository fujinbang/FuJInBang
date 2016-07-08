package com.fujinbang.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.fourmob.datetimepicker.Utils;

/**
 * Created by Administrator on 2016/5/17.
 * 为了使地图上的头像图片保持一个合适的大小，在CircleImageView基础上固定了宽高
 */
public class MapCircleImageView extends CircleImageView {
    public MapCircleImageView(Context context) {
        super(context);
        init(context);
    }

    public MapCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MapCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MapCircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private int dp56;

    private void init(Context context) {
        setImageAlpha(0);
        dp56 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, context.getResources().getDisplayMetrics());
    }

    /**
     * 残暴地把宽高固定到200，纯粹个人喜好
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(dp56, dp56);
    }
}
