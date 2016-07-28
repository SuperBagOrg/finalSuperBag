package com.example.k.superbag2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.ItemBean;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.GetImageUtils;
import com.example.k.superbag2.utils.GetTime;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by K on 2016/6/26.
 */

public class EditActivity extends Activity implements
        View.OnClickListener,RadioGroup.OnCheckedChangeListener,
        CheckBox.OnCheckedChangeListener{

    private Button backBT,saveBT,picBT,faceBT,weatherBT,locationBT,editAlarm,feelingsBT;
    private EditText contentET;
    private RadioGroup radioGroup;
    private ImageView headIcon;
    private TextView oldTime;
    private LinearLayout backLL,saveLL,bottomLL;
    private RadioButton editDiary,editMemo;
    private PopupWindow popupWindow,alarmPOpup;
    private ImageView popupPic1,popupPic2,popupPic3,popupPic4;
    private ViewPager viewPager;
    private Button setTimeBT,doneBT,cancelBT,addTagBT;
    private TextView tag2TV,tag3TV;
    private CheckBox sunnyCK,cloudyCk,rainyCK,snowyCk,foggyCK,hazeCK;
    private AlertDialog weatherDialog;
    private CheckBox happyCK,sweetCK,unforgettableCK,calmCK,angryCk,aggrievedCK,sadCK,noFeelingsCK;
    private AlertDialog feelingsDialog;

    private boolean hasSaved = false;
    private Uri imageUri;
    private boolean isMemo;
    private String newTime = "-1";
    private boolean isEditable = true;
    private List<View> popupViewList;
    private boolean clickable = false;
    private String tag1="",tag2="",tag3="";
    private int flag = 1;
    private String weather = "晴",feelings = "开心";
    private int picNum = 0;

    private List<String> uriList;

    private List<Integer> weatherCKIdList = new ArrayList<>(Arrays.asList(R.id.sunny_checkbox,
            R.id.cloudy_checkbox,R.id.rainy_checkbox,
            R.id.snowy_checkbox,R.id.foggy_checkbox,R.id.haze_checkbox));
    private String[] weatherList = {"晴","阴","雨","雪","雾","霾"};
    private List<CheckBox> weatherCKList;
    private int weatherIndex = 0;

    private List<Integer> feelingsCKIdList = new ArrayList<>(Arrays.asList(R.id.happy_checkbox,
            R.id.sweet_checkbox,R.id.unforgettable_checkbox,
            R.id.calm_checkbox,R.id.angry_checkbox,R.id.aggrieved_checkbox,
            R.id.sad_checkbox,R.id.no_checkbox));
    private String[] feelingsList = {"开心","甜蜜","难忘","平静","生气","委屈","伤心","无"};
    private List<CheckBox> feelingsCKList;
    private int feelingsIndex = 0;
    private String oldtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //为了进入activity时，不自动弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_edit);

        initView();
        initListener();
        initData();
    }


    private void initView(){
        backBT = (Button)findViewById(R.id.edit_back);
        saveBT = (Button)findViewById(R.id.edit_save);
        picBT = (Button)findViewById(R.id.edit_pic_bt);
//        faceBT = (Button)findViewById(R.id.edit_face_bt);
        weatherBT = (Button)findViewById(R.id.edit_weather_bt);
//        locationBT = (Button)findViewById(R.id.edit_location_bt);
        contentET = (EditText)findViewById(R.id.edit_et);
        radioGroup = (RadioGroup)findViewById(R.id.edit_rg);
        editMemo = (RadioButton)findViewById(R.id.edit_memo);
        editDiary = (RadioButton)findViewById(R.id.edit_diary);
        headIcon = (ImageView)findViewById(R.id.edit_head_icon);
        oldTime = (TextView) findViewById(R.id.edit_time);
        backLL = (LinearLayout)findViewById(R.id.edit_back_ll);
        saveLL = (LinearLayout)findViewById(R.id.edit_save_ll);
//        bottomLL = (LinearLayout)findViewById(R.id.edit_bottom_LL);
        editAlarm = (Button)findViewById(R.id.edit_alarm);
        tag2TV = (TextView)findViewById(R.id.edit_tag2);
        tag3TV = (TextView)findViewById(R.id.edit_tag3);
        addTagBT = (Button)findViewById(R.id.add_tag_bt);
        feelingsBT = (Button)findViewById(R.id.edit_feelings_bt);

        tag2TV.setVisibility(View.GONE);
        tag3TV.setVisibility(View.GONE);
        //设置头像
        Bitmap head = GetImageUtils.getBMFromUri(this,Constant.HEAD_ICON_URI);
        if (head != null){
            headIcon.setImageBitmap(head);
        }

        View v = LayoutInflater.from(this).inflate(R.layout.popup_pic,null);
        popupPic1 = (ImageView)v.findViewById(R.id.popup_pic1);
        popupPic2 = (ImageView)v.findViewById(R.id.popup_pic2);
        popupPic3 = (ImageView)v.findViewById(R.id.popup_pic3);
        popupPic4 = (ImageView)v.findViewById(R.id.popup_pic4);
        popupViewList = new ArrayList<>();
        popupViewList.add(v);
    }

    private void initListener(){
        backBT.setOnClickListener(this);
        saveBT.setOnClickListener(this);
        picBT.setOnClickListener(this);
//        faceBT.setOnClickListener(this);
        weatherBT.setOnClickListener(this);
//        locationBT.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        backLL.setOnClickListener(this);
        saveLL.setOnClickListener(this);
        popupPic1.setOnClickListener(this);
        popupPic2.setOnClickListener(this);
        popupPic3.setOnClickListener(this);
        popupPic4.setOnClickListener(this);
        editAlarm.setOnClickListener(this);
        addTagBT.setOnClickListener(this);
        feelingsBT.setOnClickListener(this);
    }

    private void initData(){
        //表示已选取的图片数量
        picNum = 0;
        GetTime gt = new GetTime();
        oldtime = gt.getYear()+"-"+gt.getMonth()+"-"+gt.getDay();
        oldTime.setText(oldtime);

        uriList = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            uriList.add("");
        }

        //如果是从ListView点击进入活动，则初始化数据;
        //如何让editTExt无法点击编辑，还有问题
        Intent intent = getIntent();
        int lineNum = intent.getIntExtra(Constant.LINE_INDEX,-1);
        if (lineNum != -1){
            ItemBean item = DataSupport.find(ItemBean.class,lineNum);
            saveBT.setBackground(getResources().getDrawable(R.drawable.edit));
            contentET.setFocusable(false);

            contentET.setText(item.getContent());
            if(item.getIsMemo()){
                editMemo.setChecked(true);
                editDiary.setChecked(false);
            }
            if (!item.getNewTime().equals("-1")){
                editAlarm.setBackground(getResources().getDrawable(R.drawable.alarm_blue));
                clickable = true;
            }
            if (!item.getTag2().equals("")){
                tag2TV.setVisibility(View.VISIBLE);
                tag2TV.setText(item.getTag2());
            }
            if (!item.getTag3().equals("")){
                tag3TV.setVisibility(View.VISIBLE);
                tag3TV.setText(item.getTag3());
            }
            isEditable = false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_back_ll:
            case R.id.edit_back:
                if (hasSaved){
                    finish();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    builder.setMessage("尚未保存，确认退出？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    builder.create().dismiss();
                                }
                            });
                    builder.show();
                }
                break;
            case R.id.edit_save_ll:
            case R.id.edit_save:
                if (!isEditable){
                    saveBT.setBackground(getResources().getDrawable(R.drawable.save));
                    contentET.setFocusable(true);
                    contentET.setClickable(true);
                    isEditable = true;
                } else {
                    String content = contentET.getText().toString();
                    Log.d("比较结果，=", (content.trim().equals("")) + "");
                    if (content.trim().equals("")) {
                        Toast.makeText(EditActivity.this, "内容不能为空哦", Toast.LENGTH_SHORT).show();
                    } else {
                        //执行保存操作
                        saveData();
                        finish();
                    }
                }

                break;
            case R.id.edit_pic_bt:
