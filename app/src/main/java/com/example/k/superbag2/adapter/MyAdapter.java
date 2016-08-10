package com.example.k.superbag2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.k.superbag2.R;
import com.example.k.superbag2.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends CommonAdapter<String> {

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage;

    /**
     * 文件夹路径
     */
    private String mDirPath;
    private Context context;

    public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
                     String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.context = context;
        mSelectedImage = new ArrayList<>();
    }

    @Override
    public void convert(ViewHolder helper, final String item) {

        helper.setImageResource(R.id.id_item_select,R.drawable.place_holder);
        //设置no_selected
        helper.setImageResource(R.id.id_item_select,
                R.drawable.square);
        //设置图片
        Glide.with(context)
                .load(mDirPath + "/" + item)
                .into((ImageView) helper.getView(R.id.id_item_image));

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {

                // 已经选择过该图片
                if (mSelectedImage.contains(mDirPath + "/" + item)) {
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.square);
                    mImageView.setColorFilter(null);
                } else {
                // 未选择该图片
                    if (mSelectedImage.size() == 4){
                        Toast.makeText(context,"最多只能添加4张图片",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSelectedImage.add(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.choosen);
                    mImageView.setColorFilter(Color.parseColor("#77000000"));
                }

            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }

    }

    public ArrayList<String> getSelectedImage() {
        return (ArrayList<String>) mSelectedImage;
    }
}
