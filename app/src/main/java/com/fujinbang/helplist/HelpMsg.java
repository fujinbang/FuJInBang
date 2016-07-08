package com.fujinbang.helplist;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/5/7.
 */
public class HelpMsg {
    private int id;
    private String chatGroupId;
    private String userName = "";
    private int integration;
    private double x;
    private double y;
    private String description = "";
    private int neederId;
    private String startTime = "";
    private int needPeopleNum;
    private String endTime = "";
    private int pos;
    private Bitmap avatar = null;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(String chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNeederId() {
        return neederId;
    }

    public void setNeederId(int neederId) {
        this.neederId = neederId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getNeedPeopleNum() {
        return needPeopleNum;
    }

    public void setNeedPeopleNum(int needPeopleNum) {
        this.needPeopleNum = needPeopleNum;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "id:" + id + " neederId:" + neederId + " integration:" + integration + " description:" + description
                + " startTime:" + startTime + " endTime:" + endTime
                + " needPeopleNum" + needPeopleNum + " location:(" + x + "," + y + ")";
    }
}
