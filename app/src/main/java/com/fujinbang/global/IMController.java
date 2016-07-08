package com.fujinbang.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.ui.activity.MainActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;

import java.util.List;

/**
 * Created by VITO on 2016/6/26.
 * 初始化环信
 */
public class IMController {

    private EaseUI easeUI;
    private Context appContext;
    protected EMMessageListener messageListener = null;

    private static IMController instance = null;
    public synchronized static IMController getInstance() {
        if (instance == null) {
            instance = new IMController();
        }
        return instance;
    }

    public void init(Context context) {
        appContext = context;
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);// 默认添加好友时，是不需要验证的，改成需要验证
        options.setMipushConfig("2882303761517486818", "5671748667818");
        if (EaseUI.getInstance().init(context, options)){
            easeUI = EaseUI.getInstance();

            setNotifier();
            registerEventListener();
        }
    }

    protected void setNotifier() {
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //修改标题,这里使用默认
                return "附近帮";
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //设置小图标，这里为默认
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                // String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                // if(message.getType() == EMMessage.Type.TXT){
                //     ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                // }
                // EaseUser user = getUserInfo(message.getFrom());
                // if(user != null){
                //     return getUserInfo(message.getFrom()).getNick() + ": " + ticker;
                //  }else{
                //       return message.getFrom() + ": " + ticker;
                //   }
                return "您收到群组信息，快点击查看吧！";
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
                // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                //设置点击通知栏跳转事件
                //Intent intent = new Intent(appContext, ChatActivity.class);
                //有电话时优先跳转到通话页面
                //if(isVideoCalling){
                //    intent = new Intent(appContext, VideoCallActivity.class);
                //}else if(isVoiceCalling){
                //    intent = new Intent(appContext, VoiceCallActivity.class);
                //}else{
                //    ChatType chatType = message.getChatType();
                //    if (chatType == ChatType.Chat) { // 单聊信息
                //        intent.putExtra("userId", message.getFrom());
                //        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                //    } else { // 群聊信息
                //        // message.getTo()为群聊id
                //        intent.putExtra("userId", message.getTo());
                //        if(chatType == ChatType.GroupChat){
                //            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                //       }else{
                //            intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                //        }

                //   }
                //}
                return new Intent(appContext, MainActivity.class);
            }
        });
    }

    protected void registerEventListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    //EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    //应用在后台，不需要刷新UI,通知栏提示新消息
                    if(!easeUI.hasForegroundActivies()){
                        easeUI.getNotifier().onNewMsg(message);
                    } else {

                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    //EMLog.d(TAG, "收到透传消息");
                    //获取消息body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action

                    //获取扩展属性 此处省略
                    //message.getStringAttribute("");
                    //EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action,message.toString()));
                    final String str = appContext.getString(R.string.receive_the_passthrough);

                    final String CMD_TOAST_BROADCAST = "hyphenate.demo.cmd.toast";
                    IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);

                    if(broadCastReceiver == null){
                        broadCastReceiver = new BroadcastReceiver(){

                            @Override
                            public void onReceive(Context context, Intent intent) {
                                // TODO Auto-generated method stub
                                Toast.makeText(appContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
                            }
                        };

                        //注册广播接收者
                        appContext.registerReceiver(broadCastReceiver,cmdFilter);
                    }

                    Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
                    broadcastIntent.putExtra("cmd_value", str+action);
                    appContext.sendBroadcast(broadcastIntent, null);
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }
}
