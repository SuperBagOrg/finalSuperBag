package com.example.k.superbag2.view;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.ItemBean;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by K on 2016/8/3.
 */
public class LookPicDialog extends Dialog {

    private HackyViewPager pager;
    private TextView textView;
    private ItemBean itemBean;
    private Context context;
    private int position;

    public LookPicDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LookPicDialog(Context context, ItemBean itemBean,int position){
        this(context,R.style.Pic_Dialog);
        this.context = context;
        this.itemBean = itemBean;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_pic_view);

        pager = (HackyViewPager)findViewById(R.id.look_pic_viewpager);
        textView = (TextView)findViewById(R.id.look_pic_tv);
        textView.setText(position + "/" + itemBean.getPicNum());
        LookPicPagerAdapter adapter = new LookPicPagerAdapter();
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        pager.setCurrentItem(position-1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText(position+1 + "/" + itemBean.getPicNum());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    class LookPicPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return itemBean.getPicNum();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String s = itemBean.getPicList().get(position);
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageURI(Uri.parse(s));
/*            Glide.with(context)
                    .load(Uri.parse(s))
                    .asBitmap()
                    .into(photoView);*/
            photoView.setHorizontalScrollBarEnabled(true);
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    dismiss();
                }

                @Override
                public void onOutsidePhotoTap() {

                }
            });
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
