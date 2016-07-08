package com.fujinbang.global;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.fujinbang.baidumap.LocationManager;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.igexin.sdk.PushManager;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2016/5/6.
 */
public class NeibourApplication extends Application {

    private static NeibourApplication instance;

    @Override
    public void onCreate() {
        Log.i("zy", "on app create");
        super.onCreate();
        instance = this;

        SDKInitializer.initialize(getApplicationContext());//初始化百度SDK账号 必须在使用地图前注册
        ShareSDK.initSDK(this);//初始化MobSDK账号 必须在邀请好友功能前注册
        PushManager.getInstance().initialize(this.getApplicationContext());//个推
        IMController.getInstance().init(this.getApplicationContext());//初始化环信

        LocationManager.getInstance(this).start();
    }

    @Override
    public void onTrimMemory(int level) {
        //Log.i("zy", "onTrimMemory" + level);
        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        Log.i("zy", "on app terminate");
        LocationManager.getInstance(this).destroy();
        //上方这行代码会结束ShareSDK的统计功能并释放资源。如果这行代码没有被调用，那么“应用启动次数”将会不准确，因为应用可能从来没有被关闭过
        ShareSDK.stopSDK(this);
        super.onTerminate();
    }

    /**
     * 退出整个程序
     */
    public void exit() {
        new SimpleDataBase(getApplicationContext()).clearAllMsg();

        LocationManager.getInstance(this).destroy();

        //上方这行代码会结束ShareSDK的统计功能并释放资源。如果这行代码没有被调用，那么“应用启动次数”将会不准确，因为应用可能从来没有被关闭过
        ShareSDK.stopSDK(this);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        System.exit(0);
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static NeibourApplication getInstance() {
        return instance;
    }
}