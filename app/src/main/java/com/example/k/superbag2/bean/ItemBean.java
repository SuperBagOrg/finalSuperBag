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
public class ItemBean extends DataSupport {

    private String tag1 = "",tag2 = "",tag3 = "";
    private String content;
    private String dayTime;
    private String weather;
    private String feelings;
    private int importance;
    //新增，为了能再次编辑，所以要保存位置
    private int pic1Index = -1,pic2Index = -1,pic3Index = -1,pic4Index = -1;
    private List<Integer> picIndex;
    private long updateTime;

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    private String pic1 = "",pic2 = "",pic3 = "",pic4 = "";
    private List<String> picList;

    public String getContent() {
        String[] con = content.split("/-/");
        String str = "";
        for (int i = 0; i < con.length; i++){
            str += con[i];
        }
        return str;
    }

    public List<EditData> getListData(){
        List<EditData> editDatas = new ArrayList<>();
        String[] con = content.split("/-/");

        for (int i = 0; i < 9; i++){
            EditData editData = new EditData();
            int temp = 0;
            if (i != getPic1Index() && i != getPic2Index() && i != getPic3Index()
                    && i != getPic4Index() && i < con.length){
                editData.setInputStr(con[i]);
            } else {
                editData.setImagePath(getPicList().get(temp));
                temp++;
            }
            editDatas.add(editData);
        }
        return editDatas;
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

    public List<Integer> getPicIndex() {
        picIndex = new ArrayList<>(Arrays.asList(pic1Index,pic2Index,pic3Index,pic4Index));
        return picIndex;
    }

    public void setPicIndex(List<Integer> picIndex) {
        this.picIndex = picIndex;
    }

    public int getPic1Index() {
        return pic1Index;
    }

    public void setPic1Index(int pic1Index) {
        this.pic1Index = pic1Index;
    }

    public int getPic2Index() {
        return pic2Index;
    }

    public void setPic2Index(int pic2Index) {
        this.pic2Index = pic2Index;
    }

    public int getPic3Index() {
        return pic3Index;
    }

    public void setPic3Index(int pic3Index) {
        this.pic3Index = pic3Index;
    }

    public int getPic4Index() {
        return pic4Index;
    }

    public void setPic4Index(int pic4Index) {
        this.pic4Index = pic4Index;
    }

    public String[] getCon() {
        return content.split("/-/");
    }

}
