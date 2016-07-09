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
import com.fujinbang.ui.activity.iactivity.ISettingEmailView;
import com.fujinbang.presenter.SettingPresenter;
import com.fujinbang.presenter.ipresenter.ISettingPresenter;

public class EmailSettingActivity extends BaseActivity implements View.OnTouchListener, ISettingEmailView {

    private EditText et_setting_Email;
    private ImageView iv_back;
    private Button btn_ok;
    private ISettingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_setting);

        initView();
    }

    private final void initView() {
        presenter = new SettingPresenter(this);

        et_setting_Email = (EditText) findViewById(R.id.et_setting_Email);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        String email = getIntent().getStringExtra("email");
        if (email != null) et_setting_Email.setText(email);
        iv_back.setOnTouchListener(this);
        btn_ok.setOnTouchListener(this);
    }

    public final void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishActivity();
                break;
            case R.id.btn_ok:
                presenter.uploadEmail();
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
    public String getEmail() {
        return et_setting_Email.getText().toString();
    }

    @Override
    public void setEmail(String email) {
        if (email != null)
            et_setting_Email.setText(email);
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
        Toast.makeText(getActivityContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static final void startActivity(Context context, String email) {
        Intent intent = new Intent(context, EmailSettingActivity.class);
        intent.putExtra("email", email);
        context.startActivity(intent);
    }
}
