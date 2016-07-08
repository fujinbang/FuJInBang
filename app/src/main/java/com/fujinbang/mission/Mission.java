package com.fujinbang.mission;

/**
 * Created by Administrator on 2016/5/18.
 */
public class Mission {
    private String desc;
    private int intergration;
    private boolean isComplete;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIntergration() {
        return intergration;
    }

    public void setIntergration(int intergration) {
        this.intergration = intergration;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    private String date;

    public Mission(String desc, int intergration, boolean isComplete) {
        this.desc = desc;
        this.intergration = intergration;
        this.isComplete = isComplete;
    }

    public Mission() {

    }
}
