package com.example.k.superbag2.bean;

import android.view.ViewGroup;

/**
 * Created by Aersasi on 2016/8/20.
 */
public class EditBean {
    Object id;
    String type;
    int width;
    int height;
    ViewGroup.MarginLayoutParams cParams;
    String line;
    String imageId;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ViewGroup.MarginLayoutParams getcParams() {
        return cParams;
    }

    public void setcParams(ViewGroup.MarginLayoutParams cParams) {
        this.cParams = cParams;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
