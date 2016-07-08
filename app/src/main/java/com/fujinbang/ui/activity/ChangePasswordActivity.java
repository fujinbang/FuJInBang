package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.ui.activity.iactivity.ISetActivity;
import com.fujinbang.internet.UserMsgUpload;
import com.fujinbang.presenter.SetPresenter;
import com.fujinbang.presenter.ipresenter.ISetPresenter;

public class ChangePasswordActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener, ISetActivity {

    private ImageView iv_back;
    private Button btn_complete;
    private EditText et_pwd_1, et_pwd_2;
    private ISetPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initPresenter();
        initView();
    }

    private final void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_complete = (Button) findViewById(R.id.btn_change_psd_complete);
        et_pwd_1 = (EditText) findViewById(R.id.et_change_psd_1);
        et_pwd_2 = (EditText) findViewById(R.id.et_change_psd_2);

        iv_back.setOnTouchListener(this);
        btn_complete.setOnClickListener(this);
    }

    private final void initPresenter() {
        presenter = new SetPresenter(this);
    }

    @Override
    public final void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_change_psd_complete:
                String psd = et_pwd_1.getText().toString();
                String psd2 = et_pwd_2.getText().toString();
                if (psd.equals(psd2)) {
                    presenter.uploadPassword(psd, new UserMsgUpload.OnUserUploadListener() {
                        @Override
                        public void OnSuccess(String desc) {
                            finish();
                        }

                        @Override
                        public void OnError(String desc) {
                            showToast("修改密码失败！");
                            Log.e("zy", "ChangePsdActivity upload psd error " + desc);
                        }
                    });
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "密码不相等", Toast.LENGTH_SHORT).show();
                }
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
                v.setBackgroundResource(R.drawable.zy_linearlayout_bg);
                this.onClick(v);
                break;
            default:
                break;
        }
        return true;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ChangePasswordActivity.class));
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }
}
