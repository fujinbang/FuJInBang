package com.fujinbang.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.internet.HttpConnRequest;
import com.fujinbang.internet.MD5;
import com.fujinbang.internet.UrlConstant;
import com.fujinbang.internet.UserMsgUpload;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.igexin.sdk.PushManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by VITO on 2016/5/17.
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnTouchListener{
    protected TextView login_error, relogintxt;
    protected Button registerBtn, loginBtn;
    protected EditText number, password;
    protected ImageButton see_password;
    protected boolean can_see, canSubmit;

    SimpleDataBase simpleDataBase;
    int code;
    int id;
    String invitation;
    String phoneNum;
    String regDate;
    String email;
    String userName;
    String area;
    boolean isMan;
    int score;
    int status;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        simpleDataBase = new SimpleDataBase(this);

        can_see = false;
        canSubmit = true;
        number = (EditText) LoginActivity.this.findViewById(R.id.login_editText);
        password = (EditText) LoginActivity.this.findViewById(R.id.login_editText2);
        see_password = (ImageButton) LoginActivity.this.findViewById(R.id.login_see_password);
        login_error = (TextView) LoginActivity.this.findViewById(R.id.login_error);
        registerBtn = (Button) LoginActivity.this.findViewById(R.id.login_register_btn);
        loginBtn = (Button) LoginActivity.this.findViewById(R.id.login_btn);
        relogintxt = (TextView) LoginActivity.this.findViewById(R.id.login_forget_psw);

        number.addTextChangedListener(mTextWatcher);
        password.addTextChangedListener(mTextWatcher);
        see_password.setOnTouchListener(this);
        login_error.setVisibility(View.GONE);
        registerBtn.setOnTouchListener(this);
        loginBtn.setOnTouchListener(this);
        relogintxt.setOnTouchListener(this);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (number.getText().length() == 1) {
                password.setTextColor(0xff000000);
                see_password.setVisibility(View.VISIBLE);
            }
            if (password.getText().length() == 1) {
                password.setTextColor(0xff000000);
                login_error.setVisibility(View.GONE);
            }
        }
    };


    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_see_password:
                if (!can_see) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    can_see = true;
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    can_see = false;
                }
                Editable etable = password.getText();
                Selection.setSelection(etable, etable.length());
                break;
            case R.id.login_register_btn:
                RegisterActivity.startActivity(this);
                break;
            case R.id.login_btn:
                if (number.getText().length() == 11 && password.getText().length() >= 6 && password.getText().length() <= 10) {
                    if (canSubmit){
                        canSubmit = false;
                        new loginTask().execute(
                                number.getText().toString(),
                                password.getText().toString());
                    }
                } else if (number.getText().length() != 11) {
                    Toast.makeText(LoginActivity.this, "请输入有效的手机号码", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "请输入6~10位密码 ", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.login_forget_psw:
                ResetPasswordActivity.startActivity(LoginActivity.this);
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

    class loginTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("phonenum", params[0]);
                String md5password = MD5.getMD5(params[1]);
                obj.put("password", md5password);
                return HttpConnRequest.request(UrlConstant.login, "POST", obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {//在主线程中执行，方法参数为任务执行结果
            if (result == null) {
                canSubmit = true;
                Toast.makeText(LoginActivity.this, "连接服务器失败！", Toast.LENGTH_LONG).show();
            } else {
                getJsonData(result);
                if (code == 1) {
                    //登录成功
                    String cid = PushManager.getInstance().getClientid(LoginActivity.this);
                    simpleDataBase.putPushId(cid);
                    new UserMsgUpload(getApplicationContext()).uploadPushID(token, cid, new UserMsgUpload.OnUserUploadListener() {
                        @Override
                        public void OnSuccess(String desc) {
                            Log.e("cc", "上传个推id成功");
                        }

                        @Override
                        public void OnError(String desc) {

                        }
                    });
                    //登录环信
                    EMClient.getInstance().login(number.getText().toString(), MD5.getMD5(password.getText().toString()), new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                    Log.d("main", "登录聊天服务器成功！");
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                    String cur = df.format(curDate);
                                    simpleDataBase.putToken(token);
                                    simpleDataBase.putClientId(id);
                                    simpleDataBase.putLoginDate(cur);
                                    simpleDataBase.putArea(area);
                                    simpleDataBase.putSex(isMan);
                                    simpleDataBase.putEmail(email);
                                    simpleDataBase.putUserName(userName);
                                    simpleDataBase.putPhoneNum(phoneNum);
                                    simpleDataBase.putIntegration(score);
                                    LoginActivity.this.finish();
                                }
                            });
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }

                        @Override
                        public void onError(int code, String message) {
                            Log.d("main", "登录聊天服务器失败！");
                            canSubmit = true;
                            Toast.makeText(LoginActivity.this, "登录失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (code == 403) {
                    //密码错误
                    canSubmit = true;
                    login_error.setVisibility(View.VISIBLE);
                } else if (code == 404) {
                    //用户不存在
                    canSubmit = true;
                    password.setHint("用户不存在，请先注册");
                    password.setTextColor(0xffff4444);
                    password.setText("");
                    see_password.setVisibility(View.GONE);
                }
            }
        }
    }

    private void getJsonData(String jsonResult) {
        try {
            JSONObject Obj = new JSONObject(jsonResult);
            code = Obj.getInt("code");
            if (code == 1) {
                JSONObject data = Obj.getJSONObject("data");
                id = data.getInt("id");
                invitation = data.getString("invitation");
                phoneNum = data.getString("phoneNum");
                regDate = data.getString("regDate");
                score = data.getInt("score");
                status = data.getInt("status");
                token = Obj.getString("token");
                isMan = data.getString("gender").equals("male")?true:false;
                userName = data.getString("nickname");
                email = data.getString("email");
                area = data.getString("city");
            }
        } catch (Exception e) {
            Log.e("zy", "LoginActivity parseJson:" + e.toString());
        }
    }
}
