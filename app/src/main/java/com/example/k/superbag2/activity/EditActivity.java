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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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

import com.bumptech.glide.Glide;
import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.ItemBean;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.DialogUtils;
import com.example.k.superbag2.utils.GetImageUtils;
import com.example.k.superbag2.utils.GetTime;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by K on 2016/6/26.
 */

public class EditActivity extends BaseActivity implements View.OnClickListener,
    CheckBox.OnCheckedChangeListener{

    private Button backBT,saveBT,picBT,weatherBT,feelingsBT;
    private EditText contentET;
    private RadioGroup radioGroup;
    private ImageView headIcon;
    private TextView oldTime;
    private LinearLayout backLL,saveLL,bottomLL;
    private PopupWindow popupWindow,alarmPOpup;
    private ImageView popupPic1,popupPic2,popupPic3,popupPic4;
    private Button setTimeBT,doneBT,cancelBT,addTagBT;
    private TextView tag1TV,tag2TV,tag3TV;
    private CheckBox sunnyCK,cloudyCk,rainyCK,snowyCk,foggyCK,hazeCK;
    private AlertDialog weatherDialog;
    private CheckBox happyCK,sweetCK,unforgettableCK,calmCK,angryCk,aggrievedCK,sadCK,noFeelingsCK;
    private AlertDialog feelingsDialog;
    private ImageView pic1,pic2,pic3,pic4;

    private boolean hasSaved = false;
    private Uri imageUri;
    private List<View> popupViewList;
    private String tag1="",tag2="",tag3="";
    private int flag = 1;
    private String weather = "晴",feelings = "开心";
    private int picNum;

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
    private int lineNum;
    private int record_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
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
        weatherBT = (Button)findViewById(R.id.edit_weather_bt);
        contentET = (EditText)findViewById(R.id.edit_et);
        headIcon = (ImageView)findViewById(R.id.edit_head_icon);
        oldTime = (TextView) findViewById(R.id.edit_time);
        backLL = (LinearLayout)findViewById(R.id.edit_back_ll);
        saveLL = (LinearLayout)findViewById(R.id.edit_save_ll);
        tag1TV = (TextView)findViewById(R.id.edit_tag1);
        tag2TV = (TextView)findViewById(R.id.edit_tag2);
        tag3TV = (TextView)findViewById(R.id.edit_tag3);
        addTagBT = (Button)findViewById(R.id.add_tag_bt);
        feelingsBT = (Button)findViewById(R.id.edit_feelings_bt);
        pic1 = (ImageView)findViewById(R.id.edit_pic1);
        pic2 = (ImageView)findViewById(R.id.edit_pic2);
        pic3 = (ImageView)findViewById(R.id.edit_pic3);
        pic4 = (ImageView)findViewById(R.id.edit_pic4);

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
        backLL.setOnClickListener(this);
        saveLL.setOnClickListener(this);
        popupPic1.setOnClickListener(this);
        popupPic2.setOnClickListener(this);
        popupPic3.setOnClickListener(this);
        popupPic4.setOnClickListener(this);
        addTagBT.setOnClickListener(this);
        feelingsBT.setOnClickListener(this);
    }

    private void initData(){
        //表示已选取的图片数量
        picNum = 0;
        GetTime gt = new GetTime();
        oldtime = gt.getYear()+"-"+gt.getMonth()+"-"+gt.getDay();
        oldTime.setText(oldtime);

//        uriList = new ArrayList<>();
        uriList = new ArrayList<>(Arrays.asList("","","",""));

        //如果是从ListView点击进入活动，则初始化数据;
        //如何让editTExt无法点击编辑，还有问题
        Intent intent = getIntent();
        lineNum = intent.getIntExtra(Constant.EDIT_DONE,-1);
        Log.d("edit act", lineNum +"");
        if (lineNum != -1){//从列表点击进入
            record_num = DataSupport.count(ItemBean.class);
            ItemBean item = DataSupport.find(ItemBean.class, record_num -lineNum);
            contentET.setText(item.getContent());

            if (!item.getTag1().equals("")){
                tag1TV.setVisibility(View.VISIBLE);
                tag1TV.setText(item.getTag1());
            }
            if (!item.getTag2().equals("")){
                tag2TV.setVisibility(View.VISIBLE);
                tag2TV.setText(item.getTag2());
            }
            if (!item.getTag3().equals("")){
                tag3TV.setVisibility(View.VISIBLE);
                tag3TV.setText(item.getTag3());
            }
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
                    String content = contentET.getText().toString();
                    if (content.trim().equals("")) {
                        Toast.makeText(EditActivity.this, "内容不能为空呦", Toast.LENGTH_SHORT).show();
                    } else {
                        //执行保存操作
                        saveData();
                        finish();
                    }
                break;
            case R.id.edit_pic_bt:
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
            case R.id.cancel_bt:
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
        DialogUtils.setDialog(EditActivity.this,feelingsDialog,3,5);
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
        DialogUtils.setDialog(EditActivity.this,weatherDialog,1,2);
    }

    private void addTag(){
//        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.add_tag,null);
//        final EditText addTagET = (EditText) v.findViewById(R.id.add_tag_edittext);
        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.add_tag2,null);
        final EditText addTagET = (TagsEditText) v.findViewById(R.id.add_tag2_et);

        final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        /*builder.setTitle("添加标签")
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
                });*/
        builder.setView(v)
                .setCancelable(true)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //得到的标签是字符串，每个标签中间用空格分开
                        String tags = addTagET.getText().toString();
                        // TODO
                        String[] allTag = tags.split(" ");
                        switch (allTag.length){
                            case 0:
                                break;
                            case 1:
                                tag1 = allTag[0];
                                tag1TV.setText(tag1);
                                tag1TV.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                tag1 = allTag[0];
                                tag2 = allTag[1];
                                tag1TV.setText(tag1);
                                tag1TV.setVisibility(View.VISIBLE);
                                tag2TV.setText(tag2);
                                tag2TV.setVisibility(View.VISIBLE);
                                break;
                            default:
                                tag1 = allTag[0];
                                tag2 = allTag[1];
                                tag3 = allTag[2];
                                tag1TV.setText(tag1);
                                tag1TV.setVisibility(View.VISIBLE);
                                tag2TV.setText(tag2);
                                tag2TV.setVisibility(View.VISIBLE);
                                tag3TV.setText(tag3);
                                tag3TV.setVisibility(View.VISIBLE);
                                break;
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }

    //保存数据
    private void saveData(){
        if (lineNum!=-1){
            save_edit();
        }else {
            save_first();
        }
    }
    private void save_edit(){
        ItemBean newitem = new ItemBean();
        String content = contentET.getText().toString();
        GetTime gt = new GetTime();
        newitem.setTag1(tag1);
        newitem.setTag2(tag2);
        newitem.setTag3(tag3);
        newitem.setContent(content);
        newitem.setDayTime(gt.getSpecificTime());
        newitem.setPic1(uriList.get(0));
        newitem.setPic2(uriList.get(1));
        newitem.setPic3(uriList.get(2));
        newitem.setPic4(uriList.get(3));
        newitem.setFeelings(feelings);
        newitem.setImportance(1);
        newitem.setWeather(weather);
        newitem.update(record_num-lineNum);
        Log.d("已执行修改操作","");
    }
    private void save_first(){
        ItemBean newitem = new ItemBean();
        String content = contentET.getText().toString();
        GetTime gt = new GetTime();
        newitem.setTag1(tag1);
        newitem.setTag2(tag2);
        newitem.setTag3(tag3);
        newitem.setContent(content);
        newitem.setDayTime(gt.getSpecificTime());

        newitem.setPic1(uriList.get(0));
        newitem.setPic2(uriList.get(1));
        newitem.setPic3(uriList.get(2));
        newitem.setPic4(uriList.get(3));
        newitem.setFeelings(feelings);
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
                    uriList.set(picNum,imageUri.toString());
                    if(picNum == 0){
                        Glide.with(EditActivity.this)
                                .load(imageUri)
                                .asBitmap()
                                .into(pic1);
                        pic1.setVisibility(View.VISIBLE);
                    } else if (picNum == 1){
                        Glide.with(EditActivity.this)
                                .load(imageUri)
                                .asBitmap()
                                .into(pic2);
                        pic2.setVisibility(View.VISIBLE);
                    } else if (picNum == 2){
                        Glide.with(EditActivity.this)
                                .load(imageUri)
                                .asBitmap()
                                .into(pic3);
                        pic3.setVisibility(View.VISIBLE);
                    } else if (picNum == 4){
                        Glide.with(EditActivity.this)
                                .load(imageUri)
                                .asBitmap()
                                .into(pic4);
                        pic4.setVisibility(View.VISIBLE);
                    }
                    picNum = picNum + 1;
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