//                Log.d("已点击","插入图片");
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0,0,0,600);
//                params.bottomMargin = 400;
//                bottomLL.setLayoutParams(params);
//                popupWindow.showAsDropDown(view);
//                popupWindow.showAtLocation(findViewById(R.id.edit_ll),Gravity.BOTTOM,0,0);
//                Log.d("popup是否显示：",popupWindow.isShowing()+"");
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("选择图片来源")
                        .setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                takePhoto();
                            }
                        })
                        .setPositiveButton("图库", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectFromAlbum();
                            }
                        });
                builder.show();
                break;
            case R.id.edit_alarm:
                setAlarmPopup();
                break;
            case R.id.done_bt:
                editAlarm.setBackground(getResources().getDrawable(R.drawable.alarm_black));
                Toast.makeText(EditActivity.this,"已标记为已完成",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancel_bt:
                editAlarm.setBackground(getResources().getDrawable(R.drawable.alarm_black));
                Toast.makeText(EditActivity.this,"已取消提醒",Toast.LENGTH_SHORT).show();
                doneBT.setEnabled(false);
                break;
            case R.id.add_tag_bt:
                addTag();
                break;
            case R.id.edit_weather_bt:
                chooseWeather();
                break;
            case R.id.edit_feelings_bt:
                chooseFeelings();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //选择天气
        if (weatherCKIdList.contains(compoundButton.getId())) {
            weatherIndex = compoundButton.getId();
            weather = weatherList[weatherCKIdList.indexOf(weatherIndex)];
            Log.d("天气是", weather);
            weatherCKList.get(weatherCKIdList.indexOf(weatherIndex)).setChecked(true);
            weatherDialog.dismiss();
            weatherBT.setText(weather);
        } else {
            feelingsIndex = compoundButton.getId();
            feelings = feelingsList[feelingsCKIdList.indexOf(feelingsIndex)];
            Log.d("心情是",feelings);
            feelingsCKList.get(feelingsCKIdList.indexOf(feelingsIndex)).setChecked(true);
            feelingsDialog.dismiss();
            feelingsBT.setText(feelings);
        }
    }

    private void chooseFeelings(){
        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.choose_feeling,null);
        happyCK = (CheckBox)v.findViewById(R.id.happy_checkbox);
        sweetCK = (CheckBox)v.findViewById(R.id.sweet_checkbox);
        unforgettableCK = (CheckBox)v.findViewById(R.id.unforgettable_checkbox);
        calmCK = (CheckBox)v.findViewById(R.id.calm_checkbox);
        angryCk = (CheckBox)v.findViewById(R.id.angry_checkbox);
        aggrievedCK = (CheckBox)v.findViewById(R.id.aggrieved_checkbox);
        sadCK = (CheckBox)v.findViewById(R.id.sad_checkbox);
        noFeelingsCK = (CheckBox)v.findViewById(R.id.no_checkbox);
        happyCK.setOnCheckedChangeListener(EditActivity.this);
        sweetCK.setOnCheckedChangeListener(EditActivity.this);
        unforgettableCK.setOnCheckedChangeListener(EditActivity.this);
        calmCK.setOnCheckedChangeListener(EditActivity.this);
        angryCk.setOnCheckedChangeListener(EditActivity.this);
        aggrievedCK.setOnCheckedChangeListener(EditActivity.this);
        sadCK.setOnCheckedChangeListener(EditActivity.this);
        noFeelingsCK.setOnCheckedChangeListener(EditActivity.this);
        feelingsCKList = new ArrayList<>(Arrays.asList(happyCK,sweetCK,unforgettableCK,calmCK,
                angryCk,aggrievedCK,sadCK,noFeelingsCK));

        feelingsDialog = new AlertDialog.Builder(EditActivity.this).create();
        feelingsDialog.setView(v);
        if(feelingsIndex == 0){
            feelingsCKList.get(0).setChecked(true);
        } else {
            feelingsCKList.get(feelingsCKIdList.indexOf(feelingsIndex)).setChecked(true);
        }
        feelingsDialog.show();
        int width = getWindowManager().getDefaultDisplay().getWidth();//得到当前显示设备的宽度，单位是像素
        WindowManager.LayoutParams params = feelingsDialog.getWindow().getAttributes();//得到这个dialog界面的参数对象
        params.width = 3*width/5;//设置dialog的界面宽度
        params.height =  WindowManager.LayoutParams.WRAP_CONTENT;//设置dialog高度为包裹内容
        params.gravity = Gravity.CENTER;//设置dialog的重心
        //dialog.getWindow().setLayout(width-(width/6),  LayoutParams.WRAP_CONTENT);//用这个方法设置dialog大小也可以，但是这个方法不能设置重心之类的参数，推荐用Attributes设置
        feelingsDialog.getWindow().setAttributes(params);//最后把这个参数对象设置进去，即与dialog绑定
    }

    private void chooseWeather(){
        weatherDialog = new AlertDialog.Builder(EditActivity.this).create();
        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.choose_weather,null);
        sunnyCK = (CheckBox)v.findViewById(R.id.sunny_checkbox);
        rainyCK = (CheckBox)v.findViewById(R.id.rainy_checkbox);
        cloudyCk = (CheckBox)v.findViewById(R.id.cloudy_checkbox);
        snowyCk = (CheckBox)v.findViewById(R.id.snowy_checkbox);
        foggyCK = (CheckBox)v.findViewById(R.id.foggy_checkbox);
        hazeCK = (CheckBox)v.findViewById(R.id.haze_checkbox);
        sunnyCK.setOnCheckedChangeListener(EditActivity.this);
        rainyCK.setOnCheckedChangeListener(EditActivity.this);
        cloudyCk.setOnCheckedChangeListener(EditActivity.this);
        snowyCk.setOnCheckedChangeListener(EditActivity.this);
        foggyCK.setOnCheckedChangeListener(EditActivity.this);
        hazeCK.setOnCheckedChangeListener(EditActivity.this);
        weatherCKList = new ArrayList<>
                (Arrays.asList(sunnyCK,cloudyCk,rainyCK,snowyCk,foggyCK,hazeCK));

        weatherDialog.setView(v);
        if (weatherIndex == 0){
            weatherCKList.get(weatherIndex).setChecked(true);
        } else {
            weatherCKList.get(weatherCKIdList.indexOf(weatherIndex)).setChecked(true);
        }
        weatherDialog.show();
        int width = getWindowManager().getDefaultDisplay().getWidth();//得到当前显示设备的宽度，单位是像素
        WindowManager.LayoutParams params = weatherDialog.getWindow().getAttributes();//得到这个dialog界面的参数对象
        params.width = width/2;//设置dialog的界面宽度
        params.height =  WindowManager.LayoutParams.WRAP_CONTENT;//设置dialog高度为包裹内容
        params.gravity = Gravity.CENTER;//设置dialog的重心
        //dialog.getWindow().setLayout(width-(width/6),  LayoutParams.WRAP_CONTENT);//用这个方法设置dialog大小也可以，但是这个方法不能设置重心之类的参数，推荐用Attributes设置
        weatherDialog.getWindow().setAttributes(params);//最后把这个参数对象设置进去，即与dialog绑定
    }

    private void addTag(){
        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.add_tag,null);
        final EditText addTagET = (EditText) v.findViewById(R.id.add_tag_edittext);
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setTitle("添加标签")
                .setView(v)
                .setCancelable(true)
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String content = addTagET.getText().toString().trim();
                        if (content.equals("")){
                            Toast.makeText(EditActivity.this,"标签不能为空呦",Toast.LENGTH_SHORT).show();

                        } else {
                            if (flag % 2 == 1) {
                                tag1 = content;
                                tag2TV.setVisibility(View.VISIBLE);
                                tag2TV.setText(tag1);
                            } else {
                                tag2 = content;
                                tag3TV.setVisibility(View.VISIBLE);
                                tag3TV.setText(tag2);
                            }
                            flag++;
                        }
                    }
                });
        builder.show();
    }

    private void setAlarmPopup(){
        View v = LayoutInflater.from(this).inflate(R.layout.popup_alarm,null);
        alarmPOpup = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置点击外部popup消失
        alarmPOpup.setOutsideTouchable(true);
        alarmPOpup.setBackgroundDrawable(new BitmapDrawable());

        setTimeBT = (Button)v.findViewById(R.id.set_time_bt);
        doneBT = (Button)v.findViewById(R.id.done_bt);
        cancelBT = (Button)v.findViewById(R.id.cancel_bt);
        setTimeBT.setOnClickListener(this);
        doneBT.setOnClickListener(this);
        cancelBT.setOnClickListener(this);
        alarmPOpup.showAsDropDown(findViewById(R.id.edit_alarm));
    }


    //用于确定单选钮的选中情况
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.edit_memo:
                isMemo = true;
                break;
            case R.id.edit_diary:
                isMemo = false;
                break;
        }
    }

    //保存数据
    private void saveData(){
//        SQLiteDatabase myDatabase = Connector.getDatabase();
        ItemBean newitem = new ItemBean();
        String content = contentET.getText().toString().trim();
        GetTime gt = new GetTime();
        //使用litepal库的数据库操作
        newitem.setIsMemo(isMemo);
        newitem.setNewTime(newTime);
        newitem.setOldTime(oldtime);
        newitem.setTag1(tag1);
        newitem.setTag2(tag2);
        newitem.setTag3(tag3);
        newitem.setContent(content);
        newitem.setDayTime(gt.getSpecificTime());
        //图片这不知道存的是啥。。。
//        newitem.setDrawableList(uriList);
//        newitem.setPicList(uriList);
        newitem.setPic1(uriList.get(0));
        newitem.setPic2(uriList.get(1));
        newitem.setPic3(uriList.get(0));
        newitem.setPic4(uriList.get(0));
        newitem.setFeelings(feelings);
        //貌似暂无数据，先写1吧
        newitem.setImportance(1);
        newitem.setWeather(weather);
        newitem.save();
        Log.d("已执行保存操作","");
    }

    //拍照
    private void takePhoto(){
        createUri();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //设置图片的输出地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    imageUri = data.getData();
                    uriList.add(picNum,imageUri.toString());
                    picNum++;
                    if(picNum == 4){
                        picBT.setClickable(false);
                    }
                }
                break;
            default:
                Log.d("default ","");
                break;
        }
    }

    //从相册选取
    private void selectFromAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    //创建File对象，用于存储选择的照片
    private void createUri(){
        File outputImage = new File(Environment.getExternalStorageDirectory(),"SuperBagTemp.jpg");
        try{
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
    }

}
