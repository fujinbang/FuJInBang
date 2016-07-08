package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.presenter.PayPresenter;

public class PayActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView tv_fee, tv_integration;
    //private RadioButton rb_alipay, rb_weixinpay;
    private Button btn_pay;
    private ImageView iv_back;
    private PayPresenter presenter;
    /**
     * 购买的积分和所需要的金额
     */
    private int fee, integration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        fee = getIntent().getIntExtra("fee", 0);
        integration = getIntent().getIntExtra("integration", 0);

        initView();
        initPresenter();
    }

    private final void initView() {
        tv_fee = (TextView) findViewById(R.id.tv_pay_fee);
        tv_integration = (TextView) findViewById(R.id.tv_pay_integration);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        //rb_alipay = (RadioButton) findViewById(R.id.rb_alipay);
        //rb_weixinpay = (RadioButton) findViewById(R.id.rb_weixinpay);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_back.setOnClickListener(this);
        btn_pay.setOnClickListener(this);

        //rb_alipay.setOnCheckedChangeListener(this);
        //rb_weixinpay.setOnCheckedChangeListener(this);

        tv_fee.setText("￥" + fee);
        tv_integration.setText("当前选择：" + integration + "积分");
    }

    private final void initPresenter() {
        presenter = new PayPresenter(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (buttonView.getId() == R.id.rb_alipay && isChecked) {
//            rb_weixinpay.setChecked(false);
//            rb_alipay.setChecked(true);
//        } else if (buttonView.getId() == R.id.rb_weixinpay && isChecked) {
//            rb_alipay.setChecked(false);
//            rb_weixinpay.setChecked(true);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_pay:
//                if (rb_alipay.isChecked()) {
//                    presenter.pay(PayPresenter.ALIPAY);
//                } else if (rb_weixinpay.isChecked()) {
//                    presenter.pay(PayPresenter.WEIXINPAY);
//                }
                presenter.pay(fee * 100);
                break;
            default:
                break;
        }
    }

    public static void startActivity(Context context, int fee, int integration) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("fee", fee);
        intent.putExtra("integration", integration);
        context.startActivity(intent);
    }

    public int getFee() {
        return fee;
    }

    public String getGoodsDescription() {
        return "附近帮" + integration + "积分";
    }

    public Context getActivityContext() {
        return this;
    }

    public void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
}
