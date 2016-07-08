package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.global.SimpleDataBase;

public class SetPayPwdActivity extends BaseActivity {

    private EditText et_pwd, et_confirm_pwd;
    private ImageView iv_back;
    private Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pay_pwd);

        initView();
    }

    private final void initView() {
        et_pwd = (EditText) findViewById(R.id.et_set_pwd);
        et_confirm_pwd = (EditText) findViewById(R.id.et_set_confirm_pwd);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_ok = (Button) findViewById(R.id.btn_set_ok);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_pwd.getText().toString();
                String pwd2 = et_confirm_pwd.getText().toString();

                if (pwd.equals(pwd2) && pwd.length() == 6) {
                    new SimpleDataBase(SetPayPwdActivity.this).putPayPassword(pwd);
                    finish();
                } else {
                    Toast.makeText(SetPayPwdActivity.this, "密码不一致或长度不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SetPayPwdActivity.class));
    }
}
