package com.fujinbang.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fujinbang.R;
import com.fujinbang.global.StatusBarCompat;

/**
 * Created by VITO on 2016/5/19.
 */
public class MyHelpActivity extends Activity {
    ImageView personal_help_back;
    private TextView reqDesc;
    private FrameLayout play_sound;
    private View animView;
    private TextView record_time;
    private TextView starttime;
    private TextView endtime;
    private TextView textbonus;
    private TextView neednum;
    private TextView sex;
    private TextView location;
    private Button cancelBtn;


    private int mMinWidth; //最小的item宽度
    private int mMaxWidth; //最大的item宽度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_help);
        StatusBarCompat.compat(this);
        initView();
    }

    private final void initView() {
        personal_help_back = (ImageView) findViewById(R.id.my_help_back);
        personal_help_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyHelpActivity.this.finish();
            }
        });

        reqDesc = (TextView) findViewById(R.id.my_help_reqdes);
        starttime = (TextView) findViewById(R.id.my_help_starttime);
        endtime = (TextView) findViewById(R.id.my_help_endtime);
        textbonus = (TextView) findViewById(R.id.my_help_textbonus);
        neednum = (TextView) findViewById(R.id.my_help_num);
        sex = (TextView) findViewById(R.id.my_help_sex);
        location = (TextView) findViewById(R.id.my_help_location);
        cancelBtn = (Button) findViewById(R.id.my_help_cancel_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        record_time = (TextView)findViewById(R.id.my_help_recoder_time);
        record_time.setText("" + "\"");
        play_sound = (FrameLayout) findViewById(R.id.my_help_recoder_lenght);
        animView = findViewById(R.id.my_help_recoder_anim);
        //获取屏幕的宽度
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxWidth = (int) (outMetrics.widthPixels * 0.4f);
        mMinWidth = (int) (outMetrics.widthPixels * 0.15f);
        ViewGroup.LayoutParams lp = play_sound.getLayoutParams();
        //lp.width = (int) (mMinWidth + (mMaxWidth / 60f)* seconds);//对话框长度
        play_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放帧动画
                animView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
                animation.start();
                //播放录音
                /*
                MediaManager.playSound(fpath, new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer mp) {
                        //停止播放帧动画
                        animView.setBackgroundResource(R.drawable.adj);
                    }
                });*/
            }
        });
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyHelpActivity.class);
        context.startActivity(intent);
    }
}
