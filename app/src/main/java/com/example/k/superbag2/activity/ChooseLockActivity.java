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

/**
 * Created by K on 2016/8/1.
 */
public class ChooseLockActivity extends AppCompatActivity implements View.OnClickListener{

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.no_lock_rl:
                MyApplication.setHasSetLock(false);
                break;
            case R.id.num_lock_rl:
                MyApplication.setLockStyle(Constant.NUMBERLOCK);
                Intent intent = new Intent(ChooseLockActivity.this,NumLockActivity.class);
                startActivity(intent);
                break;
            case R.id.pic_lock_rl:
                MyApplication.setLockStyle(Constant.NINEBLOCKLOCK);
                Intent intent1 = new Intent(ChooseLockActivity.this,ScreenLockActivity.class);
                intent1.putExtra("setting",true);
                startActivity(intent1);
                break;
        }
    }
}
