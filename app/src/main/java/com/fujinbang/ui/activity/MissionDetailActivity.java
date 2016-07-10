package com.fujinbang.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fujinbang.R;
import com.fujinbang.global.IMController;
import com.fujinbang.global.MissionDetail;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.global.TimeCalculator;
import com.fujinbang.internet.HttpConnRequest;
import com.fujinbang.internet.UrlConstant;
import com.fujinbang.seekhelp.MediaManager;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.CircleTransform;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by VITO on 2016/5/18.
 * 任务详情界面
 */
public class MissionDetailActivity extends BaseActivity implements View.OnClickListener{
    protected static String dir = Environment.getExternalStorageDirectory()+"/fujinbang_vido";
    protected static String pushFinish = "您发布的任务已有参与者确认完成，请确认";
    protected static String pushDrop = "已有用户放弃您的任务，请确认";

    protected ImageView mission_detail_back,announcer_img;
    protected TextView announcer_name,txt_desc,start_time,end_time,rest_time,urge,status,bonus,add_bonus,item1_txt,item2_txt;
    protected GridLayout attender_list;
    protected RelativeLayout item1,item2,item3,item4;

    protected int mMinWidth;
    protected int mMaxWidth;
    protected View animView;
    protected FrameLayout play_sound;
    protected TextView record_time;
    private boolean isAnnouncer;
    private String myToken;
    private String myid;

