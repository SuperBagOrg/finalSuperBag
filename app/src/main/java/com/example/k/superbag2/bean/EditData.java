package com.example.k.superbag2.bean;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

public class EditData extends DataSupport{
    private String inputStr = "";
    private String imagePath = "";
    private long updateTime;

    //表示存储索引
    private int position;

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getInputStr(){
        return inputStr;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setInputStr(String inputStr){
        this.inputStr = inputStr;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}