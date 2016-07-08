package com.fujinbang.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.fujinbang.R;
import com.fujinbang.global.AudioController;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.internet.HttpConnRequest;
import com.fujinbang.internet.UrlConstant;
import com.fujinbang.baidumap.LocationManager;
import com.fujinbang.mission.MissionModel;
import com.fujinbang.seekhelp.DialogManager;
import com.fujinbang.seekhelp.MediaManager;
import com.fujinbang.ui.activity.MainActivity;
import com.fujinbang.ui.view.PayDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * Created by VITO on 2016/3/19.
 * 求助界面
 */
public class SeekHelpFragment extends Fragment implements OnDateSetListener, TimePickerDialog.OnTimeSetListener, PoiListDialogFragment.OnClickPoiListener {
    protected LinearLayout timepicker1, timepicker2;
    protected TextView textCount, timepicker1_date, timepicker1_time, timepicker2_date, timepicker2_time, textbonus;
    protected TextView textPoi;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    protected Spinner sexspinner;
    protected Button addnum, minusnum, helpbtn;
    protected ImageButton microphone;
    protected EditText reqDes, numofpeople;
    protected ImageView helpActivity;

    protected DialogManager mDialogManager;
    protected View animView;
    protected FrameLayout play_sound;
    protected TextView record_time;
    private int mMinWidth; //最小的item宽度
    private int mMaxWidth; //最大的item宽度
    float sec = 0;
    String fpath;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected boolean isVoiceReq = false;
    protected boolean canSubmit = false;
    protected boolean locationPrepare = false;

    String token = null;
    String starttime = null;
    String endtime = null;
    protected double locationX;
    protected double locationY;
    int helpid;

    protected Context mContext;

    /* 在OnResume定位后获取poi列表 */
    protected List<Poi> mPoiList;

