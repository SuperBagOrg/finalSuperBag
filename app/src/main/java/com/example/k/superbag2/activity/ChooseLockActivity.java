package com.example.k.superbag2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.SaveUtils;

/**
 * Created by K on 2016/8/1.
 */
public class ChooseLockActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout noLockRL,numLockRL,picLockRL;
    private ImageView noLockIV,numLockIV,picLockIV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_lock);
        initView();
    }
    private void initView(){
        noLockRL = (RelativeLayout)findViewById(R.id.no_lock_rl);
        noLockIV = (ImageView)findViewById(R.id.no_lock_right);
        numLockRL = (RelativeLayout)findViewById(R.id.num_lock_rl);
        numLockIV = (ImageView)findViewById(R.id.num_lock_right);
        picLockRL = (RelativeLayout)findViewById(R.id.pic_lock_rl);
        picLockIV = (ImageView)findViewById(R.id.pic_lock_right);
        noLockRL.setOnClickListener(this);
        numLockRL.setOnClickListener(this);
        picLockRL.setOnClickListener(this);
        if (!SaveUtils.getHasSetLock()){
            setNoLockRL();
        }else if (SaveUtils.getLockStyle()){
            setNumLockRL();
        }else {
            setPicLockRL();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.no_lock_rl:
                Intent intent_nolock;
                if (SaveUtils.getHasSetLock()) {
                    if (SaveUtils.getLockStyle()){
                        intent_nolock = new Intent(this, NumLockActivity.class);
                    }else {
                        intent_nolock = new Intent(this, ScreenLockActivity.class);
                    }
                    intent_nolock.putExtra("lock",true);
                    startActivity(intent_nolock);
                }
                if (!SaveUtils.getHasSetLock()){
                    setNoLockRL();
                }
                finish();
                break;
            case R.id.num_lock_rl:
                setNumLockRL();
                Intent intent = new Intent(ChooseLockActivity.this,NumLockActivity.class);
                intent.putExtra("set",true);
                startActivity(intent);
                break;
            case R.id.pic_lock_rl:
                setPicLockRL();
                Intent intent1 = new Intent(ChooseLockActivity.this,ScreenLockActivity.class);
                intent1.putExtra("setting",true);
                startActivity(intent1);
                break;
        }
    }
    private void setNoLockRL(){
        noLockIV.setVisibility(View.VISIBLE);
        numLockIV.setVisibility(View.GONE);
        picLockIV.setVisibility(View.GONE);
    }
    private void setNumLockRL(){
        noLockIV.setVisibility(View.GONE);
        numLockIV.setVisibility(View.VISIBLE);
        picLockIV.setVisibility(View.GONE);
    }
    private void setPicLockRL(){
        noLockIV.setVisibility(View.GONE);
        numLockIV.setVisibility(View.GONE);
        picLockIV.setVisibility(View.VISIBLE);
    }
}
