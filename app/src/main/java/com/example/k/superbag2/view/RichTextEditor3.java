package com.example.k.superbag2.view;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.DataImageView;
import com.example.k.superbag2.bean.EditData;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个富文本编辑器，给外部提供insertImage接口，添加的图片跟当前光标所在位置有关
 *
 * @author xmuSistone
 */
@SuppressLint({"NewApi", "InflateParams"})
public class RichTextEditor3 extends ScrollView {
    private static final int EDIT_PADDING = 10; // edittext常规padding是10dp
    private static final int EDIT_FIRST_PADDING_TOP = 10; // 第一个EditText的paddingTop值
    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private LayoutInflater inflater;
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private OnClickListener btnListener; // 图片右上角红叉按钮监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
//    private EditText lastFocusEdit; // 最近被聚焦的EditText
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
    private int editNormalPadding = 0; //
    private int disappearingImageIndex = 0;
    private Context context;

    private OnImageClickListener onImageClickListener = null;
    public interface OnImageClickListener{
        void onImageClick(int position);
    }
    public void setOnImageClickListener(OnImageClickListener clickListener){
        this.onImageClickListener = clickListener;
    }


    public RichTextEditor3(Context context) {
        this(context, null);
        this.context = context;
    }
    public RichTextEditor3(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RichTextEditor3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        this.context = context;

        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        allLayout.setBackgroundColor(Color.WHITE);
        setupLayoutTransitions();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(allLayout, layoutParams);


        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        editNormalPadding = dip2px(EDIT_PADDING);
        TextView tv = createTextView(dip2px(EDIT_FIRST_PADDING_TOP));
        allLayout.addView(tv, firstEditParam);
    }

    private TextView createTextView(int paddingTop){
        TextView textView = (TextView) inflater.inflate(R.layout.pre_textview,null);
        textView.setPadding(editNormalPadding, paddingTop, editNormalPadding, 0);
        return textView;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.pre_imageview, null);
        layout.setTag(viewTagIndex++);
        ImageView imageView = (ImageView) layout.findViewById(R.id.pre_imageView);

        return layout;
    }

    public void addTextViewAtIndex(final int index, String editStr) {

        TextView textView = createTextView(getResources()
                .getDimensionPixelSize(R.dimen.edit_padding_top));
        textView.setText(editStr);
        Log.d("插入文字：--",editStr);
        // 请注意此处，EditText添加、或删除不触动Transition动画
        allLayout.setLayoutTransition(null);
        allLayout.addView(textView, index);
        allLayout.setLayoutTransition(mTransitioner); // remove之后恢复transition动画
    }

    /**
     * 在特定位置添加ImageView
     */
    public void addImageViewAtIndex(final int index, Bitmap bmp,
                                    String imagePath) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout
                .findViewById(R.id.pre_imageView);
//        imageView.setImageBitmap(bmp);
        Glide.with(context)
                .load(imagePath)
                .asBitmap()
                .into(imageView);
        imageView.setAbsolutePath(imagePath);

        Log.d("bitmap高度：  宽度：",bmp.getHeight()+"----"+bmp.getWidth());
        // 调整imageView的高度
//        int imageHeight = getWidth() * bmp.getHeight() / bmp.getWidth();
//        Log.d("imageHeight是  ",imageHeight+"");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, bmp.getHeight());
        imageView.setLayoutParams(lp);

        // onActivityResult无法触发动画，此处post处理
        Log.d("插入图片一次，成功","------------");
//        allLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                allLayout.addView(imageLayout, index);
//            }
//        }, 200);
        allLayout.addView(imageLayout, index);
    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    public Bitmap getScaledBitmap(String filePath, int width) {
        Log.d("rich_edit------ ","getScaledBitmap(String filePath, int width)宽度是"+ width);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //采样率
        int sampleSize;
        /*if (width ==0){
            width = options.outWidth;
            Log.d("rich_edit------ ","activity.width"+width);
        }*/
//        sampleSize = options.outWidth > width ? options.outWidth / width + 1 : 1;
        sampleSize = calculateInSampleSize(options,400,400);

        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap == null){
            Log.d("得到的bitmap为空-","");
        }
        Log.d("bitmap 高度是 ",bitmap.getHeight()+"");
        return bitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        if (reqHeight == 0 || reqWidth == 0){
            return 1;
        }

        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d("本来的width是--",width+"");

        int res = 1;
        if (height > reqHeight || width > reqWidth){
            final int halfHeight = height / 2;
            final int halfWidth = height/ 2;

            while ((halfHeight / res) >= reqHeight && (halfWidth / res) >= reqWidth){
                res *= 2;
            }
        }
        Log.d("缩放是：  ",res+"");
        return res;
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        Log.d("rich_edit------ ","getRealFilePath(final Uri uri)");

        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        allLayout.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                    // transition动画结束，合并EditText
                    // mergeEditText();
                }
            }
        });
        mTransitioner.setDuration(300);
    }

    /**
     * dp和pixel转换
     *
     * @param dipValue dp值
     * @return 像素值
     */
    public int dip2px(float dipValue) {
        float m = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    //获取所有的imageView
    public List<ImageView> getImageViews() {
        List<ImageView> imageViews = new ArrayList<>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView
                        .findViewById(R.id.pre_imageView);
                imageViews.add(item);
            }
        }
        return imageViews;
    }
}
