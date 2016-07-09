package com.fujinbang.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fujinbang.R;

/**
 * Created by Administrator on 2016/4/22.
 */
public class CheckInButton extends ImageView {
    /**
     * 是否已经点击过签到
     */
    private boolean mIsChecked = false;
    /**
     * 获得的积分（上限7）
     */
    private String mIntegration = "1";
    /**
     * 已签到的天数
     */
    private String mDays = "0";

    public CheckInButton(Context context) {
        super(context);
    }

    public CheckInButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckInButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckInButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String toDraw;
        float textSize;

        Paint paint = new Paint();
        paint.setColor(0xffffffff);

        if (!isChecked()) {
            toDraw = mDays;
        } else {
            toDraw = mIntegration;
        }

        //根据字符串的长度调整字符的大小，字符串越长字符越小
        textSize = getWidth() / 6f;
        paint.setTextSize(textSize);
        while (paint.measureText(toDraw) > getWidth() / 6f) {
            textSize /= 2f;
            paint.setTextSize(textSize);
        }

        //画上字符串
        canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        canvas.drawText(toDraw, getWidth() / 2f, getHeight() * 7f / 9f, paint);
        canvas.restore();
    }

    public void setDays(int days) {
        mDays = String.valueOf(days);
    }

    public void setIntegration(int integration) {
        mIntegration = String.valueOf(integration);
    }

    public void setCheck(boolean isChecked) {
        mIsChecked = isChecked;
        if (isChecked) {
            setImageResource(R.drawable.mission_checkin_integration);
            setClickable(false);
        } else {
            setImageResource(R.drawable.mission_checkin);
            setClickable(true);
        }
    }

    public boolean isChecked() {
        return mIsChecked;
    }
}
