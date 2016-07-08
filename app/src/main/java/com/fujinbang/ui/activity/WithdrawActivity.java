package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fujinbang.R;
import com.fujinbang.global.SimpleDataBase;

public class WithdrawActivity extends BaseActivity implements View.OnTouchListener {

    private ImageView iv_back;
    private RelativeLayout rl_500, rl_700, rl_900, rl_1000;
    private TextView tv_integration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        initView();
    }

    private final void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);

        rl_500 = (RelativeLayout) findViewById(R.id.rl_withdraw_500);
        rl_700 = (RelativeLayout) findViewById(R.id.rl_withdraw_700);
        rl_900 = (RelativeLayout) findViewById(R.id.rl_withdraw_900);
        rl_1000 = (RelativeLayout) findViewById(R.id.rl_withdraw_1000);

        iv_back.setOnTouchListener(this);
        rl_500.setOnTouchListener(this);
        rl_700.setOnTouchListener(this);
        rl_900.setOnTouchListener(this);
        rl_1000.setOnTouchListener(this);

        tv_integration = (TextView) findViewById(R.id.tv_integration);
        tv_integration.setText(new SimpleDataBase(this).getIntegration() + "");
    }

    private final void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_withdraw_500:
                break;
            case R.id.rl_withdraw_700:
                break;
            case R.id.rl_withdraw_900:
                break;
            case R.id.rl_withdraw_1000:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundColor(0xffe8e8e8);
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundColor(0x00000000);
                onClick(v);
                break;
            case MotionEvent.ACTION_MOVE:
                v.setBackgroundColor(0x00000000);
                break;
            default:
                break;
        }
        return true;
    }

    public static final void startActivity(Context context) {
        context.startActivity(new Intent(context, WithdrawActivity.class));
    }
}
