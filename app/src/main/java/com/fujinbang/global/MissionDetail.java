package com.fujinbang.global;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.internet.HttpConnRequest;
import com.fujinbang.internet.UrlConstant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by VITO on 2016/5/19.
 * ConversationFragment加载任务相关数据
 */
public class MissionDetail {
    private static MissionDetail missionDetail;
    private List<HashMap<String,Object>> mission = new ArrayList<>();
    private List<HashMap<String,Object>> invitedMission = new ArrayList<>();
    private HashMap<String,String> localAudio = new HashMap<>();
    private HashMap<String,HashMap<String,Integer>> attendersInfo = new HashMap<>();
    private List<EMConversation> groupChat = new ArrayList<>();

    private MissionDetail() {
    }

    public static MissionDetail getInstance() {
        if (null == missionDetail) {
            synchronized (MissionDetail.class) {
                if (null == missionDetail) {
                    missionDetail = new MissionDetail();
                }
            }
        }
        return missionDetail;
    }

    public void initialize(String phoneNum, final OnMissionListener missionListener)  {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("phonenum", params[0]);
                    return HttpConnRequest.request(UrlConstant.queryHelp, "POST", object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    mission.clear();
                    invitedMission.clear();
                    groupChat.clear();
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray attend = object.getJSONArray("attend");
                        for (int i = 0;i<attend.length();i++){
                            JSONObject attendMisson = attend.getJSONObject(i);
                            if(attendMisson.has("status") && attendMisson.getInt("status") == 0){
                                continue;
                            }
                            if (attendMisson.has("user_status") && attendMisson.getInt("user_status") == 5){
                                continue;
                            }
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("bonus", attendMisson.getInt("bonus"));
                            hashMap.put("chatgroupid", attendMisson.getString("chatgroupid"));
                            hashMap.put("start_time",attendMisson.getString("start_time"));
                            hashMap.put("end_time",attendMisson.getString("end_time"));
                            hashMap.put("neederid",attendMisson.getInt("neederid"));
                            hashMap.put("desc",attendMisson.getString("desc"));
                            hashMap.put("helpid",attendMisson.getInt("id"));
                            JSONObject location = new JSONObject(attendMisson.getString("location"));
                            hashMap.put("x",location.getDouble("x"));
                            hashMap.put("y", location.getDouble("y"));
                            if (attendMisson.has("voicelength")){
                                hashMap.put("voicelength", attendMisson.get("voicelength").toString());
                            }
                            if (attendMisson.has("user_status")){
                                hashMap.put("user_status", attendMisson.get("user_status").toString());
                            }
                            groupChat.add(EMClient.getInstance().chatManager().getConversation(
                                    attendMisson.getString("chatgroupid"),
                                    EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_GROUP),
                                    true));
                            mission.add(hashMap);
                        }
                        JSONArray post = object.getJSONArray("post");
                        for (int i = 0;i<post.length();i++){
                            JSONObject postMisson = post.getJSONObject(i);
                            if(postMisson.has("status") && postMisson.getInt("status") == 0){
                                continue;
                            }
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("bonus",postMisson.getInt("bonus"));
                            hashMap.put("chatgroupid",postMisson.getString("chatgroupid"));
                            hashMap.put("start_time",postMisson.getString("start_time"));
                            hashMap.put("end_time",postMisson.getString("end_time"));
                            hashMap.put("neederid",postMisson.getInt("neederid"));
                            hashMap.put("isAnnouncer",true);
                            hashMap.put("desc", postMisson.getString("desc"));
                            hashMap.put("helpid",postMisson.getInt("id"));
                            JSONObject location = new JSONObject(postMisson.getString("location"));
                            hashMap.put("x",location.getDouble("x"));
                            hashMap.put("y", location.getDouble("y"));
                            if (postMisson.has("voicelength")){
                                hashMap.put("voicelength", postMisson.get("voicelength").toString());
                            }
                            groupChat.add(EMClient.getInstance().chatManager().getConversation(
                                    postMisson.getString("chatgroupid"),
                                    EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_GROUP),
                                    true));
                            mission.add(hashMap);
                        }
                        missionListener.onMissionSucceed(mission);
                        JSONArray invited = object.getJSONArray("invited");
                        for (int i = 0;i<invited.length();i++){
                            JSONObject invitedmisson = invited.getJSONObject(i);
                            if(invitedmisson.has("status") && invitedmisson.getInt("status") == 0){
                                continue;
                            }
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("bonus",invitedmisson.getInt("bonus"));
                            hashMap.put("chatgroupid",invitedmisson.getString("chatgroupid"));
                            hashMap.put("start_time",invitedmisson.getString("start_time"));
                            hashMap.put("end_time",invitedmisson.getString("end_time"));
                            hashMap.put("neederid",invitedmisson.getInt("neederid"));
                            hashMap.put("needPeopleNum",invitedmisson.getInt("needPeopleNum"));
                            hashMap.put("desc", invitedmisson.getString("desc"));
                            hashMap.put("helpid",invitedmisson.getInt("id"));
                            hashMap.put("regDate", invitedmisson.getString("regDate"));
                            JSONObject location = new JSONObject(invitedmisson.getString("location"));
                            hashMap.put("x",location.getDouble("x"));
                            hashMap.put("y",location.getDouble("y"));
                            if (invitedmisson.has("voicelength")){
                                hashMap.put("voicelength", invitedmisson.get("voicelength").toString());
                            }
                            invitedMission.add(hashMap);
                        }
                        missionListener.onInvitedSucceed(invitedMission);
                    } catch (Exception e) {
                        e.printStackTrace();
                        missionListener.onError("获取数据失败！");
                    }
                } else {
                    missionListener.onError("连接服务器失败！");
                }
            }
        }.execute(phoneNum);
    }

    public interface OnMissionListener {
        void onMissionSucceed(List<HashMap<String,Object>> mission);
        void onInvitedSucceed(List<HashMap<String,Object>> invitedMission);
        void onError(String errorMsg);
    }

    public HashMap<String, Object> getMission(int position) {return mission.get(position);}

    public HashMap<String,Object> getInvitedMission(int position){return invitedMission.get(position);}

    public List<HashMap<String,Object>> getMissionList(){return mission;}

    public List<HashMap<String,Object>> getInvitedMissionList(){return invitedMission;}

    public HashMap<String,String> getLocalAudio(){return localAudio;}

    public HashMap<String,HashMap<String,Integer>> getAttendersInfo(){return attendersInfo;}

    public List<EMConversation> getGroupChat(){return groupChat;}

    public void initGroupAvatar(){
        for (int i = 0;i<mission.size();i++){
            final int finalI = i;
            new AsyncTask<String, Integer, String>() {
                @Override
                protected String doInBackground(String... params) {
                    return HttpConnRequest.request(UrlConstant.queryAttenders + params[0], "GET", null);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (result != null) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.has("attenders")) {
                                JSONArray attenders = object.getJSONArray("attenders");
                                HashMap<String,Integer> hashMap = new HashMap<>();
                                for (int i = 0; i < attenders.length(); i++) {
                                    JSONObject attender = attenders.getJSONObject(i);
                                    if (attender.getInt("status") != 3 && attender.getInt("status") != 5){
                                        //0为参加任务，1为完成任务，2为发布者确认其完成任务，3为受邀，4为催单，5为放弃任务
                                        hashMap.put(attender.getString("phoneNum"), attender.getInt("id"));
                                    }
                                }
                                attendersInfo.put(mission.get(finalI).get("helpid").toString(), hashMap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mission.get(i).get("helpid").toString());
        }
    }

    public void initAudio(final String helpid, final String sec){
        File file = new File(Environment.getExternalStorageDirectory()+"/fujinbang_vido",helpid+".acc");
        if (file.exists()){
            if (!localAudio.containsKey(helpid)) {
                localAudio.put(helpid, sec);
            }
            return;
        }
        new AsyncTask<String, Integer, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                return HttpConnRequest.downloadAudio(params[0]);
            }
            @Override
            protected void onPostExecute(Integer result) {
                if (result != 0) {
                    if (!localAudio.containsKey(helpid)) {
                        localAudio.put(helpid, sec);
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,helpid);
    }

    public void getInvitedNeederNickName(final OnLoadNeederNickName listener){
        for (int i = 0;i<invitedMission.size();i++){
            if (invitedMission.get(i).containsKey("nickName")) {
                continue;
            }
            final int finalI = i;
            new AsyncTask<String, Integer, String>() {
                @Override
                protected String doInBackground(String... params) {
                    String result = null;
                    try {
                        JSONObject object = new JSONObject();
                        object.put("id", Integer.parseInt(params[0]));
                        result = HttpConnRequest.request(UrlConstant.queryUserInfo,"POST",object);
                    }catch (Exception e){e.printStackTrace();}
                    return result;
                }
                @Override
                protected void onPostExecute(String result) {
                    if (result != null) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.has("code") && object.getInt("code") == 1){
                                JSONObject data = object.getJSONObject("data");
                                invitedMission.get(finalI).put("nickName", data.getString("nickName"));
                            }
                            if (finalI == invitedMission.size() - 1){
                                listener.onSucceed();
                            }
                        } catch (Exception e){e.printStackTrace();}
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,invitedMission.get(i).get("neederid").toString());
        }
    }

    public interface OnLoadNeederNickName{
        void onSucceed();
    }

    public static AlertDialog dialog;
    public void showConfirmDialog(Context context, String content, final String url, final String jsonString){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AudioDialog);
        View viewDia= LayoutInflater.from(context).inflate(R.layout.dialog_mission_confirm, null);
        TextView msg = (TextView)viewDia.findViewById(R.id.dialog_mission_confirm_msg);
        Button yes = (Button)viewDia.findViewById(R.id.dialog_mission_confirm_yes);
        Button no = (Button)viewDia.findViewById(R.id.dialog_mission_confirm_no);
        msg.setText(content);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //missionOperation(url, jsonString);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
        builder.setView(viewDia);
        builder.create();
        dialog = builder.show();
    }

    public static void missionFinished(String token, String helpinfoid, String userid){
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try{
                    JSONObject obj = new JSONObject();
                    obj.put("token", params[0]);
                    obj.put("helpinfoid", Integer.parseInt(params[1]));
                    obj.put("finisherid", Integer.parseInt(params[2]));
                    result = HttpConnRequest.request(UrlConstant.finishMission, "POST", obj);
                }catch (Exception e){e.printStackTrace();}
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                if (result!=null){
                    try{
                        JSONObject obj = new JSONObject(result);
                        if (obj.getInt("code") == 1){
                            Log.e("MissionDetail", "已确认任务");
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        }.execute(token, helpinfoid, userid);
    }

    public static void missionDrop(String token, String helpinfoid, String userid){
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try{
                    JSONObject obj = new JSONObject();
                    obj.put("token", params[0]);
                    obj.put("helpinfoid", Integer.parseInt(params[1]));
                    obj.put("dropuserid", Integer.parseInt(params[2]));
                    result = HttpConnRequest.request(UrlConstant.dropMission, "POST", obj);
                }catch (Exception e){e.printStackTrace();}
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                if (result!=null){
                    try{
                        JSONObject obj = new JSONObject(result);
                        if (obj.getInt("code") == 1){
                            Log.e("MissionDetail", "已确认放弃任务");
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        }.execute(token, helpinfoid, userid);
    }

}
