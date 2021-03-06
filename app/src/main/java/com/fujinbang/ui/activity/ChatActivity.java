package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.fujinbang.R;
import com.fujinbang.global.IMController;
import com.fujinbang.global.MissionDetail;
import com.fujinbang.global.StatusBarCompat;
import com.fujinbang.internet.HttpConnRequest;
import com.fujinbang.internet.UrlConstant;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by VITO on 2016/5/13.
 * 聊天界面
 */
public class ChatActivity extends FragmentActivity {
    private int groupPosition;
    protected ArrayList<String> attendersPhoneNum = new ArrayList<>();
    protected ArrayList<Integer> attendersId = new ArrayList<>();
    protected String announcerId;
    protected String announcerPhoneNum = "";
    EaseChatFragment chatFragment = new EaseChatFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        StatusBarCompat.compat(this);
        Intent it = this.getIntent();
        this.groupPosition = it.getExtras().getInt("groupPosition");
        announcerId = MissionDetail.getInstance().getMission(groupPosition).get("neederid").toString();

        initAllUser();

        chatFragment.setMyMissionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MissionDetailActivity.startActivity(ChatActivity.this, groupPosition, announcerPhoneNum, attendersPhoneNum, attendersId, announcerId);
            }
        });
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
        args.putString(EaseConstant.EXTRA_USER_ID, MissionDetail.getInstance().getMission(groupPosition).get("chatgroupid").toString());
        int memberCount = 1;
        if (MissionDetail.getInstance().getAttendersInfo().get(MissionDetail.getInstance().getMission(groupPosition).get("helpid").toString()) != null){
            memberCount = MissionDetail.getInstance().getAttendersInfo().get(MissionDetail.getInstance().getMission(groupPosition).get("helpid").toString()).size()+1;
        }
        args.putInt("memberCount", memberCount);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.chat_contain, chatFragment).commit();
    }


    protected void initAllUser(){
        new SearchUserTask().
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        MissionDetail.getInstance().getMission(groupPosition).get("neederid").toString());
        new SearchAttenderTask().
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        MissionDetail.getInstance().getMission(groupPosition).get("helpid").toString());
    }

    class SearchAttenderTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            return HttpConnRequest.request(UrlConstant.queryAttenders + params[0], "GET", null);
        }
        @Override
        protected void onPostExecute(String result){
            if (result!=null){
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.has("attenders")){
                        attendersPhoneNum.clear();
                        attendersId.clear();
                        JSONArray attenders = object.getJSONArray("attenders");
                        for (int i = 0;i<attenders.length();i++){
                            JSONObject attender = attenders.getJSONObject(i);
                            if (attender.getInt("status") != 3 && attender.getInt("status") != 5){
                                EaseUser user = new EaseUser(attender.getString("phoneNum"));
                                String avatar = "http://o73gf55zi.bkt.clouddn.com/" + attender.getInt("id") + ".png";
                                user.setAvatar(avatar);
                                if (attender.has("nickname") && !attender.getString("nickname").equals("")){
                                    user.setNick(attender.getString("nickname"));
                                } else {
                                    user.setNick("");
                                }
                                IMController.userMap.put(user.getUsername(), user);
                                attendersPhoneNum.add(attender.getString("phoneNum"));
                                attendersId.add(attender.getInt("id"));
                            }
                        }
                        chatFragment.refresh();
                    }
                } catch (Exception e){e.printStackTrace();}
            }
        }
    }

    class SearchUserTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                JSONObject obj = new JSONObject();
                obj.put("id", Integer.parseInt(params[0]));
                result = HttpConnRequest.request(UrlConstant.queryUserInfo, "POST", obj);
            } catch (Exception e){e.printStackTrace();}
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            if (result!=null){
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.has("code")){
                        if (object.getInt("code") == 1){
                            JSONObject data = object.getJSONObject("data");
                            EaseUser user = new EaseUser(data.getString("phoneNum"));
                            user.setNick(data.getString("nickName"));
                            String avatar = "http://o73gf55zi.bkt.clouddn.com/" + data.getInt("id") + ".png";
                            user.setAvatar(avatar);
                            IMController.userMap.put(user.getUsername(), user);
                            announcerPhoneNum = data.getString("phoneNum");
                            chatFragment.refresh();
                        }
                    }
                } catch (Exception e){e.printStackTrace();}
            }
        }
    }

    public static void startActivity(Context context, int groupPosition) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("groupPosition", groupPosition);
        context.startActivity(intent);
    }
}