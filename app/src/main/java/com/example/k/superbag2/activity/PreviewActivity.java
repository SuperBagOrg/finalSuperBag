package com.example.k.superbag2.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.EditData;
import com.example.k.superbag2.bean.ItemBean;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.GetImageUtils;
import com.example.k.superbag2.view.LookPicDialog;
import com.example.k.superbag2.view.RichTextEditor3;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

/**
 * Created by K on 2016/7/26.
 */
public class PreviewActivity extends BaseActivity implements View.OnClickListener{

    private Button preBack,preEdit;
    private LinearLayout preBackLL,preEditLL;
    private ImageView preHeadIcon;
    private TextView preMonth,preWeather,preFeelings,preTag1,preTag2,preTag3,preMin;
    private RichTextEditor3 preContent;

    private ItemBean item;
    public static int width = 0;
    private Long lineNum;
    private List<EditData> list_richedit;
    private List<ImageView> imageViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_preview);

        initView();
        setListener();
        initData();
        imageViewList = preContent.getImageViews();
        setPicClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            width = preContent.getWidth();
            Log.d("preview获取的宽度是:  ",width+"");
        }
    }

    private void initView(){
        preBack = (Button)findViewById(R.id.pre_back);
        preEdit = (Button)findViewById(R.id.pre_edit);
        preBackLL = (LinearLayout)findViewById(R.id.pre_back_ll);
        preEditLL = (LinearLayout)findViewById(R.id.pre_edit_ll);
        preHeadIcon = (ImageView)findViewById(R.id.pre_head_icon);
        preMonth = (TextView)findViewById(R.id.pre_month);
        preWeather = (TextView)findViewById(R.id.pre_weather_TV);
        preFeelings = (TextView)findViewById(R.id.pre_feelings_TV);
        preTag1 = (TextView)findViewById(R.id.pre_tag1);
        preTag2 = (TextView)findViewById(R.id.pre_tag2);
        preTag3 = (TextView)findViewById(R.id.pre_tag3);
        preContent = (RichTextEditor3)findViewById(R.id.pre_content);
        preMin = (TextView)findViewById(R.id.pre_min);

        //设置头像
        Uri headUri = GetImageUtils.getUri(this, Constant.HEAD_ICON_URI);
        if (!headUri.toString().equals("")){
            Glide.with(this)
                    .load(headUri)
                    .asBitmap()
                    .into(preHeadIcon);
        }
    }

    private void setListener(){
        preBack.setOnClickListener(this);
        preEdit.setOnClickListener(this);
        preBackLL.setOnClickListener(this);
        preEditLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pre_back:
            case R.id.pre_back_ll:
                finish();
                break;
            case R.id.pre_edit:
            case R.id.pre_edit_ll:
                Intent intent = new Intent(PreviewActivity.this,EditActivity.class);
                intent.putExtra(Constant.EDIT_DONE,lineNum);
                Log.d("传递给edit的linenum是：",lineNum+"");
                startActivity(intent);
                //跳转后关闭当前活动
                finish();
                break;
        }
    }

    private void showPic(int i){
        LookPicDialog dialog = new LookPicDialog(PreviewActivity.this,item,i);
        dialog.show();
        //设置dialog的宽度为屏幕宽度，否则则不能充满屏幕
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private void initData(){
        Intent intent = getIntent();
        //获取到点击条目的创建时间。
        lineNum = intent.getLongExtra(Constant.LINE_INDEX,-1);
        Log.d("preview获取的linenum是：--",lineNum+"");

        if (lineNum != -1){
            //根据创建时间从数据库拿到条目记录
            //拿到RichEdit的结构信息。
            list_richedit = DataSupport.where("updateTime = ?",""+lineNum).find(EditData.class);

            item = DataSupport.where("updateTime = ?",""+lineNum).find(ItemBean.class).get(0);
            if (item == null) {
                return;
            }
            for (EditData editData:list_richedit){
                if (!editData.getImagePath().equals("")){
                    //设置图片
                    Bitmap bp = preContent.getScaledBitmap(editData.getImagePath(),0);
                    preContent.addImageViewAtIndex(editData.getPosition(),bp,editData.getImagePath());
                } else if (!editData.getInputStr().trim().equals("")){
                    preContent.addTextViewAtIndex(editData.getPosition(),editData.getInputStr());
                }
            }

            //tag还原
            if (!item.getTag1().equals("")){
                preTag1.setVisibility(View.VISIBLE);
                preTag1.setText(item.getTag1());
            }
            if (!item.getTag2().equals("")){
                preTag2.setVisibility(View.VISIBLE);
                preTag2.setText(item.getTag2());
            }
            //时间还原
            preMonth.setText(item.getYear()+"-"+item.getMonth()+"-"+item.getDay());
            preFeelings.setText(item.getFeelings());
            preWeather.setText(item.getWeather());
            preMin.setText(item.getHourMIn());

        }
    }

    private void setPicClick(){
        for (int i = 0; i < imageViewList.size();i++){
            ImageView imageView = imageViewList.get(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPic(1);
                }
            });
        }
    }
}
