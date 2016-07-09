package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.fujinbang.R;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.global.StatusBarCompat;
import com.fujinbang.ui.fragment.SystemMsgFragment;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

public class ChatRobotActivity extends FragmentActivity {
    EaseChatFragment chatFragment = new EaseChatFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_robot);
        StatusBarCompat.compat(this);
        SimpleDataBase simpleDataBase = new SimpleDataBase(this);
        String clientPhoneNum = simpleDataBase.getPhoneNum();

        if (!clientPhoneNum.equals(SimpleDataBase.admin)){
            Bundle args = new Bundle();
            args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
            args.putString(EaseConstant.EXTRA_USER_ID, SimpleDataBase.admin);
            args.putInt("memberCount", 0);
            args.putString("clientPhoneNum", clientPhoneNum);
            chatFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.chat_robot_contain, chatFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.chat_robot_contain, new SystemMsgFragment()).commit();
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChatRobotActivity.class);
        context.startActivity(intent);
    }
}
