package com.fujinbang.presenter;

import android.content.Intent;

import com.fujinbang.ui.activity.PayActivity;
import com.fujinbang.global.SimpleDataBase;
import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/12.
 */
public class PayPresenter implements PaymentHandler {

    private PayActivity view;

    public PayPresenter(PayActivity view) {
        this.view = view;
    }

    /**
     * 使用ping++一支付
     *
     * @param fee 支付的费用 单位是分
     */
    public void pay(int fee) {
        //String URL = "http://218.244.151.190/demo/charge";
        String URL = "http://120.24.240.199:6789/pay/wx/unified";
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "jdpay_wap"});
        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";
        PingppLog.DEBUG = true;

        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());
        // 计算总金额（以分为单位）
        int amount = fee;
        // 构建账单json对象
        JSONObject bill = new JSONObject();
        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("token", new SimpleDataBase(view.getActivityContext()).getToken());
            extras.put("price", amount);
            extras.put("body", "1111");
            extras.put("channel","wx");
            extras.put("subject", fee * 100 + "积分");

            bill.put("order_no", orderNo);
            bill.put("price", amount);
            bill.put("subject", fee * 100 + "积分");
            bill.put("body", "111");
            bill.put("amount", amount);
            bill.put("channel","wx");
            bill.put("token", new SimpleDataBase(view.getActivityContext()).getToken());
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(view.getSupportFragmentManager(), bill.toString(), URL, this);
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {
            /**
             * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
             * error_msg：支付结果信息
             */
            int code = data.getExtras().getInt("code");
            String errorMsg = data.getExtras().getString("error_msg");

            view.showToast(code + ": " + errorMsg);
        }
    }
}