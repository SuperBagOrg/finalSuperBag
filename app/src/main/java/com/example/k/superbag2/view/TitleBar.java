package com.example.k.superbag2.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;

import org.w3c.dom.Text;

/**
 * Created by K on 2016/8/14.
 */
public class TitleBar extends RelativeLayout{

    private Button leftBT,rightBT;
    private TextView title;
    private RelativeLayout relativeLayout;

    private RelativeLayout.LayoutParams leftParams,rightParams,midParams;

    private Context context;
    private Drawable leftBTBackground,rightBTBackground;
    private int barBackground;
    private String titleText;
    private float titleSize;
    private int titleColor;

    private topbarClickListener listener;

    public TitleBar(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.TopBar);
        leftBTBackground = ta.getDrawable(R.styleable.TopBar_leftButtonBG);
        rightBTBackground = ta.getDrawable(R.styleable.TopBar_rightButtonBG);
        barBackground = ta.getColor(R.styleable.TopBar_barBackground,0);
        titleText = ta.getString(R.styleable.TopBar_title);
        titleSize = ta.getDimension(R.styleable.TopBar_titleTextSize,10);
        titleColor = ta.getColor(R.styleable.TopBar_titleTextColor,0);

        ta.recycle();

        init();
    }

    private void init(){
        leftBT = new Button(context);
        rightBT = new Button(context);
        title = new TextView(context);
        relativeLayout = new RelativeLayout(context);

        leftBT.setBackground(leftBTBackground);
        rightBT.setBackground(rightBTBackground);
        title.setText(titleText);
        title.setTextSize(titleSize);
        title.setTextColor(titleColor);
        title.setGravity(Gravity.CENTER);
        relativeLayout.setBackgroundColor(barBackground);


        leftBT.setPadding(dip2px(15),0,0,0);
        rightBT.setPadding(0,0,dip2px(15),0);


        leftParams = new RelativeLayout.LayoutParams(dip2px(30),dip2px(30));
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL,TRUE);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftBT,leftParams);

        rightParams = new LayoutParams(dip2px(30),dip2px(30));
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL,TRUE);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightBT,rightParams);

        midParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        midParams.addRule(RelativeLayout.CENTER_HORIZONTAL,TRUE);
        addView(title,midParams);

        //响应点击事件
        leftBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.leftClick();
            }
        });

        rightBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.rightClick();
            }
        });
    }

    public interface topbarClickListener{
        void leftClick();
        void rightClick();
    }

    public void setOnTopbarClickListener(topbarClickListener listener){
        this.listener = listener;
    }

    public void setLeftBTVisiable(boolean visiable){
        if (visiable) {
            leftBT.setVisibility(VISIBLE);
        } else {
            leftBT.setVisibility(GONE);
        }
    }

    public void setRightBTVisiable(boolean visiable){
        if (visiable){
            rightBT.setVisibility(VISIBLE);
        } else {
            rightBT.setVisibility(GONE);
        }
    }

    /** dip转换px */
    private int dip2px(int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
}
