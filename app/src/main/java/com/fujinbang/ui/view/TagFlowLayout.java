package com.fujinbang.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fujinbang.R;

/**
 * Created by Administrator on 2016/3/10.
 */
public class TagFlowLayout extends ViewGroup implements View.OnClickListener {

    private final float textSize = 20f;
    private final static int mColumnNum = 3;

    /**
     * 正常模式：标签黄白相间
     */
    public static final int MODE_NORMAL = 0;

    /**
     * 灰模式：所有标签都是灰色的
     */
    public static final int MODE_GRAY = 1;

    /**
     * 删除模式：所有标签有删除的下标
     */
    public static final int MODE_DELETE = 2;

    public TagFlowLayout(Context context) {
        super(context);
        init();
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Bitmap empty, full, empty_close, full_close, fal;
    private int mode;

    private final void init() {
        empty = BitmapFactory.decodeResource(getResources(), R.drawable.tag_empty);
        empty_close = BitmapFactory.decodeResource(getResources(), R.drawable.tag_empty_close);
        full = BitmapFactory.decodeResource(getResources(), R.drawable.tag_full);
        full_close = BitmapFactory.decodeResource(getResources(), R.drawable.tag_full_close);
        fal = BitmapFactory.decodeResource(getResources(), R.drawable.tag_false);
        mode = MODE_NORMAL;
    }

    public void setMode(int mode) {
        if (mode != MODE_NORMAL && mode != MODE_GRAY && mode != MODE_DELETE) {
            throw new IllegalArgumentException("No such mode!");
        }
        this.mode = mode;
        requestLayout();
    }

    public int getMode() {
        return mode;
    }

    public void addTag(String tagName) {
        if (TextUtils.isEmpty(tagName)) {
            throw new IllegalArgumentException("tagName can not null");
        }
        TagView tagView = new TagView(getContext());
        tagView.setText(tagName);
        tagView.setGravity(Gravity.CENTER);
        tagView.setOnClickListener(this);
        addView(tagView);
    }

    public void removeTag(View v) {
        removeView(v);
    }

    /**
     * remove all tag with the name "tagName"
     */
    public void removeTag(String tagName) {
        for (int i = 0; i < getChildCount(); i++) {
            TagView childView = (TagView) getChildAt(i);
            String temp = childView.getText().toString();

            if (temp.contentEquals(tagName)) {
                removeView(childView);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        //textSize = width / mColumnNum / 5f;

        if (getChildCount() != 0) {
            for (int i = 0; i < getChildCount(); i++) {
                TagView childView = (TagView) getChildAt(i);
                childView.setTextSize(textSize);
                childView.setMinimumWidth((int) (getMeasuredWidth() / 4f));
            }

            measureChildren(widthMeasureSpec, heightMeasureSpec);

            int rowNum = getChildCount() / mColumnNum + 1;
            height = (int) (rowNum * getChildAt(0).getMeasuredHeight() * 1.5f);
        } else {
            height = 0;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float len = (r - l) / mColumnNum;
        for (int i = 0; i < getChildCount(); i++) {

            int column = i % mColumnNum;
            int row = i / mColumnNum;

            TagView childView = (TagView) getChildAt(i);
            float childWidth = childView.getMeasuredWidth();
            float childHeight = childView.getMeasuredHeight();

            if (mode == MODE_NORMAL) {
                if ((row % 2 == 0 && column % 2 != 0)
                        || (row % 2 != 0 && column % 2 == 0)) {
                    childView.setBackgroundBitmap(full);
                    childView.setTextColor(Color.WHITE);
                } else {
                    childView.setBackgroundBitmap(empty);
                    childView.setTextColor(Color.rgb(255, 175, 0));
                }
            } else if (mode == MODE_GRAY) {
                childView.setBackgroundBitmap(fal);
                childView.setTextColor(Color.rgb(255, 175, 0));
            } else if (mode == MODE_DELETE) {
                if ((row % 2 == 0 && column % 2 != 0)
                        || (row % 2 != 0 && column % 2 == 0)) {
                    childView.setBackgroundBitmap(full_close);
                    childView.setTextColor(Color.WHITE);
                } else {
                    childView.setBackgroundBitmap(empty_close);
                    childView.setTextColor(Color.rgb(255, 175, 0));
                }
            }

            float offsetX = (len - childWidth) / 2f;
            float offsetY = childHeight / 4f;
            int left = (int) (len * column + offsetX);
            int top = (int) (row * (childHeight + 2 * offsetY) + offsetY);

            childView.layout(left, top, (int) (left + childWidth), (int) (top + childHeight));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClick(v, ((TagView) v).getText().toString());
        }
    }

    public interface onClickTagViewListener {
        void onClick(View v, String TagName);
    }

    onClickTagViewListener mListener;

    public void setOnClickTagViewListener(onClickTagViewListener listener) {
        mListener = listener;
    }

    private class TagView extends TextView {

        public TagView(Context context) {
            super(context);
            init();
        }

        public TagView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public TagView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init();
        }

        private final void init() {
        }

        Bitmap mBackground;
        float mWidth, mHeight;

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            //Log.i("zy", "onMeasureChild:" + mWidth + " , " + mHeight);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            if (mBackground != null) {
                canvas.save();
                canvas.drawBitmap(mBackground, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG));
                canvas.restore();
            }
            super.onDraw(canvas);
        }

        public void setBackgroundBitmap(Bitmap background) {
            if (background != null && mWidth != 0 && mHeight != 0) {
                Matrix matrix = new Matrix();
                matrix.setScale(mWidth / background.getWidth(), mHeight / background.getHeight());
                mBackground = Bitmap.createBitmap(background, 0, 0, background.getWidth(), background.getHeight(), matrix, false);

                invalidate();
            }
        }
    }
}
