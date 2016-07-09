package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fujinbang.R;

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_5, btn_10, btn_15, btn_20, btn_50, btn_150;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        initView();
    }

    private final void initView() {
        btn_5 = (Button) findViewById(R.id.btn_recharge_5);
        btn_10 = (Button) findViewById(R.id.btn_recharge_10);
        btn_15 = (Button) findViewById(R.id.btn_recharge_15);
        btn_20 = (Button) findViewById(R.id.btn_recharge_20);
        btn_50 = (Button) findViewById(R.id.btn_recharge_50);
        btn_150 = (Button) findViewById(R.id.btn_recharge_150);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        btn_5.setOnClickListener(this);
        btn_10.setOnClickListener(this);
        btn_15.setOnClickListener(this);
        btn_20.setOnClickListener(this);
        btn_50.setOnClickListener(this);
        btn_150.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge_5:
                PayActivity.startActivity(this, 5, 50);
                break;
            case R.id.btn_recharge_10:
                PayActivity.startActivity(this, 10, 100);
                break;
            case R.id.btn_recharge_15:
                PayActivity.startActivity(this, 15, 150);
                break;
            case R.id.btn_recharge_20:
                PayActivity.startActivity(this, 20, 200);
                break;
            case R.id.btn_recharge_50:
                PayActivity.startActivity(this, 50, 500);
                break;
            case R.id.btn_recharge_150:
                PayActivity.startActivity(this, 150, 1500);
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RechargeActivity.class));
    }
}