    HashMap<String, Object> mission;
    String announcer;
    String announcerId;
    List<String> attenders = new ArrayList<>();
    List<Integer> attendersId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);
        Intent it = this.getIntent();
        announcer = it.getExtras().getString("announcer");
        announcerId = it.getExtras().getString("announcerId");
        attenders = it.getExtras().getStringArrayList("attenders");
        attendersId = it.getExtras().getIntegerArrayList("attendersId");
        mission = MissionDetail.getInstance().getMission(it.getExtras().getInt("groupPosition"));
        isAnnouncer = mission.containsKey("isAnnouncer");
        SimpleDataBase simpleDataBase = new SimpleDataBase(this);
        myToken = simpleDataBase.getToken();
        myid = String.valueOf(simpleDataBase.getClientId());
        initView();
    }

    private void initView() {
        mission_detail_back = (ImageView) findViewById(R.id.mission_detail_back);
        announcer_img = (ImageView) findViewById(R.id.mission_detail_announcer_img);
        announcer_name = (TextView) findViewById(R.id.mission_detail_announcer_name);
        attender_list = (GridLayout)findViewById(R.id.mission_detail_attender_list);
        txt_desc = (TextView) findViewById(R.id.mission_detail_txt_desc);
        start_time = (TextView) findViewById(R.id.mission_detail_starttime);
        end_time = (TextView) findViewById(R.id.mission_detail_endtime);
        rest_time = (TextView) findViewById(R.id.mission_detail_rest_time);
        urge = (TextView) findViewById(R.id.mission_detail_urge);
        status = (TextView) findViewById(R.id.mission_detail_status);
        bonus = (TextView) findViewById(R.id.mission_detail_bonus);
        add_bonus = (TextView) findViewById(R.id.mission_detail_add_bonus);
        item1 = (RelativeLayout) findViewById(R.id.mission_detail_item1);
        item2 = (RelativeLayout) findViewById(R.id.mission_detail_item2);
        item3 = (RelativeLayout) findViewById(R.id.mission_detail_item3);
        item4 = (RelativeLayout) findViewById(R.id.mission_detail_item4);
        item1_txt = (TextView) findViewById(R.id.mission_detail_item1_txt);
        item2_txt = (TextView) findViewById(R.id.mission_detail_item2_txt);
        record_time = (TextView) findViewById(R.id.mission_detail_recoder_time);
        play_sound = (FrameLayout) findViewById(R.id.mission_detail_recoder_lenght);
        animView = findViewById(R.id.mission_detail_recoder_anim);

        mission_detail_back.setOnClickListener(this);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
        item4.setOnClickListener(this);
        txt_desc.setText(mission.get("desc").toString());
        start_time.setText(mission.get("start_time").toString());
        end_time.setText(mission.get("end_time").toString());
        bonus.setText(mission.get("bonus").toString());

        if (isAnnouncer){
            item1_txt.setText("确认完成任务");
            item2_txt.setText("取消任务");
            urge.setVisibility(View.VISIBLE);
            add_bonus.setVisibility(View.VISIBLE);
            urge.setOnClickListener(this);
            add_bonus.setOnClickListener(this);
        } else {
            item1_txt.setText("完成任务");
            item2_txt.setText("放弃任务");
            urge.setVisibility(View.GONE);
            add_bonus.setVisibility(View.GONE);
        }

        //加载昵称与头像
        initNickAndAvatar();

        if (mission.containsKey("voicelength")){
            WindowManager wm = (WindowManager) MissionDetailActivity.this.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            mMaxWidth = (int) (outMetrics.widthPixels * 0.4f);
            mMinWidth = (int) (outMetrics.widthPixels * 0.15f);
            ViewGroup.LayoutParams lp = play_sound.getLayoutParams();
            lp.width = (int) (mMinWidth + (mMaxWidth / 60f) * Integer.parseInt(mission.get("voicelength").toString()));
            animView.setBackgroundResource(R.drawable.adj);
            txt_desc.setVisibility(View.GONE);
            play_sound.setVisibility(View.VISIBLE);
            record_time.setVisibility(View.VISIBLE);
            record_time.setText(mission.get("voicelength") + "\"");
            play_sound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //播放帧动画
                    animView.setBackgroundResource(R.drawable.play_anim);
                    AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
                    animation.start();
                    //播放录音
                    MediaManager.playSound(MissionDetailActivity.this, new File(dir, mission.get("helpid").toString()+".aac").getAbsolutePath(), new MediaPlayer.OnCompletionListener() {

                        public void onCompletion(MediaPlayer mp) {
                            //停止播放帧动画
                            animView.setBackgroundResource(R.drawable.adj);
                            MediaManager.release();
                        }
                    });
                }
            });
        }

    }

    private void initAvatar(String avatar, ImageView imageView){
        Glide.with(this)
                .load(avatar)
                .placeholder(R.drawable.integration)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform(this))
                .into(imageView);
    }

    private void addViewToGridLayout(String nick, String avatar){
        View view = View.inflate(MissionDetailActivity.this, R.layout.item_information, null);
        TextView txt = (TextView) view.findViewById(R.id.item_information_name);
        ImageView img = (ImageView) view.findViewById(R.id.item_information_img);
        txt.setText(nick);
        initAvatar(avatar, img);
        attender_list.addView(view);
    }

    private void initNickAndAvatar() {
        EaseUser easeUser = IMController.getInstance().getUserInfo(announcer);
        announcer_name.setText(easeUser.getNick());
        initAvatar(easeUser.getAvatar(), announcer_img);
        for (int i = 0;i < attenders.size();i++){
            EaseUser easeAttender = IMController.getInstance().getUserInfo(attenders.get(i));
            addViewToGridLayout(easeAttender.getNick(), easeAttender.getAvatar());
        }
    }

    public static void startActivity(Context context, int groupPosition, String announcer, ArrayList<String> attenders, ArrayList<Integer> attendersId, String announcerId) {
        Intent intent = new Intent(context, MissionDetailActivity.class);
        intent.putExtra("groupPosition", groupPosition);
        intent.putExtra("announcer", announcer);
        intent.putExtra("announcerId", announcerId);
        intent.putStringArrayListExtra("attenders", attenders);
        intent.putIntegerArrayListExtra("attendersId", attendersId);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mission_detail_back:
                finish();
                break;
            case R.id.mission_detail_urge:
                //催促接单者
                for (int i = 1;i<attendersId.size();i++){
                   urgeAttender(attendersId.get(i).toString());
                }
                break;
            case R.id.mission_detail_add_bonus:
                //追加奖励
                showCustomDia();
                break;
            case R.id.mission_detail_item1:
                if (isAnnouncer){
                    MissionDetail.missionFinished(myToken, mission.get("helpid").toString(), myid);
                } else {
                    pushMessage(pushFinish, "1", announcerId);
                }
                break;
            case R.id.mission_detail_item2:
                //放弃任务
                if (isAnnouncer){
                    pushMessage(pushDrop, "0", null);
                } else {
                    pushMessage(pushDrop, "0", announcerId);
                }
                break;
            case R.id.mission_detail_item3:
                //联系客服
                FeedbackPhonecallActivity.startActivity(MissionDetailActivity.this);
                break;
            case R.id.mission_detail_item4:
                //举报
                FeedbackOnlineActivity.startActivity(MissionDetailActivity.this, true);
                break;
            default:
                break;
        }
    }

    AlertDialog dialog;
    private void showCustomDia()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MissionDetailActivity.this, R.style.AudioDialog);
        View viewDia= LayoutInflater.from(MissionDetailActivity.this).inflate(R.layout.dialog_add_bonus, null);
        builder.setView(viewDia);
        final EditText bonus = (EditText) viewDia.findViewById(R.id.dialog_bonus);
        Button submit = (Button) viewDia.findViewById(R.id.dialog_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBonus(bonus.getText().toString());
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
        builder.create();
        dialog = builder.show();
    }

    private void addBonus(String bonus) {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try{
                    JSONObject obj = new JSONObject();
                    obj.put("token",params[0]);
                    obj.put("target",Integer.parseInt(params[1]));
                    obj.put("bonus",Integer.parseInt(params[2]));
                    result = HttpConnRequest.request(UrlConstant.transferBonus,"POST",obj);
                }catch (Exception e){e.printStackTrace();}
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                if (result!=null){
                    try{
                        JSONObject obj = new JSONObject(result);
                        if (obj.has("code") && obj.getInt("code") == 1){
                            Toast.makeText(MissionDetailActivity.this,"追加奖励成功！",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MissionDetailActivity.this,"追加奖励失败！",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myToken, mission.get("helpid").toString(), bonus);
    }

    protected void pushMessage(String message, String type, String userid){
        Toast.makeText(MissionDetailActivity.this,"请稍候...",Toast.LENGTH_SHORT).show();
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try{
                    JSONObject obj = new JSONObject();
                    obj.put("token",params[0]);
                    obj.put("push_message", params[1]);
                    obj.put("message", params[2]+","+mission.get("helpid").toString()+","+params[4]);
                    if (params[3] == null){
                        obj.put("helpinfoid",Integer.parseInt(mission.get("helpid").toString()));
                    } else {
                        obj.put("targetuserid", Integer.parseInt(params[3]));
                    }
                    result = HttpConnRequest.request(UrlConstant.transMission,"POST",obj);
                }catch (Exception e){e.printStackTrace();}
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                if (result!=null){
                    try{
                        JSONObject obj = new JSONObject(result);
                        if (obj.getInt("code") == 1){
                            Toast.makeText(MissionDetailActivity.this,"操作成功，等待对方确认",Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(MissionDetailActivity.this, MainActivity.class);
                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(it);
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        }.execute(myToken, message, type, userid, myid);
    }

    int urgeCount = 0;
    protected void urgeAttender(String targetid){
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try{
                    JSONObject obj = new JSONObject();
                    obj.put("token",params[0]);
                    obj.put("helpid",Integer.parseInt(params[1]));
                    obj.put("targetuser",Integer.parseInt(params[2]));
                    result = HttpConnRequest.request(UrlConstant.urgeAttender,"POST",obj);
                }catch (Exception e){e.printStackTrace();}
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                if (result!=null){
                    try{
                        JSONObject obj = new JSONObject(result);
                        if (obj.has("code") && obj.getInt("code") == 1){
                            urgeCount++;
                            Toast.makeText(MissionDetailActivity.this,"已催促"+ urgeCount +"位接单者",Toast.LENGTH_SHORT).show();
                            //if (urgeCount == attenderId.size()){
                            //    urgeCount = 0;
                            //}
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myToken, mission.get("helpid").toString(), targetid);
    }

    DecimalFormat df = new DecimalFormat("00");
    protected boolean isFinished;
    protected Handler missionDetailTimeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1) {
                Log.e("ccc","handle还在跑");
                long second = TimeCalculator.getRestTime(mission.get("end_time").toString());
                rest_time.setText(second / 3600 + ":" + df.format(second % 3600 / 60) + ":" + df.format(second % 3600 % 60));
                if (second / 3600 < 24 && second > 0 && rest_time.getCurrentTextColor()!=0xffff0000){
                    rest_time.setTextColor(0xffff0000);
                }
                if (second == 0){
                    isFinished = true;
                }
                if (!isFinished){
                    missionDetailTimeHandler.sendEmptyMessageDelayed(1,1000);
                } else {
                    status.setText("已结束");
                }
            }
        }
    };

    @Override
    public void onPause(){
        super.onPause();
        isFinished = true;
    }

    @Override
    public void onResume(){
        super.onResume();
        isFinished = false;
        missionDetailTimeHandler.sendEmptyMessageDelayed(1, 1000);
    }
}
