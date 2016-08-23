package com.example.k.superbag2.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag2.R;
import com.example.k.superbag2.adapter.MyAdapter;
import com.example.k.superbag2.bean.ImageFloder;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.view.ListImageDirPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by K on 2016/8/6.
 */
public class ChoosePicActivity extends Activity implements ListImageDirPopupWindow.OnImageDirSelected
        , View.OnClickListener {

    private ProgressDialog mProgressDialog;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs;

    private GridView mGirdView;
    private MyAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

    private RelativeLayout mBottomLy;
    private Button morePicBT;

    private TextView mImageCount;
    int totalCount = 0;

    private int mScreenHeight;

    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private Button chooPicBackBT;
    private LinearLayout chooPicBackLL;
    private Button chooPicOKBT;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
//            mProgressDialog.dismiss();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;

        initView();
        getImages();
        initEvent();

    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mImgs = Arrays.asList(mImgDir.list());
        Collections.reverse(mImgs);
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new MyAdapter(getApplicationContext(), mImgs,
                R.layout.choose_pic_grid_item, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(totalCount + "张");
    }


    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.choose_pic_list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }


    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
//        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = ChoosePicActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    String dirName = dirPath.substring(dirPath.lastIndexOf("/"));
                    if (mDirPaths.contains(dirPath) || dirName.equals("/1") || dirName.equals("/0")) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    int picSize = 0;
                    if ( parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }) != null) {
                        picSize = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.endsWith(".jpg")
                                        || filename.endsWith(".png")
                                        || filename.endsWith(".jpeg"))
                                    return true;
                                return false;
                            }
                        }).length;
                    }
                    totalCount += picSize;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();

    }

    /**
     * 初始化View
     */
    private void initView() {
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mImageCount = (TextView) findViewById(R.id.id_total_count);

        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        chooPicBackBT = (Button) findViewById(R.id.choose_pic_back);
        chooPicBackLL = (LinearLayout) findViewById(R.id.choose_pic_back_ll);
        chooPicOKBT = (Button) findViewById(R.id.choose_pic_ok);
        morePicBT = (Button)findViewById(R.id.id_choose_dir);
    }

    private void initEvent() {
        chooPicBackBT.setOnClickListener(this);
        chooPicBackLL.setOnClickListener(this);
        chooPicOKBT.setOnClickListener(this);
        morePicBT.setOnClickListener(this);
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        morePicBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void selected(ImageFloder floder) {

        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        Collections.reverse(mImgs);
        mAdapter = new MyAdapter(getApplicationContext(), mImgs,
                R.layout.choose_pic_grid_item, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mImageCount.setText(floder.getCount() + "张");
        morePicBT.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.choose_pic_back:
            case R.id.choose_pic_back_ll:
                finish();
                break;
            case R.id.choose_pic_ok:
                Intent intent = new Intent();
                intent.putStringArrayListExtra(Constant.IMAGE_URI_LIST,mAdapter.getSelectedImage());
                setResult(2, intent);
                finish();
                break;
        }
    }
}
