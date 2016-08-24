package com.example.k.superbag2.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;
import com.example.k.superbag2.activity.ChooseLockActivity;
import com.example.k.superbag2.activity.LoginActivity;
import com.example.k.superbag2.bean.ItemBean;
import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.bean.UploadItembean;
import com.example.k.superbag2.bean.UploadMemoItem;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.GetImageUtils;
import com.example.k.superbag2.utils.GetTime;
import com.example.k.superbag2.utils.LoginUtils;
import com.example.k.superbag2.utils.SaveUtils;
import com.example.k.superbag2.view.RoundImageView;
import com.flyco.animation.Attention.Tada;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by K on 2016/7/26.
 */
public class SettingsLeftFragment extends Fragment {

    private LinearLayout changeHeadLL, changeBgLL, uploadLL, downloadLL, aboutLL, lockLL;
    private Uri imageUri;
    private ImageView headIV;
    private TextView hintTextView;
    private Context context;
    private UploadMemoItem uploadMemoItem;
    private UploadItembean uploadItembean;
    private MemoItem memoItem;
    private ItemBean data;
    private Handler myHd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (message.what){
                case 1:
                    Toast.makeText(MyApplication.getContext(),"数据下载成功，正在加载，请稍候",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MyApplication.getContext(),"成功上传数据",Toast.LENGTH_SHORT).show();

            }
        }
    };
    private Message message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sliding_settings, container, false);
        context = getContext();
        message = new Message();

        initView(v);
        initListener();
        return v;
    }

    private void initView(View v) {
        changeHeadLL = (LinearLayout) v.findViewById(R.id.change_head_ll);
        changeBgLL = (LinearLayout) v.findViewById(R.id.change_bg_ll);
        uploadLL = (LinearLayout) v.findViewById(R.id.upload_ll);
        downloadLL = (LinearLayout) v.findViewById(R.id.download_ll);
        aboutLL = (LinearLayout) v.findViewById(R.id.about_ll);
        lockLL = (LinearLayout) v.findViewById(R.id.password_ll);
        headIV = (RoundImageView) v.findViewById(R.id.setting_head);
        hintTextView = (TextView) v.findViewById(R.id.setting_hint);

        if (LoginUtils.getLoginStatus()){
            hintTextView.setVisibility(View.GONE);
            Uri headUri = GetImageUtils.getUri(context, Constant.HEAD_ICON_URI);
            if (!headUri.toString().equals("")){
                Glide.with(context)
                        .load(headUri)
                        .asBitmap()
                        .into(headIV);
            }
        }
    }
    private void initListener() {
        changeHeadLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFromAlbum(1);
            }
        });
        changeBgLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFromAlbum(2);
            }
        });
        uploadLL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (LoginUtils.getLoginStatus()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            upLoad();
                            message.what = 2;
                            myHd.sendMessage(message);
                        }
                    }).start();
                }else {
                    Toast.makeText(MyApplication.getContext(),"请先登录！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        downloadLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginUtils.getLoginStatus()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            message.what = 1;
                            myHd.sendMessage(message);
                            download();
                        }
                    }).start();
                    Log.d("download","+download ing+++++++");

                }else {
                    Toast.makeText(MyApplication.getContext(),"请先登录！",Toast.LENGTH_SHORT).show();
                }

            }
        });
        aboutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "两个大帅比的作品", Toast.LENGTH_SHORT).show();
            }
        });
        lockLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ChooseLockActivity.class));
            }
        });
        headIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NormalDialog dialog = new NormalDialog(context);
                if (LoginUtils.getLoginStatus()){
                    dialog.isTitleShow(false)//
                            .bgColor(Color.parseColor("#383838"))//
                            .cornerRadius(5)//
                            .content("退出登录?")//
                            .contentGravity(Gravity.CENTER)//
                            .contentTextColor(Color.parseColor("#ffffff"))//
                            .dividerColor(Color.parseColor("#222222"))//
                            .btnTextSize(15.5f, 15.5f)//
                            .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))//
                            .btnPressColor(Color.parseColor("#2B2B2B"))//
                            .widthScale(0.85f)//
                            .showAnim(new Tada())//
                            .dismissAnim(new ZoomOutExit())//
                            .show();
                }else {
                    dialog.isTitleShow(false)//
                            .bgColor(Color.parseColor("#383838"))//
                            .cornerRadius(5)//
                            .content("现在登录?")//
                            .contentGravity(Gravity.CENTER)//
                            .contentTextColor(Color.parseColor("#ffffff"))//
                            .dividerColor(Color.parseColor("#222222"))//
                            .btnTextSize(15.5f, 15.5f)//
                            .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))//
                            .btnPressColor(Color.parseColor("#2B2B2B"))//
                            .widthScale(0.85f)//
                            .showAnim(new Tada())//
                            .dismissAnim(new ZoomOutExit())//
                            .show();
                }
                dialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        LoginUtils.setLoginStatus(false);
                        startActivity(new Intent(context, LoginActivity.class));
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private void upLoad(){
        //在本地记录更新时间，若记录为空，表示没有上传过，此时上传本地所有记录。
        if (SaveUtils.getUpdateTime() == 0){
            //0:初始（可以上传） 上传完毕后，设置为当前时间
            List<ItemBean> itemBeen = DataSupport.findAll(ItemBean.class);
            Log.d("upload","diary upload error"+itemBeen.size());
            //上传查询到的数据。
            for (int i = 0;i<itemBeen.size();i++){
                data = itemBeen.get(i);
                uploadItembean = new UploadItembean();
                uploadItembean.setContent(data.getContent());
                uploadItembean.setDayTime(data.getDayTime());
                uploadItembean.setFeelings(data.getFeelings());
                uploadItembean.setPic1(data.getPic1());
                uploadItembean.setPic2(data.getPic2());
                uploadItembean.setPic3(data.getPic3());
                uploadItembean.setPic4(data.getPic4());
                uploadItembean.setTag1(data.getTag1());
                uploadItembean.setTag2(data.getTag2());
                uploadItembean.setTag3(data.getTag3());
                uploadItembean.setUpdateTime(data.getUpdateTime());
                uploadItembean.setPhone(LoginUtils.getPhoneNumber());
                uploadItembean.setWeather(data.getWeather());
                uploadItembean.save(new SaveListener<String>(){
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                            //save successful
                        }else {
                            Log.d("upload","diary upload error");
                        }
                    }
                });
            }
            List<MemoItem> memoItemList = DataSupport.findAll(MemoItem.class);
            for (int j = 0;j<memoItemList.size();j++){
                memoItem = memoItemList.get(j);
                uploadMemoItem = new UploadMemoItem();
                uploadMemoItem.setImportance(memoItem.getImportance());
                uploadMemoItem.setAlarm(memoItem.isAlarm()?true:false);
                uploadMemoItem.setAlarmTime(memoItem.getAlarmTime());
                uploadMemoItem.setContent(memoItem.getContent());
                uploadMemoItem.setDate(memoItem.getDate());
                uploadMemoItem.setEditTime(memoItem.getEditTime());
                uploadMemoItem.setShake(memoItem.isShake()?true:false);
                uploadMemoItem.setSound(memoItem.isSound()?true:false);
                uploadMemoItem.setTitle(memoItem.getTitle());
                uploadMemoItem.setPhone(LoginUtils.getPhoneNumber());
                uploadMemoItem.setUpdateTime(data.getUpdateTime());
                uploadMemoItem.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){

                        }else {
                            Log.d("upload"," memo upload error");
                        }
                    }
                });
            }
            //记录更新时间。
        }else {
            //否则，从本地数据库筛选出创建时间晚于上传时间的记录，上传这些记录。
            float date = SaveUtils.getUpdateTime();
            List<ItemBean> itemBeanList = DataSupport.where("updateTime>?",date+"").find(ItemBean.class);
            Log.d("upload","diary upload error"+itemBeanList.size());

            for (int i = 0;i<itemBeanList.size();i++) {
                Log.d("upload","diary upload error"+"========================");

                data = itemBeanList.get(i);
                uploadItembean = new UploadItembean();
                uploadItembean.setContent(data.getContent());
                uploadItembean.setDayTime(data.getDayTime());
                uploadItembean.setFeelings(data.getFeelings());
                uploadItembean.setPic1(data.getPic1());
                uploadItembean.setPic2(data.getPic2());
                uploadItembean.setPic3(data.getPic3());
                uploadItembean.setPic4(data.getPic4());
                uploadItembean.setTag1(data.getTag1());
                uploadItembean.setTag2(data.getTag2());
                uploadItembean.setTag3(data.getTag3());
                uploadItembean.setUpdateTime(data.getUpdateTime());
                uploadItembean.setPhone(LoginUtils.getPhoneNumber());
                uploadItembean.setWeather(data.getWeather());
                uploadItembean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            //save successful
                        } else {
                            Log.d("upload"," memo upload error");
                        }
                    }
                });
            }
            List<MemoItem> memoItemList = DataSupport.where("updateTime>?","date").find(MemoItem.class);
            for (int j = 0;j<memoItemList.size();j++){
                memoItem = memoItemList.get(j);
                uploadMemoItem = new UploadMemoItem();
                uploadMemoItem.setImportance(memoItem.getImportance());
                uploadMemoItem.setAlarm(memoItem.isAlarm()?true:false);
                uploadMemoItem.setAlarmTime(memoItem.getAlarmTime());
                uploadMemoItem.setContent(memoItem.getContent());
                uploadMemoItem.setDate(memoItem.getDate());
                uploadMemoItem.setEditTime(memoItem.getEditTime());
                uploadMemoItem.setShake(memoItem.isShake()?true:false);
                uploadMemoItem.setSound(memoItem.isSound()?true:false);
                uploadMemoItem.setTitle(memoItem.getTitle());
                uploadMemoItem.setPhone(LoginUtils.getPhoneNumber());

                uploadMemoItem.setUpdateTime(memoItem.getUpdateTime());
                uploadMemoItem.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                        }else {
                            Log.d("upload"," memo upload error");
                        }
                    }
                });
            }
        }
        //记录更新时间。
        GetTime gt = new GetTime();
        String daytime = gt.getSpecificTime().replace("-","").replace(" ","").replace(":","");
        SaveUtils.setUpdateTime(Long.parseLong(daytime));
    }
    /*本地数据与网络数据都按updateTime排序处理。
                   * 例： 本地 1 2 4 5 6
                   *      网络 1 2 3 4 5 6
                   *    相等->下一数据  本地大->给本地递增量加1，将网络序号对应数据存至本地，下一轮
                   *    如此循环至结束。
                   * */
    private void download(){
        Log.d("download","+download in 1");

        final List<ItemBean> list_diary = DataSupport.order("updateTime asc").find(ItemBean.class);
        final List<MemoItem> list_memo = DataSupport.order("updateTime asc").find(MemoItem.class);

        BmobQuery<UploadItembean> uploadItembeanBmobQuery = new BmobQuery<UploadItembean>();
        BmobQuery<UploadMemoItem> uploadMemoItemBmobQuery = new BmobQuery<UploadMemoItem>();
        //取手机号为当前用户的数据
        uploadItembeanBmobQuery.addWhereEqualTo("phone",LoginUtils.getPhoneNumber());
        uploadMemoItemBmobQuery.addWhereEqualTo("phone",LoginUtils.getPhoneNumber());

        uploadItembeanBmobQuery.setLimit(100);
        uploadMemoItemBmobQuery.setLimit(100);
        //将网络数据库中的数据按updateTime升序排列
        uploadItembeanBmobQuery.order("updateTime");
        uploadMemoItemBmobQuery.order("updateTime");
        //ItemBean 下载
        uploadItembeanBmobQuery.findObjects(new FindListener<UploadItembean>() {
            private ItemBean itemBean_local;
            @Override
            public void done(List<UploadItembean> list, BmobException e) {

                if (e==null){
                    int i = 0;
                    int j = 0;
                    Log.d("download","+download in "+list.size());

                    for (UploadItembean itembean : list){
                        Log.d("download"," item "+itembean.getUpdateTime());
                        Log.d("download"," item "+list_diary.size());
                        if (list_diary.size()-i>0){
                            if (list_diary.get(i).getUpdateTime()>list.get(j).getUpdateTime()){
                                Log.d("download","   nei"+list_diary.get(i).getUpdateTime()+list.get(j).getUpdateTime());
                                itemBean_local = new ItemBean();
                                itemBean_local.setWeather(itembean.getWeather());
                                itemBean_local.setUpdateTime(itembean.getUpdateTime());
                                itemBean_local.setDayTime(itembean.getDayTime());
                                itemBean_local.setContent(itembean.getContent());
                                itemBean_local.setFeelings(itembean.getFeelings());
                                itemBean_local.setTag1(itembean.getTag1());
                                itemBean_local.setTag2(itembean.getTag2());
                                itemBean_local.setTag3(itembean.getTag3());
                                itemBean_local.setPic1(itembean.getPic1());
                                itemBean_local.setPic2(itembean.getPic2());
                                itemBean_local.setPic3(itembean.getPic3());
                                itemBean_local.setPic4(itembean.getPic4());
                                itemBean_local.save();
                                j++;
                            }else if (list_diary.get(i).getUpdateTime() == list.get(j).getUpdateTime()){
                                i++;
                                j++;
                            }
                        }else {
                            j++;
                            Log.d("download"," item "+itembean.getUpdateTime());
                            itemBean_local = new ItemBean();
                            itemBean_local.setWeather(itembean.getWeather());
                            itemBean_local.setUpdateTime(itembean.getUpdateTime());
                            itemBean_local.setDayTime(itembean.getDayTime());
                            itemBean_local.setContent(itembean.getContent());
                            itemBean_local.setFeelings(itembean.getFeelings());
                            itemBean_local.setTag1(itembean.getTag1());
                            itemBean_local.setTag2(itembean.getTag2());
                            itemBean_local.setTag3(itembean.getTag3());
                            itemBean_local.setPic1(itembean.getPic1());
                            itemBean_local.setPic2(itembean.getPic2());
                            itemBean_local.setPic3(itembean.getPic3());
                            itemBean_local.setPic4(itembean.getPic4());
                            itemBean_local.save();
                        }

                        if (j == list.size()){
                            Intent intent = new Intent("local.broadcast");
                            MainActivity.localBroadcastManager.sendBroadcast(intent);
                        }
                    }
                }
            }
        });
        uploadMemoItemBmobQuery.findObjects(new FindListener<UploadMemoItem>() {
            private MemoItem memoItem_local;
            @Override
            public void done(List<UploadMemoItem> list, BmobException e) {
                if (e == null){
                    int i = 0,j = 0;
                    for (UploadMemoItem memoItem : list){
                        if (list_memo.size()-i>0){
                            if (list_memo.get(i).getUpdateTime()>list.get(j).getUpdateTime()){
                                memoItem_local = new MemoItem();
                                memoItem_local.setUpdateTime(memoItem.getUpdateTime());
                                memoItem_local.setContent(memoItem.getContent());
                                memoItem_local.setTitle(memoItem.getTitle());
                                memoItem_local.setAlarm(memoItem.isAlarm());
                                memoItem_local.setAlarmTime(memoItem.getAlarmTime());
                                memoItem_local.setDate(memoItem.getDate());
                                memoItem_local.setEditTime(memoItem.getEditTime());
                                memoItem_local.setImportance(memoItem.getImportance());
                                memoItem_local.setShake(memoItem.isShake());
                                memoItem_local.setSound(memoItem.isSound());
                                memoItem_local.save();
                                j++;
                            }else if (list_diary.get(i).getUpdateTime() == list.get(j).getUpdateTime()){
                                i++;
                                j++;
                            }
                        }else {
                            memoItem_local = new MemoItem();
                            memoItem_local.setUpdateTime(memoItem.getUpdateTime());
                            memoItem_local.setContent(memoItem.getContent());
                            memoItem_local.setTitle(memoItem.getTitle());
                            memoItem_local.setAlarm(memoItem.isAlarm());
                            memoItem_local.setAlarmTime(memoItem.getAlarmTime());
                            memoItem_local.setDate(memoItem.getDate());
                            memoItem_local.setEditTime(memoItem.getEditTime());
                            memoItem_local.setImportance(memoItem.getImportance());
                            memoItem_local.setShake(memoItem.isShake());
                            memoItem_local.setSound(memoItem.isSound());
                            memoItem_local.save();
                            j++;
                        }

                        if (j == list.size()){
                            Intent intent = new Intent("local.broadcast");
                            MainActivity.localBroadcastManager.sendBroadcast(intent);
                        }
                    }
                }
            }
        });

    }

    /**
     * 从相册选取,参数用于确定头像还是背景
     *
     * @param i 头像为1，背景为2
     */
    //在6.0以上有bug,其他未测试
    private void selectFromAlbum(int i) {
        createUri(i);
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, i);
/*
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image");
            intent.addCategory("android.intent.category.OPENABLE");
            startActivityForResult(intent,i);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image");
                startActivityForResult(intent,i);
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }
*/

       /* if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("*image*//*");
            startActivityForResult(intent, i);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*image*//*");
            startActivityForResult(intent, i);
        }
*/
    }

    //创建File对象，用于存储选择的照片
    private void createUri(int i) {
        File outputImage;
        if (i == 1) {
            outputImage = new File(Environment.getExternalStorageDirectory(), "sbHeadTemp.jpg");
        } else {
            outputImage = new File(Environment.getExternalStorageDirectory(), "sbBGTemp.jpg");
        }
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 1:
                    if (resultCode != -1) {
                        return;
                    } else {  //头像
                        //测试
                        imageUri = data.getData();
                        saveData(1);

                    }
                    break;
                case 2:
                    imageUri = data.getData();
                    saveData(2);
                    break;
            }
        }
    }

    //存储，参数为1代表头像，2代表背景
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void saveData(int i) {
        Log.d("uri是  ", imageUri.toString());

        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (i == 1) {
            editor.putString(Constant.HEAD_ICON_URI, imageUri.toString());
            editor.putBoolean("HEAD_CHANGED", true);
            Toast.makeText(context, "头像更换成功！", Toast.LENGTH_SHORT).show();
        } else {
            editor.putString(Constant.BACKGROUND_URI, imageUri.toString());
            editor.putBoolean("BG_CHANGED", true);
            Toast.makeText(context, "背景更换成功！", Toast.LENGTH_SHORT).show();
        }
        editor.commit();
    }

}

