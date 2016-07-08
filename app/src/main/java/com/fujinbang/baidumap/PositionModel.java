package com.fujinbang.baidumap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/5/5.
 * 对本地存储读写 上一次在地图上显示的位置
 */
public class PositionModel {
    private final static String fileName = "localPos";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public PositionModel(Context context) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * -1表示不存在X
     */
    public double getPositionX() {
        String x = sp.getString("x", "-1");
        return Double.parseDouble(x);
    }

    /**
     * -1表示不存在Y
     */
    public double getPositionY() {
        String y = sp.getString("y", "-1");
        return Double.parseDouble(y);
    }

    public void setPositionX(double x) {
        editor.putString("x", String.valueOf(x));
        editor.commit();
    }

    public void setPositionY(double y) {
        editor.putString("y", String.valueOf(y));
        editor.commit();
    }
}