    protected int targetId = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seekhelp, container, false);
        mContext = getContext();
        SimpleDataBase simpleDataBase = new SimpleDataBase(mContext);
        token = simpleDataBase.getToken();
        if (getArguments() != null) {
            targetId = getArguments().getInt("targetId");
        }

        //获取屏幕的宽度
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxWidth = (int) (outMetrics.widthPixels * 0.4f);
        mMinWidth = (int) (outMetrics.widthPixels * 0.15f);

        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                false);

        timePickerDialog = TimePickerDialog.newInstance(this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false, false);//是否是24小时制，是否抖动。false，false


        helpActivity = (ImageView) view.findViewById(R.id.seekhelp_help);
        textCount = (TextView) view.findViewById(R.id.seekhelp_textcount);
        reqDes = (EditText) view.findViewById(R.id.seekhelp_reqdes);
        record_time = (TextView) view.findViewById(R.id.id_recoder_time);
        microphone = (ImageButton) view.findViewById(R.id.seekhelp_microphone);
        play_sound = (FrameLayout) view.findViewById(R.id.id_recoder_lenght);
        animView = view.findViewById(R.id.id_recoder_anim);
        timepicker1 = (LinearLayout) view.findViewById(R.id.seekhelp_timepicker1);
        timepicker2 = (LinearLayout) view.findViewById(R.id.seekhelp_timepicker2);
        timepicker1_date = (TextView) view.findViewById(R.id.seekhelp_timepicker1_date);
        timepicker1_time = (TextView) view.findViewById(R.id.seekhelp_timepicker1_time);
        timepicker2_date = (TextView) view.findViewById(R.id.seekhelp_timepicker2_date);
        timepicker2_time = (TextView) view.findViewById(R.id.seekhelp_timepicker2_time);
        textbonus = (TextView) view.findViewById(R.id.seekhelp_textbonus);
        addnum = (Button) view.findViewById(R.id.seekhelp_addnum);
        minusnum = (Button) view.findViewById(R.id.seekhelp_minusnum);
        numofpeople = (EditText) view.findViewById(R.id.seekhelp_numofpeople);
        sexspinner = (Spinner) view.findViewById(R.id.seekhelp_sexspinner);
        textPoi = (TextView) view.findViewById(R.id.seekhelp_poi);
        helpbtn = (Button) view.findViewById(R.id.seekhelp_helpbtn);

        helpActivity.setOnClickListener(mListener);
        reqDes.addTextChangedListener(mTextWatcher);
        microphone.setOnClickListener(mListener);
        play_sound.setOnClickListener(mListener);
        timepicker1.setOnClickListener(mListener);
        timepicker2.setOnClickListener(mListener);
        textbonus.addTextChangedListener(mTextWatcher);
        addnum.setOnClickListener(mListener);
        minusnum.setOnClickListener(mListener);
        textPoi.setOnClickListener(mListener);
        helpbtn.setOnClickListener(mListener);

        ArrayAdapter<CharSequence> adaptersex = new ArrayAdapter<CharSequence>(mContext, R.layout.spinner_item, new String[]{"无所谓", "男", "女"});
        adaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexspinner.setAdapter(adaptersex);
        sexspinner.setSelection(0);

        if (targetId != -1) {
            LinearLayout ll_num = (LinearLayout) view.findViewById(R.id.seekhelp_ll_needpeoplenum);
            LinearLayout ll_sex = (LinearLayout) view.findViewById(R.id.seekhelp_ll_sex);
            ImageView back = (ImageView) view.findViewById(R.id.seekhelp_back);
            ll_num.setVisibility(View.GONE);
            ll_sex.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(mListener);
        }

        return view;
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isVoiceReq) {
                textCount.setText(reqDes.getText().length() + "/100");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (timepicker1_date.getText().length() > 0
                    & timepicker2_date.getText().length() > 0
                    & textbonus.getText().length() > 0) {
                if ((!isVoiceReq & reqDes.getText().length() > 0) || isVoiceReq) {
                    canSubmit = true;
                    helpbtn.setBackgroundResource(R.drawable.login_btn);
                }
            } else {
                canSubmit = false;
                helpbtn.setBackgroundResource(R.drawable.register_btn2);
            }
        }
    };

    protected OnClickListener mListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.seekhelp_back:
                    getActivity().finish();
                    break;
                case R.id.seekhelp_help:
                    //跳转帮助界面
                    break;
                case R.id.seekhelp_microphone:
                    // 显示對話框在开始录音以后
                    isVoiceReq = true;
                    reqDes.setVisibility(View.GONE);
                    textCount.setVisibility(View.GONE);
                    mDialogManager = new DialogManager(mContext);
                    mDialogManager.setAudioFinishRecorderListener(new DialogManager.AudioFinishRecorderListener() {

                        public void onFinish(float seconds, String filePath) {
                            if (seconds < 1) {
                                Toast.makeText(mContext, "录音时间过短！", Toast.LENGTH_SHORT).show();
                            } else {
                                sec = seconds;
                                fpath = filePath;
                                record_time.setText(Math.round(seconds) + "\"");//显示语音时间
                                ViewGroup.LayoutParams lp = play_sound.getLayoutParams();
                                lp.width = (int) (mMinWidth + (mMaxWidth / 60f) * seconds);//对话框长度
                                play_sound.setVisibility(View.VISIBLE);
                                record_time.setVisibility(View.VISIBLE);
                                if (timepicker1_date.getText().length() > 0
                                        & timepicker2_date.getText().length() > 0
                                        & textbonus.getText().length() > 0) {
                                    canSubmit = true;
                                    helpbtn.setBackgroundResource(R.drawable.login_btn);
                                }
                            }
                        }
                    });
                    mDialogManager.showRecordingDialog();
                    break;
                case R.id.id_recoder_lenght:
                    //播放帧动画
                    animView.setBackgroundResource(R.drawable.play_anim);
                    AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
                    animation.start();
                    //播放录音
                    MediaManager.playSound(mContext, fpath, new MediaPlayer.OnCompletionListener() {

                        public void onCompletion(MediaPlayer mp) {
                            //停止播放帧动画
                            animView.setBackgroundResource(R.drawable.adj);
                            MediaManager.release();
                        }
                    });
                    break;
                case R.id.seekhelp_addnum:
                    int num1 = Integer.parseInt(numofpeople.getText().toString());
                    numofpeople.setText(String.valueOf(num1 + 1));
                    break;
                case R.id.seekhelp_minusnum:
                    int num2 = Integer.parseInt(numofpeople.getText().toString());
                    if (num2 > 1) {
                        num2 -= 1;
                        numofpeople.setText(String.valueOf(num2));
                    }
                    break;
                case R.id.seekhelp_timepicker1:
                case R.id.seekhelp_timepicker2:
                    datePickerDialog.setVibrate(false);//是否抖动
                    datePickerDialog.setYearRange(1985, 2028);//设置年份区间
                    datePickerDialog.setCloseOnSingleTapDay(false);//选择后是否消失
                    datePickerDialog.show(getFragmentManager(), v.getId() == R.id.seekhelp_timepicker1 ? "time1" : "time2");//展示dialog，传一个tag参数
                    break;
                case R.id.seekhelp_helpbtn:
                    if (canSubmit) {
                        if (locationPrepare) {
                            if (!textbonus.getText().toString().equals("0") && new SimpleDataBase(getContext()).getPayPassword() != "") {//需要输入支付密码
                                new PayDialog().setOnFinishListener(new PayDialog.OnPasswordFinish() {
                                    @Override
                                    public void onFinish(boolean passwordCorrectOrNull, String psd) {
                                        if (passwordCorrectOrNull) {//如果输入密码正确
                                            helpbtn.setText("正在发布中...");
                                            canSubmit = false;
                                            helpbtn.setBackgroundResource(R.drawable.register_btn2);
                                            new helpTask().execute(
                                                    token,
                                                    textbonus.getText().toString(),
                                                    numofpeople.getText().toString(),
                                                    reqDes.getText().toString(),
                                                    String.valueOf(Math.round(sec)));
                                        } else {
                                            Toast.makeText(getContext(), "密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).show(getChildFragmentManager(), "payDialog");
                            } else {//没有密码 或者 积分为0
                                helpbtn.setText("正在发布中...");
                                canSubmit = false;
                                helpbtn.setBackgroundResource(R.drawable.register_btn2);
                                new helpTask().execute(
                                        token,
                                        textbonus.getText().toString(),
                                        numofpeople.getText().toString(),
                                        reqDes.getText().toString(),
                                        String.valueOf(Math.round(sec)));
                            }
                        } else {
                            Toast.makeText(mContext, "正在定位中，请稍候", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "请填完所有要求哦", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.seekhelp_poi:
                    if (mPoiList != null && mPoiList.size() != 0) {/*如果定位成功后 则打开一个选择poi的列表*/
                        PoiListDialogFragment.newInstance(mPoiList).show(getChildFragmentManager(), "poiListDialog");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if (datePickerDialog.getTag().equals("time1")) {
            timepicker1_date.setText((month + 1) + "-" + day);
            starttime = year + "-" + (month + 1) + "-" + day;
        } else {
            timepicker2_date.setText((month + 1) + "-" + day);
            endtime = year + "-" + (month + 1) + "-" + day;
        }
        //启动时间选择器
        timePickerDialog.setVibrate(false);//是否抖动
        timePickerDialog.setCloseOnSingleTapMinute(false);//选择后是否消失
        timePickerDialog.show(getFragmentManager(), datePickerDialog.getTag());//展示dialog，传一个tag参数
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        if (timePickerDialog.getTag().equals("time1")) {
            timepicker1_time.setText(hourOfDay + ":" + minute);
            starttime += " " + hourOfDay + ":" + minute + ":00";
            try {
                starttime = format.format(format.parse(starttime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            timepicker2_time.setText(hourOfDay + ":" + minute);
            endtime += " " + hourOfDay + ":" + minute + ":00";
            try {
                endtime = format.format(format.parse(endtime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 在点击界面蓝色的poi定位的时候 弹出一个PoiListDialogFragment用作选择poi
     * 选择后回调本函数
     *
     * @param poi 用户选中的poi名称
     */
    @Override
    public void onClick(String poi) {
        textPoi.setText(poi);
    }

    class helpTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            String result = null;
            try {
                JSONObject obj = new JSONObject();
                obj.put("token", params[0]);
                obj.put("bonus", params[1]);
                obj.put("starttime", starttime);
                obj.put("endtime", endtime);
                String location = "{'x':" + locationX + ",'y':" + locationY + "}";
                obj.put("location", location);
                obj.put("needpeoplenum", params[2]);
                if (isVoiceReq) {
                    obj.put("desc", "");
                    obj.put("voicelength", params[4]);
                } else {
                    obj.put("desc", params[3]);
                }
                if (targetId != -1) {
                    obj.put("targetid", targetId);
                }
                result = HttpConnRequest.request(UrlConstant.help, "POST", obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT).show();
                textbonus.setText("");
                helpbtn.setText("发布");
            } else {
                if (getJsonData(result) == 1) {
                    //开始上传音频
                    if (isVoiceReq) {
                        uploadHelpAudio();
                    } else {
                        Toast.makeText(mContext, "发布成功！", Toast.LENGTH_LONG).show();

                        new MissionModel(mContext).addPublishMission();
                        //跳转至消息界面
                        if (targetId == -1) {
                            ((MainActivity) getActivity()).updateMission();
                            ((MainActivity) getActivity()).changTab(2);
                        } else {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "发布失败！积分不足", Toast.LENGTH_LONG).show();
                    textbonus.setText("");
                    helpbtn.setText("发布");
                }
            }
        }
    }

    private int getJsonData(String jsonResult) {
        int code = 0;
        try {
            JSONObject Obj = new JSONObject(jsonResult);
            if (Obj.has("code")) {
                code = Obj.getInt("code");
                if (code == 1) {
                    JSONObject data = Obj.getJSONObject("data");
                    helpid = data.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 上传音频
     */
    private void uploadHelpAudio() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try {
                    JSONObject object = new JSONObject();
                    object.put("filename", String.valueOf(helpid) + ".aac");
                    result = HttpConnRequest.request(UrlConstant.token, "POST", object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.has("token")) {
                            String uploadToken = object.getString("token");
                            AudioController.getInstance().uploadAudio(helpid, uploadToken, fpath, new AudioController.UploadListener() {
                                @Override
                                public void OnUploadSucceed() {
                                    Toast.makeText(mContext, "发布成功！", Toast.LENGTH_LONG).show();
                                    //跳转至消息界面
                                    if (targetId == -1) {
                                        ((MainActivity) getActivity()).updateMission();
                                        ((MainActivity) getActivity()).changTab(2);
                                    } else {
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void OnUploadError() {
                                    Toast.makeText(mContext, "上传音频失败！", Toast.LENGTH_LONG).show();
                                    textbonus.setText("");
                                    helpbtn.setText("发布");
                                }

                                @Override
                                public void OnUploadProgress(double percent) {
                                    //进度条
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    /**
     * 获得当前定位的标志性地名
     */
    private void getPoi() {
        final LocationManager manager = LocationManager.getInstance(getContext());
        manager.addOnLocationListener(new LocationManager.OnLocationListener() {
            @Override
            public void onRecieveLocation(BDLocation location) {
                locationX = location.getLatitude();
                locationY = location.getLongitude();
                locationPrepare = true;
                mPoiList = location.getPoiList();
                if (mPoiList.size() != 0) {
                    textPoi.setText(mPoiList.get(0).getName());
                    manager.removeOnLocationListener(this);
                }
            }
        });
        manager.requestRightNow();
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaManager.pause();
        MediaManager.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        textbonus.setText("");
        getPoi();
    }

}