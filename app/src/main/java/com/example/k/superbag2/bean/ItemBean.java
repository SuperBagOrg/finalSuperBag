package com.example.k.superbag2.bean;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by K on 2016/6/26.
 */
public class ItemBean extends DataSupport{

    private String tag1,tag2,tag3;
    private String content;
    private List<Drawable> drawableList;
    private String dayTime;
    private String weather;
    private String feelings;
    private int importance;

    private String pic1,pic2,pic3,pic4;
    private List<Uri> picList;

    public ItemBean(String tag1, String tag2, String tag3, String content, boolean isMemo,
                    int importance, String oldTime, String newTime, List<Uri> picList,
                    String weather, String feelings,List<Drawable> drawableList){
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.content = content;
        this.importance = importance;
        this.picList = picList;
        this.weather = weather;
        this.feelings = feelings;
        this.drawableList = drawableList;
    }

    public ItemBean(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Drawable> getDrawableList() {
        return drawableList;
    }

    public void setDrawableList(List<Drawable> drawableList) {
        this.drawableList = drawableList;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getPicNum() {
        if (pic1.equals(""))
            return 0;
        else if (pic2.equals(""))
            return 1;
        else if (pic3.equals(""))
            return 2;
        else if (pic4.equals(""))
            return 3;
        return 4;
    }

    public List<Uri> getPicList(){
        return picList;
    }

    public void setPicList(List<Uri> list){
        picList = list;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getFeelings() {
        return feelings;
    }

    public void setFeelings(String feelings) {
        this.feelings = feelings;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getPic4() {
        return pic4;
    }

    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }
}
