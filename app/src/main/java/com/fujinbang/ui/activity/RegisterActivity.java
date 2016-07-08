package com.fujinbang.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.global.StatusBarCompat;
import com.fujinbang.internet.HttpConnRequest;
import com.fujinbang.internet.MD5;
import com.fujinbang.internet.UrlConstant;

import org.json.JSONObject;

/**
 * Created by VITO on 2016/3/4.
 * 注册界面
 */
public class RegisterActivity extends Activity implements View.OnTouchListener{
    protected TextView agreement,getCodeTxt;
    protected EditText number,password,invite_code,mail,certificate_code;
    protected ImageButton canseepassword;
    protected ImageView isCurCode;
    protected Button submitBtn;
    protected RelativeLayout getCode;
    protected CheckBox checkbox;
    protected boolean can_see,Sign1 = false,Sign2 = false,Sign3 = false;

    private int code;
    private boolean canSubmit;
    private boolean canSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarCompat.compat(this);
        can_see = false;
        canSubmit = false;
        canSend = true;


        number = (EditText)findViewById(R.id.register_editText);
        password = (EditText)findViewById(R.id.register_editText2);
        canseepassword = (ImageButton)findViewById(R.id.register_see_password);
        isCurCode = (ImageView)findViewById(R.id.register_isCurCode);
        invite_code = (EditText)findViewById(R.id.register_editText3);
        mail = (EditText)findViewById(R.id.register_editText4);
        certificate_code = (EditText)findViewById(R.id.register_editText5);
        getCode = (RelativeLayout)findViewById(R.id.register_getCode);
        getCodeTxt = (TextView)findViewById(R.id.register_getCode_txt);
        checkbox = (CheckBox)findViewById(R.id.register_checkbox);
        agreement = (TextView)findViewById(R.id.register_agreement);
        submitBtn = (Button)findViewById(R.id.register_submitBtn);

        number.addTextChangedListener(mTextWatcher);
        password.addTextChangedListener(mTextWatcher);
        certificate_code.addTextChangedListener(mTextWatcher);
        invite_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (invite_code.getText().length() == 6) {
                    new inviteTask().execute(invite_code.getText().toString());
                }
            }
        });
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    canSubmit = false;
                    submitBtn.setBackgroundResource(R.drawable.register_btn2);
                } else if (Sign1 & Sign2 & Sign3) {
                    canSubmit = true;
                    submitBtn.setBackgroundResource(R.drawable.login_btn);
                    submitBtn.setTextColor(0xffffffff);
                }
            }
        });
        checkbox.setChecked(true);
        canseepassword.setOnTouchListener(this);
        getCode.setOnTouchListener(this);
        agreement.setOnTouchListener(this);
        submitBtn.setOnTouchListener(this);

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Sign1 = number.getText().length() == 11;
            Sign2 = password.getText().length() >= 6 && password.getText().length() <= 10;
            Sign3 = certificate_code.getText().length() > 0;

            if (Sign1 & Sign2 & Sign3 & checkbox.isChecked()) {
                canSubmit = true;
                submitBtn.setBackgroundResource(R.drawable.login_btn);
                submitBtn.setTextColor(0xffffffff);
            }else {
                canSubmit = false;
                submitBtn.setBackgroundResource(R.drawable.register_btn2);
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

    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_see_password:
                if (!can_see){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    can_see = true;
                }else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    can_see = false;
                }
                Editable etable = password.getText();
                Selection.setSelection(etable, etable.length());
                break;
            case R.id.register_getCode:
                //获取验证码
                if (number.getText().length() == 11 && canSend){
                    getCode.setBackgroundResource(R.drawable.register_btn2);
                    getCodeTxt.setTextColor(0xff000000);
                    canSend = false;
                    new verTask().execute(number.getText().toString());
                } else {
                    Toast.makeText(RegisterActivity.this,"请输入有效的手机号码",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_agreement:
                //跳转协议界面
                RegisterProtocolActivity.startActivity(RegisterActivity.this);
                break;
            case R.id.register_submitBtn:
                //提交操作
                if (canSubmit){
                    new regTask().execute(
                            number.getText().toString(),
                            password.getText().toString(),
                            mail.getText().toString(),
                            invite_code.getText().toString(),
                            certificate_code.getText().toString());
                } else if (!Sign1 & !Sign2 & !Sign3){
                    Toast.makeText(RegisterActivity.this, "请输入有效信息", Toast.LENGTH_SHORT).show();
                } else if (!Sign1){
                    Toast.makeText(RegisterActivity.this, "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                } else if (!Sign2){
                    Toast.makeText(RegisterActivity.this, "请设置6~10位有效密码", Toast.LENGTH_SHORT).show();
                } else if (!Sign3){
                    Toast.makeText(RegisterActivity.this, "请输入有效的验证码", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                onClick(v);
                break;
            default:
                break;
        }
        return true;
    }

    class regTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String...params){
            try{
                JSONObject obj = new JSONObject();
                obj.put("phonenum",params[0]);
                String md5password = MD5.getMD5(params[1]);
                obj.put("password",md5password);
                if (!params[2].isEmpty())obj.put("email",params[2]);
                if (!params[3].isEmpty())obj.put("invitation",params[3]);
                int vcode = Integer.parseInt(params[4]);
                obj.put("vcode",vcode);
                return HttpConnRequest.request(UrlConstant.register, "POST", obj);
            }catch (Exception e){e.printStackTrace();}
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            if(result!=null){
                if (getJsonCode(result) == 1){
                    //注册成功
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    //注册失败
                    Toast.makeText(RegisterActivity.this, "注册失败，用户已存在！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    class inviteTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                JSONObject obj = new JSONObject();
                obj.put("code",params[2]);
                return HttpConnRequest.request(UrlConstant.invitationCode,"POST",obj);
            }catch (Exception e){e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            if (result!=null){
                if (getJsonCode(result) == 1){
                    //邀请码正确
                    isCurCode.setImageResource(R.drawable.a23);
                    invite_code.setTextColor(0xff000000);
                }else {
                    //提示邀请码不存在
                    isCurCode.setImageResource(R.drawable.a22);
                    invite_code.setHint("亲，该邀请码不存在哦");
                    invite_code.setTextColor(0xffff0000);
                    invite_code.setText("");
                }
            }
        }
    }

    class verTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                JSONObject obj = new JSONObject();
                obj.put("phonenum",params[0]);
                return HttpConnRequest.request(UrlConstant.verificationCode,"POST",obj);
            }catch (Exception e){e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            getCode.setBackgroundResource(R.drawable.login_btn);
            getCodeTxt.setTextColor(0xffffffff);
            canSend = true;
            if (result!=null){
                if (getJsonCode(result) == 1){
                    Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private int getJsonCode(String jsonResult){
        try{
            JSONObject Obj = new JSONObject(jsonResult);
            code = Obj.getInt("code");
        }catch (Exception e){e.printStackTrace();}
        return code;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
