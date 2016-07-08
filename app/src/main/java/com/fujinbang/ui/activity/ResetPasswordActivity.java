package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.internet.HttpConnRequest;
import com.fujinbang.internet.MD5;
import com.fujinbang.internet.UrlConstant;

import org.json.JSONObject;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    protected ImageView back;
    protected EditText phonenum, password1, password2, vcode;
    protected ImageButton see1, see2;
    protected RelativeLayout getcode;
    protected Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        back = (ImageView) findViewById(R.id.reset_password_back);
        phonenum = (EditText) findViewById(R.id.reset_password_phonenum);
        password1 = (EditText) findViewById(R.id.reset_password_editText);
        password2 = (EditText) findViewById(R.id.reset_password_editText2);
        vcode = (EditText) findViewById(R.id.reset_password_vcode);
        see1 = (ImageButton) findViewById(R.id.reset_password_see_password);
        see2 = (ImageButton) findViewById(R.id.reset_password_see_password2);
        getcode = (RelativeLayout) findViewById(R.id.reset_password_getCode);
        submit = (Button) findViewById(R.id.reset_password_submit);

        phonenum.addTextChangedListener(mTextWatcher);
        password1.addTextChangedListener(mTextWatcher);
        password2.addTextChangedListener(mTextWatcher);
        vcode.addTextChangedListener(mTextWatcher);

        back.setOnClickListener(this);
        see1.setOnClickListener(this);
        see2.setOnClickListener(this);
        getcode.setOnClickListener(this);
        submit.setOnClickListener(this);

    }


    protected boolean can_see1 = false;
    protected boolean can_see2 = false;
    protected boolean canSend = true;
    protected boolean canSubmit = false;
    protected boolean Sign1, Sign2, Sign3, Sign4;
    @Override
    public final void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_password_back:
                finish();
                break;
            case R.id.reset_password_see_password:
                if (!can_see1){
                    password1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    can_see1 = true;
                }else {
                    password1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    can_see1 = false;
                }
                Editable etable = password1.getText();
                Selection.setSelection(etable, etable.length());
                break;
            case R.id.reset_password_see_password2:
                if (!can_see2){
                    password2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    can_see2 = true;
                }else {
                    password2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    can_see2 = false;
                }
                Editable etable2 = password2.getText();
                Selection.setSelection(etable2, etable2.length());
                break;
            case R.id.reset_password_getCode:
                if (phonenum.getText().length() == 11 && canSend){
                    verTask();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.reset_password_submit:
                if (canSubmit){
                    resetTask();
                }else if (!Sign1 & !Sign2 & !Sign3 & !Sign4){
                    Toast.makeText(ResetPasswordActivity.this, "请输入有效信息", Toast.LENGTH_SHORT).show();
                } else if (!Sign1){
                    Toast.makeText(ResetPasswordActivity.this, "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                } else if (!Sign2){
                    Toast.makeText(ResetPasswordActivity.this, "请设置6~10位有效密码", Toast.LENGTH_SHORT).show();
                } else if (!Sign3){
                    Toast.makeText(ResetPasswordActivity.this, "请输入有效的验证码", Toast.LENGTH_SHORT).show();
                } else if (!Sign4){
                    Toast.makeText(ResetPasswordActivity.this, "密码不一致！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Sign1 = phonenum.getText().length() == 11;
            Sign2 = password1.getText().length() >= 6 && password1.getText().length() <= 10;
            Sign3 = vcode.getText().length() > 0;
            Sign4 = password1.getText().toString().equals(password2.getText().toString());

            if (Sign1 & Sign2 & Sign3 & Sign4) {
                canSubmit = true;
                submit.setBackgroundResource(R.drawable.login_btn);
            }else {
                canSubmit = false;
                submit.setBackgroundResource(R.drawable.register_btn2);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void verTask(){
        getcode.setBackgroundResource(R.drawable.register_btn2);
        canSend = false;
        new AsyncTask<String,Integer,String>() {
            @Override
            protected String doInBackground(String... params) {
                try{
                    JSONObject obj = new JSONObject();
                    obj.put("phonenum",params[0]);
                    return HttpConnRequest.request(UrlConstant.verificationCode, "POST", obj);
                }catch (Exception e){e.printStackTrace();}
                return null;
            }

            @Override
            protected void onPostExecute(String result){
                getcode.setBackgroundResource(R.drawable.login_btn);
                canSend = true;
                if (result!=null){
                    if (getJsonCode(result) == 1){
                        Toast.makeText(ResetPasswordActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }.execute(phonenum.getText().toString());
    }

    private void resetTask(){
        canSubmit = false;
        submit.setBackgroundResource(R.drawable.register_btn2);
        new AsyncTask<String,Integer,String>() {
            @Override
            protected String doInBackground(String... params) {
                try{
                    JSONObject obj = new JSONObject();
                    obj.put("phonenum", params[0]);
                    obj.put("newpwd", MD5.getMD5(params[1]));
                    obj.put("vcode", params[2]);
                    return HttpConnRequest.request(UrlConstant.resetPassword, "POST", obj);
                }catch (Exception e){e.printStackTrace();}
                return null;
            }

            @Override
            protected void onPostExecute(String result){
                canSubmit = true;
                submit.setBackgroundResource(R.drawable.login_btn);
                if (result!=null){
                    if (getJsonCode(result) == 1){
                        Toast.makeText(ResetPasswordActivity.this, "修改密码成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }.execute(phonenum.getText().toString(), password1.getText().toString(), vcode.getText().toString());
    }

    private int getJsonCode(String jsonResult){
        try{
            JSONObject Obj = new JSONObject(jsonResult);
            if (Obj.has("code") && Obj.getInt("code") == 1){
                return 1;
            }
        }catch (Exception e){e.printStackTrace();}
        return 0;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ResetPasswordActivity.class));
    }
}