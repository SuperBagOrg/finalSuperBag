package com.example.k.superbag2.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.EditData;
import com.example.k.superbag2.bean.ItemBean;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.DialogUtils;
import com.example.k.superbag2.utils.GetImageUtils;
import com.example.k.superbag2.utils.GetTime;
import com.example.k.superbag2.view.RichTextEditor;
import com.example.k.superbag2.view.RichTextEditor2;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by K on 2016/6/26.
 */

public class EditActivity extends BaseActivity implements View.OnClickListener,
        CheckBox.OnCheckedChangeListener {

    private Button backBT, saveBT, picBT, weatherBT, feelingsBT;
    //    private EditText contentET;
//    private ImageView headIcon;
//    private TextView oldTime;
    private LinearLayout backLL, saveLL;
    private Button addTagBT;
    private TextView tag1TV, tag2TV, tag3TV;
    private CheckBox sunnyCK, cloudyCk, rainyCK, snowyCk, foggyCK, hazeCK;
    private AlertDialog weatherDialog;
    private CheckBox happyCK, sweetCK, unforgettableCK, calmCK, angryCk, aggrievedCK, sadCK, noFeelingsCK;
    private AlertDialog feelingsDialog;
    private RichTextEditor contentET;
    private TextView editTitle;

    private boolean hasSaved = false;
    private String tag1 = "", tag2 = "", tag3 = "";
    private String weather = "晴", feelings = "开心";
    private List<Integer> picIndex;

    private List<String> uriList;

    private List<Integer> weatherCKIdList = new ArrayList<>(Arrays.asList(R.id.sunny_checkbox,
            R.id.cloudy_checkbox, R.id.rainy_checkbox,
            R.id.snowy_checkbox, R.id.foggy_checkbox, R.id.haze_checkbox));
    private String[] weatherList = {"晴", "阴", "雨", "雪", "雾", "霾"};
    private List<CheckBox> weatherCKList;
    private int weatherIndex = 0;

    private List<Integer> feelingsCKIdList = new ArrayList<>(Arrays.asList(R.id.happy_checkbox,
            R.id.sweet_checkbox, R.id.unforgettable_checkbox,
            R.id.calm_checkbox, R.id.angry_checkbox, R.id.aggrieved_checkbox,
            R.id.sad_checkbox, R.id.no_checkbox));
    private String[] feelingsList = {"开心", "甜蜜", "难忘", "平静", "生气", "委屈", "伤心", "无"};
    private List<CheckBox> feelingsCKList;
    private int feelingsIndex = 0;
    private String oldtime;
    private int lineNum = 0;
    private int record_num;
    private String content;
    private Intent intent;
    private Long creatTime;


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

    @Override
    protected void onResume() {
        super.onResume();
        //在这儿判断
        intent = getIntent();
        //若为-1，表示新建

        lineNum = intent.getIntExtra(Constant.EDIT_DONE, -1);
        Log.d("edit act", lineNum + "");
        if (lineNum != -1) {
            //从列表点击进入。需要初始化数据
            record_num = DataSupport.count(ItemBean.class);
            ItemBean item = DataSupport.find(ItemBean.class, record_num - lineNum);
//            contentET.setText(item.getContent());

            if (!item.getTag1().equals("")) {
                tag1TV.setVisibility(View.VISIBLE);
                tag1TV.setText(item.getTag1());
            }
            if (!item.getTag2().equals("")) {
                tag2TV.setVisibility(View.VISIBLE);
                tag2TV.setText(item.getTag2());
            }
            if (!item.getTag3().equals("")) {
                tag3TV.setVisibility(View.VISIBLE);
                tag3TV.setText(item.getTag3());
            }

            uriList = item.getPicList();
        }
    }

    private void initView() {
        backBT = (Button) findViewById(R.id.edit_back);
        saveBT = (Button) findViewById(R.id.edit_save);
        picBT = (Button) findViewById(R.id.edit_pic_bt);
        weatherBT = (Button) findViewById(R.id.edit_weather_bt);
        contentET = (RichTextEditor) findViewById(R.id.edit_et);
//        headIcon = (ImageView) findViewById(R.id.edit_head_icon);
//        oldTime = (TextView) findViewById(R.id.edit_time);
        backLL = (LinearLayout) findViewById(R.id.edit_back_ll);
        saveLL = (LinearLayout) findViewById(R.id.edit_save_ll);
        tag1TV = (TextView) findViewById(R.id.edit_tag1);
        tag2TV = (TextView) findViewById(R.id.edit_tag2);
        tag3TV = (TextView) findViewById(R.id.edit_tag3);
        addTagBT = (Button) findViewById(R.id.add_tag_bt);
        feelingsBT = (Button) findViewById(R.id.edit_feelings_bt);
        editTitle = (TextView)findViewById(R.id.edit_title);
        //设置头像
//        Bitmap head = GetImageUtils.getBMFromUri(this, Constant.HEAD_ICON_URI);
//        if (head != null) {
//            headIcon.setImageBitmap(head);
//        }
    }
    private void initListener() {
        backBT.setOnClickListener(this);
        saveBT.setOnClickListener(this);
        picBT.setOnClickListener(this);
        weatherBT.setOnClickListener(this);
        backLL.setOnClickListener(this);
        saveLL.setOnClickListener(this);
        addTagBT.setOnClickListener(this);
        feelingsBT.setOnClickListener(this);
    }

    private void initData() {
        GetTime gt = new GetTime();
        oldtime = gt.getYear() + "-" + gt.getMonth() + "-" + gt.getDay();
//        oldTime.setText(oldtime);

        editTitle.setText(gt.getMonthEn()+" "+gt.getDay()+" ,  "+gt.getHour()+"."+gt.getMin());

        uriList = new ArrayList<>(Arrays.asList("", "", "", ""));
        picIndex = new ArrayList<>(Arrays.asList(-1, -1, -1, -1));
        //如果是从ListView点击进入活动，则初始化数据;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_back_ll:
            case R.id.edit_back:
                if (hasSaved) {
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
                //存储
                List<EditData> editDatas = contentET.getData();

                Log.d("rich_edit","uriLIst大小是"+": "+editDatas.size());
                StringBuilder sb = new StringBuilder();
                int count = 0;
                for (EditData save_rich:editDatas){
                    sb.append(save_rich.getInputStr());
                    if (!save_rich.getImagePath().equals("")){
                        uriList.set(count,save_rich.getImagePath());
                        count++;
                    }
                    creatTime = Long.parseLong(new GetTime().getSpecificTime2Second());
                    save_rich.setUpdateTime(creatTime);
                    save_rich.save();
                }

                content = sb.toString();
                if (content.trim().equals("")) {
                    Toast.makeText(EditActivity.this, "内容不能为空呦", Toast.LENGTH_SHORT).show();
                } else {
                    //执行保存操作
                    saveData(lineNum);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                break;
            case R.id.edit_pic_bt:
                //使用兼容库就无需判断系统版本
                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(EditActivity.this, ChoosePicActivity.class);
                    startActivityForResult(intent, 2);
                }
                //需要弹出dialog让用户手动赋予权限
                else{
                    ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                }
                break;
            case R.id.cancel_bt:
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (data != null) {
                    ArrayList<String> tempPics = data.getStringArrayListExtra(Constant.IMAGE_URI_LIST);
                    for (int i = 0; i < tempPics.size(); i++) {
                        //ImageView插入操作
//                        uriList.set(i, tempPics.get(i));
                        Log.d("edit选取的图片地址是：  ",tempPics.get(i));
                        insertBitmap(GetImageUtils.getRealFilePath(this, Uri.parse(tempPics.get(i))));
                    }
                }
                break;
        }

    }

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath) {
        contentET.insertImage(imagePath);
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
            Log.d("心情是", feelings);
            feelingsCKList.get(feelingsCKIdList.indexOf(feelingsIndex)).setChecked(true);
            feelingsDialog.dismiss();
            feelingsBT.setText(feelings);
        }
    }

    private void chooseFeelings() {
        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.choose_feeling, null);
        happyCK = (CheckBox) v.findViewById(R.id.happy_checkbox);
        sweetCK = (CheckBox) v.findViewById(R.id.sweet_checkbox);
        unforgettableCK = (CheckBox) v.findViewById(R.id.unforgettable_checkbox);
        calmCK = (CheckBox) v.findViewById(R.id.calm_checkbox);
        angryCk = (CheckBox) v.findViewById(R.id.angry_checkbox);
        aggrievedCK = (CheckBox) v.findViewById(R.id.aggrieved_checkbox);
        sadCK = (CheckBox) v.findViewById(R.id.sad_checkbox);
        noFeelingsCK = (CheckBox) v.findViewById(R.id.no_checkbox);
        happyCK.setOnCheckedChangeListener(EditActivity.this);
        sweetCK.setOnCheckedChangeListener(EditActivity.this);
        unforgettableCK.setOnCheckedChangeListener(EditActivity.this);
        calmCK.setOnCheckedChangeListener(EditActivity.this);
        angryCk.setOnCheckedChangeListener(EditActivity.this);
        aggrievedCK.setOnCheckedChangeListener(EditActivity.this);
        sadCK.setOnCheckedChangeListener(EditActivity.this);
        noFeelingsCK.setOnCheckedChangeListener(EditActivity.this);
        feelingsCKList = new ArrayList<>(Arrays.asList(happyCK, sweetCK, unforgettableCK, calmCK,
                angryCk, aggrievedCK, sadCK, noFeelingsCK));

        feelingsDialog = new AlertDialog.Builder(EditActivity.this).create();
        feelingsDialog.setView(v);
        if (feelingsIndex == 0) {
            feelingsCKList.get(0).setChecked(true);
        } else {
            feelingsCKList.get(feelingsCKIdList.indexOf(feelingsIndex)).setChecked(true);
        }
        feelingsDialog.show();
        DialogUtils.setDialog(EditActivity.this, feelingsDialog, 3, 5);
    }

    private void chooseWeather() {
        weatherDialog = new AlertDialog.Builder(EditActivity.this).create();
        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.choose_weather, null);
        sunnyCK = (CheckBox) v.findViewById(R.id.sunny_checkbox);
        rainyCK = (CheckBox) v.findViewById(R.id.rainy_checkbox);
        cloudyCk = (CheckBox) v.findViewById(R.id.cloudy_checkbox);
        snowyCk = (CheckBox) v.findViewById(R.id.snowy_checkbox);
        foggyCK = (CheckBox) v.findViewById(R.id.foggy_checkbox);
        hazeCK = (CheckBox) v.findViewById(R.id.haze_checkbox);
        sunnyCK.setOnCheckedChangeListener(EditActivity.this);
        rainyCK.setOnCheckedChangeListener(EditActivity.this);
        cloudyCk.setOnCheckedChangeListener(EditActivity.this);
        snowyCk.setOnCheckedChangeListener(EditActivity.this);
        foggyCK.setOnCheckedChangeListener(EditActivity.this);
        hazeCK.setOnCheckedChangeListener(EditActivity.this);
        weatherCKList = new ArrayList<>
                (Arrays.asList(sunnyCK, cloudyCk, rainyCK, snowyCk, foggyCK, hazeCK));

        weatherDialog.setView(v);
        if (weatherIndex == 0) {
            weatherCKList.get(weatherIndex).setChecked(true);
        } else {
            weatherCKList.get(weatherCKIdList.indexOf(weatherIndex)).setChecked(true);
        }
        weatherDialog.show();
        DialogUtils.setDialog(EditActivity.this, weatherDialog, 1, 2);
    }

    private void addTag() {
//        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.add_tag,null);
//        final EditText addTagET = (EditText) v.findViewById(R.id.add_tag_edittext);
        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.add_tag2, null);
        final EditText addTagET = (TagsEditText) v.findViewById(R.id.add_tag2_et);

        final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setView(v)
                .setCancelable(true)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //得到的标签是字符串，每个标签中间用空格分开
                        String tags = addTagET.getText().toString();
                        // TODO
                        String[] allTag = tags.split(" ");
                        switch (allTag.length) {
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
        AlertDialog dialog = builder.create();
        builder.show();
        DialogUtils.setDialog(EditActivity.this,dialog,2,3);
    }

    //从相册选取
    private void selectFromAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    /**
     * 保存数据
     *
     * @param whichToSave -1 表示保存
     *                    其他表更新
     */
    private void saveData(int whichToSave) {
        ItemBean newItem = new ItemBean();
        GetTime gt = new GetTime();
        newItem.setTag1(tag1);
        newItem.setTag2(tag2);
        newItem.setTag3(tag3);
        newItem.setContent(content);
        newItem.setDayTime(gt.getSpecificTime());
        newItem.setPic1(uriList.get(0));
        newItem.setPic2(uriList.get(1));
        newItem.setPic3(uriList.get(2));
        newItem.setPic4(uriList.get(3));
        newItem.setFeelings(feelings);
        newItem.setImportance(1);
        newItem.setWeather(weather);
        newItem.setPic1Index(picIndex.get(0));
        newItem.setPic2Index(picIndex.get(1));
        newItem.setPic3Index(picIndex.get(2));
        newItem.setPic4Index(picIndex.get(3));
        newItem.setUpdateTime(creatTime);
        if (whichToSave == -1) {
            newItem.save();
            Log.d("新建保存", "");
        } else {
            newItem.update(record_num - lineNum);
            Log.d("更新操作", "");
        }

    }
}
