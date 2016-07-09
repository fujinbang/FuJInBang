package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.ui.activity.iactivity.ISettingPhoneNumView;
import com.fujinbang.presenter.SettingPresenter;
import com.fujinbang.presenter.ipresenter.ISettingPresenter;


public class PhoneNumSettingActivity extends BaseActivity implements View.OnTouchListener, ISettingPhoneNumView {

    private EditText et_setting_phoneNum;
    private ImageView iv_back;
    private Button btn_ok;
    private ISettingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_num_setting);

        initView();
    }

    private final void initView() {
        presenter = new SettingPresenter(this);

        et_setting_phoneNum = (EditText) findViewById(R.id.et_setting_phoneNum);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        String phoneNum = getIntent().getStringExtra("phoneNum");
        if (phoneNum != null) et_setting_phoneNum.setText(phoneNum);
        iv_back.setOnTouchListener(this);
        btn_ok.setOnTouchListener(this);
    }

    public final void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_ok:
                presenter.uploadPhoneNum();
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
            default:
                break;
        }
        return true;
    }

    @Override
    public void setPhoneNum(String phoneNum) {
        if (phoneNum != null)
            et_setting_phoneNum.setText(phoneNum);
    }

    @Override
    public String getPhoneNum() {
        return et_setting_phoneNum.getText().toString();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static final void startActivity(Context context, String phoneNum) {
        Intent intent = new Intent(context, PhoneNumSettingActivity.class);
        intent.putExtra("phoneNum", phoneNum);
        context.startActivity(intent);
    }
}
