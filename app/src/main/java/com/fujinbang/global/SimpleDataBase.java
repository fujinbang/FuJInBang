package com.fujinbang.global;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by VITO on 2016/5/18.
 * SharePreferences类
 */
public class SimpleDataBase {
    public static String admin = "15626152164";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SimpleDataBase(Context context) {
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putToken(String value) {
        editor.putString("token", value).commit();
    }

    public void putSex(boolean isMan) {
        editor.putBoolean("isMan", isMan).commit();
    }

    public void putArea(String area) {
        editor.putString("area", area).commit();
    }

    public void putUserName(String value) {
        editor.putString("userName", value).commit();
    }

    public void putEmail(String email) {
        editor.putString("email", email).commit();
    }

    public void putClientId(int value) {
        editor.putInt("clientId", value).commit();
    }

    public void putLoginDate(String value) {
        editor.putString("loginDate", value).commit();
    }

    public void putPhoneNum(String value) {
        editor.putString("phoneNum", value).commit();
    }

    public void putLocationX(double x) {
        editor.putString("x", String.valueOf(x)).commit();
    }

    public void clearAllMsg() {
        editor.clear().commit();
    }

    public void clearLocation() {
        editor.remove("x").commit();
    }

    public void putLocationY(double y) {
        editor.putString("y", String.valueOf(y)).commit();
    }

    public double getLocationX() {
        return Double.parseDouble(sharedPreferences.getString("x", "0.0"));
    }

    public void putIntegration(int score) {
        editor.putInt("integration", score).commit();
    }

    public int getIntegration() {
        return sharedPreferences.getInt("integration", 0);
    }

    public double getLocationY() {
        return Double.parseDouble(sharedPreferences.getString("y", "0.0"));
    }

    public String getArea() {
        return sharedPreferences.getString("area", "无");
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", "无");
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "无");
    }

    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    public int getClientId() {
        return sharedPreferences.getInt("clientId", 0);
    }

    public String getLoginDate() {
        return sharedPreferences.getString("loginDate", null);
    }

    public String getPhoneNum() {
        return sharedPreferences.getString("phoneNum", "无");
    }

    public boolean isMan() {
        return sharedPreferences.getBoolean("isMan", true);
    }

    public void putLastSignDate(String date) {
        editor.putString("signDate", date).commit();
    }

    public String getLastSignDate() {
        return sharedPreferences.getString("signDate", "");
    }

    public void putSignDays(int days) {
        editor.putInt("signDays", days).commit();
    }

    public int getSignDays() {
        return sharedPreferences.getInt("signDays", 0);
    }

    public void putRange(int range) {
        editor.putInt("range", range).commit();
    }

    public int getRange() {
        return sharedPreferences.getInt("range", 1000);
    }

    public void putAlert(boolean isAlert) {
        editor.putBoolean("alert", isAlert).commit();
    }

    public boolean isAlert() {
        return sharedPreferences.getBoolean("alert", true);
    }

    public void putVibrate(boolean isVibrate) {
        editor.putBoolean("vibrate", isVibrate).commit();
    }

    public boolean isVibrate() {
        return sharedPreferences.getBoolean("vibrate", false);
    }

    public void putPushId(String pushId) {
        editor.putString("pushId", pushId).commit();
    }

    public String getPushId() {
        return sharedPreferences.getString("pushId", null);
    }

    public void putPayPassword(String pwd) {
        editor.putString("payPwd", pwd);
        editor.commit();
    }

    public String getPayPassword() {
        return sharedPreferences.getString("payPwd", "");
    }

    public void putPickLastDate(long millsec) {
        editor.putLong("pickDate", millsec);
        editor.commit();
    }

    public long getPickLastDate() {
        return sharedPreferences.getLong("pickDate", 0);
    }

    public void putPublishLastDate(long millsec) {
        editor.putLong("publishDate", millsec);
        editor.commit();
    }

    public long getPublishLastDate() {
        return sharedPreferences.getLong("publishDate", 0);
    }

    public void putPickNum(int pickNum) {
        editor.putInt("pickNum", pickNum);
        editor.commit();
    }

    public int getPickNum() {
        return sharedPreferences.getInt("pickNum", 0);
    }

    public void putPublishNum(int publishNum) {
        editor.putInt("publishNum", publishNum);
        editor.commit();
    }

    public int getPublishNum() {
        return sharedPreferences.getInt("publishNum", 0);
    }
}
