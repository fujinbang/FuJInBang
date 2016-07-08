package com.fujinbang.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fujinbang.R;

/**
 * Created by Administrator on 2016/3/6.
 */
public class NeibourTabView extends View {

    public static final String TAG = "zy";

    public NeibourTabView(Context context) {
        super(context);
        init();
    }

    public NeibourTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NeibourTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NeibourTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private int mCurrentBtn, mChooseBtn;

    private Bitmap[] mBitmap = new Bitmap[4];
    private Bitmap[] mBitmapOverlay = new Bitmap[4];
    private Bitmap mBitmapUnread;
    private final String[] mText = {"附近", "求助", "消息", "我的"};

    private final void init() {

        mChooseBtn = mCurrentBtn = 0;

        mBitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.nearby);
        mBitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.help);
        mBitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.message);
        mBitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.setting);

        mBitmapOverlay[0] = BitmapFactory.decodeResource(getResources(), R.drawable.nearby_overlay);
        mBitmapOverlay[1] = BitmapFactory.decodeResource(getResources(), R.drawable.help_overlay);
        mBitmapOverlay[2] = BitmapFactory.decodeResource(getResources(), R.drawable.message_overlay);
        mBitmapOverlay[3] = BitmapFactory.decodeResource(getResources(), R.drawable.setting_overlay);

        mBitmapUnread = BitmapFactory.decodeResource(getResources(), R.drawable.unread);
    }

    /**
     * 每个按钮的宽高
     */
    float mCellWidth, mCellHeight;

    /**
     * 图片的宽高
     */
    float mBitmapHeight;

    /**
     * 字体大小
     */
    float mTextSize;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, width + " , " + height);

        mCellWidth = width / 4f;
        mCellHeight = height;

        //mBitmapWidth = mCellWidth / 2f;
        mBitmapHeight = mCellHeight / 2f;
        mTextSize = height / 4f;
    }

    private int pressBtn, releaseBtn;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                pressBtn = (int) (x / mCellWidth);
                break;
            case MotionEvent.ACTION_UP:
                x = event.getX();
                releaseBtn = (int) (x / mCellWidth);

                if (releaseBtn == pressBtn && releaseBtn != mCurrentBtn) {
                    changeBtn(releaseBtn);
                }
                break;
            default:
                break;
        }
        return true;
    }

    public final void changeBtn(int toButton) {
        if (mCurrentBtn != toButton) {
            mChooseBtn = toButton;

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", 0f, 1f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCurrentBtn = mChooseBtn;
                }
            });
            objectAnimator.start();

            if (mOnClickTabListener != null) {
                mOnClickTabListener.onClick(mChooseBtn, mText[mChooseBtn]);
            }
        }
    }

    //动画的进度
    private float progress;

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < 4; i++) {
            //画图案
            drawBitmap(i, canvas);

            //画字体
            drawText(i, canvas);
        }

        canvas.save(Canvas.ALL_SAVE_FLAG);
        float left = mCurrentBtn * mCellWidth + progress * (mChooseBtn - mCurrentBtn) * mCellWidth;
        canvas.clipRect(new RectF(left, 0f, left + mCellWidth, mCellHeight));
        for (int i = 0; i < 4; i++) {
            //画遮挡图案
            drawOverlayBitmap(i, canvas);

            //画遮挡字体
            drawOverlayText(i, canvas);
        }
        canvas.restore();

        //canvas.save(Canvas.ALL_SAVE_FLAG);
        drawUnReadCount(canvas);//绘制未读信息的条数
        //canvas.restore();
    }

    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }

    public float getProgress() {
        return progress;
    }

    /**
     * 未读消息数量
     */
    private int mCount = 0;

    public void setUnReadCount(int count) {
        mCount = count;
        postInvalidate();
    }

    private final void drawBitmap(int i, Canvas canvas) {
        Matrix matrix = new Matrix();
        //matrix.setScale(mBitmapWidth / mBitmap[i].getWidth(), mBitmapHeight / mBitmap[i].getHeight());
        //matrix.postTranslate(i * mCellWidth + mBitmapWidth / 2f, mBitmapHeight / 3f);
        float scale = mBitmapHeight / mBitmap[i].getHeight();
        matrix.setScale(scale, scale);
        matrix.postTranslate(i * mCellWidth + (mCellWidth - scale * mBitmap[i].getWidth()) / 2f, mBitmapHeight / 3f);

        canvas.drawBitmap(mBitmap[i], matrix, new Paint(Paint.ANTI_ALIAS_FLAG));
    }

    private final void drawText(int i, Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(mTextSize);
        paint.setColor(0xff999999);
        float length = paint.measureText(mText[i]);//量度字体的长度
        canvas.drawText(mText[i], i * mCellWidth + (mCellWidth - length) / 2f, mCellHeight - 10, paint);
    }

    private final void drawOverlayBitmap(int i, Canvas canvas) {
        Matrix matrix = new Matrix();
        float scale = mBitmapHeight / mBitmapOverlay[i].getHeight();
        matrix.setScale(scale, scale);
        matrix.postTranslate(i * mCellWidth + (mCellWidth - scale * mBitmapOverlay[i].getWidth()) / 2f, mBitmapHeight / 3f);
        canvas.drawBitmap(mBitmapOverlay[i], matrix, new Paint(Paint.ANTI_ALIAS_FLAG));
    }

    private final void drawOverlayText(int i, Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(255, 175, 0));
        paint.setTextSize(mTextSize);
        float length = paint.measureText(mText[i]);//量度字体的长度
        canvas.drawText(mText[i], i * mCellWidth + (mCellWidth - length) / 2f, mCellHeight - 10, paint);
    }

    /**
     * 绘制未读信息的条数
     */
    private final void drawUnReadCount(Canvas canvas) {
        if (mCount != 0) {
            float margin = 10;
            float textSize = 45;
            int width = mBitmapUnread.getWidth();
            int height = mBitmapUnread.getHeight();
            canvas.drawBitmap(mBitmapUnread, 3 * mCellWidth - width - margin, margin, new Paint(Paint.ANTI_ALIAS_FLAG));
            String unread = String.valueOf(mCount);
            Paint paint = new Paint();
            paint.setTextSize(textSize);
            paint.setStrokeWidth(2);
            paint.setColor(0xffffffff);
            float len = paint.measureText(unread);
            float offset = (width - len) / 2;
            canvas.drawText(unread, 3 * mCellWidth - width - margin + offset, (height - textSize) / 2 + textSize + margin, paint);
        }
    }

    public interface onClickTabListener {
        void onClick(int tabNum, String tabName);
    }

    private onClickTabListener mOnClickTabListener;

    public void setOnClickTabListener(onClickTabListener listener) {
        mOnClickTabListener = listener;
    }
}
