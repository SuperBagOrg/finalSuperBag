package com.example.k.superbag2.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by K on 2016/7/28.
 */
public class MemoItem extends DataSupport{
    private String title = "";
    private String content = "";
    private String date = "";
    private int importance;
    private boolean isAlarm = false;
    private boolean isSound = false;
    private boolean isShake = false;
    //表示数字，用于提醒
    private String alarmTime = "";
    private String editTime = "";
    private long updateTime;
    //用于显示
    private String alarmTimeShow = "";

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public boolean isShake() {
        return isShake;
    }

    public void setShake(boolean shake) {
        isShake = shake;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public int getDay() {
        return Integer.parseInt(editTime.substring(8,10));
    }

    public int getMonth() {
        return Integer.parseInt(editTime.substring(5,7));
    }

    public int getYear() {
        return Integer.parseInt(editTime.substring(0,4));
    }

    public String getHourMIn(){
        return editTime.substring(11);
    }

    public String getAlarmTimeShow() {
        return alarmTimeShow;
    }

    public void setAlarmTimeShow(String alarmTimeShow) {
        this.alarmTimeShow = alarmTimeShow;
    }
}
