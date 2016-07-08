package com.fujinbang.mission;

import android.content.Context;
import android.util.Log;

import com.fujinbang.global.DiskLruCache;
import com.fujinbang.global.SimpleDataBase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class MissionModel {
    private Context context;
    private SimpleDataBase db;

    public MissionModel(Context context) {
        this.context = context;
        db = new SimpleDataBase(context);
    }

    /**
     * 增加一次接单
     */
    public void addPickMission() {
        Date date = new Date();
        Calendar now = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strTime = format.format(date);

        long lastDate = db.getPickLastDate();
        Calendar lastCalendar = Calendar.getInstance();
        lastCalendar.setTimeInMillis(lastDate);

        long time = date.getTime();
        db.putPickLastDate(time);

        if (isSameDay(now, lastCalendar)) {//同一天接单
            int pickNum;
            if ((pickNum = db.getPickNum()) >= 10)
                addMission("完成接单次数10次", strTime);

            db.putPickNum(pickNum + 1);
        } else {

            addMission("首次接单", strTime);
            db.putPickNum(1);
        }
    }

    /**
     * 增加一次发布
     */
    public void addPublishMission() {
        Date date = new Date();
        Calendar now = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strTime = format.format(date);

        long lastDate = db.getPublishLastDate();
        Calendar lastCalendar = Calendar.getInstance();
        lastCalendar.setTimeInMillis(lastDate);

        long time = date.getTime();
        db.putPublishLastDate(time);

        if (isSameDay(now, lastCalendar)) {//同一天接单
            int publishNum;
            if ((publishNum = db.getPublishNum()) >= 10)
                addMission("完成求助次数10次", strTime);

            db.putPublishNum(publishNum + 1);
        } else {

            addMission("首次求助", strTime);
            db.putPublishNum(1);
        }
    }

    public static boolean isSameDay(Calendar day1, Calendar day2) {
        return day1.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR)
                && day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR);
    }

    private void addMission(String desc, String time) {
        try {
            DiskLruCache cache = DiskLruCache.open(DiskLruCache.getDiskCacheDir(context, "mission"),
                    DiskLruCache.getAppVersion(context), 1, 10 * 1024 * 1024);
            JSONObject mission = new JSONObject();
            mission.put("desc", desc);
            mission.put("date", time);
            JSONArray array = getMission();
            array.put(mission);

            /*存入本地*/
            DiskLruCache.Editor editor = cache.edit("mission");
            Log.i("zy", "save mission:" + array.toString());
            editor.set(0, array.toString());
            editor.commit();
            cache.flush();
            cache.close();
        } catch (IOException e) {
            Log.e("zy", "diskLruCache error:" + e.toString());
        } catch (JSONException e) {
            Log.e("zy", "diskLruCache jsonobject error:" + e.toString());
        }
    }

    private JSONArray getMission() {
        try {
            DiskLruCache cache = DiskLruCache.open(DiskLruCache.getDiskCacheDir(context, "mission"),
                    DiskLruCache.getAppVersion(context), 1, 10 * 1024 * 1024);

            String missions = cache.edit("mission").getString(0);
            cache.close();
            if (missions == null) return new JSONArray();
            Log.i("zy", "mission record:" + missions);
            JSONArray array = new JSONArray(missions);
            return array;
        } catch (IOException e) {
            Log.e("zy", "diskLruCache error:" + e.toString());
        } catch (JSONException e) {
            Log.e("zy", "diskLruCache jsonobject error:" + e.toString());
        }
        return new JSONArray();
    }

    public List<Mission> getMissionList() {
        return JSONArray2List(getMission());
    }

    private List<Mission> JSONArray2List(JSONArray array) {
        List<Mission> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject missionObject = array.optJSONObject(i);
            if (missionObject != null) {
                String desc = missionObject.optString("desc");
                String date = missionObject.optString("date");
                Mission mission = new Mission();
                mission.setDesc(desc);
                mission.setDate(date);
                list.add(mission);
            }
        }
        return list;
    }

    public static boolean isToday(long millsec) {
        Calendar now = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millsec);

        return isSameDay(now, calendar);
    }
}
