package com.example.k.superbag2.bean;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by K on 2016/6/26.
 */
public class ItemBean extends DataSupport{

    private String tag1 = "",tag2 = "",tag3 = "";
    private String content;
    private String dayTime;
    private String weather;
    private String feelings;
    private int importance;

    private String pic1 = "",pic2 = "",pic3 = "",pic4 = "";
    private List<String> picList;

    public ItemBean(String tag1, String tag2, String tag3, String content,
                    int importance, String weather,
                    String feelings){
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.content = content;
        this.importance = importance;
        this.weather = weather;
        this.feelings = feelings;
    }

    public ItemBean(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<String> getPicList(){
        picList = new ArrayList<>(Arrays.asList(pic1,pic2,pic3,pic4));
        return picList;
    }

    public void setPicList(List<String> list){
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

    public int getDay() {
        return Integer.parseInt(dayTime.substring(8,10));
    }

    public int getMonth() {
        return Integer.parseInt(dayTime.substring(5,7));
    }

    public int getYear() {
        return Integer.parseInt(dayTime.substring(0,4));
    }

    public String getHourMIn(){
        return dayTime.substring(11);
    }
}
