package com.example.k.superbag2.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.k.superbag2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by K on 2016/7/30.
 */
public class NumLockActivity extends Activity {

    private Button deleteBT,num0BT,cancleBT;
    private View dot1,dot2,dot3,dot4;

    private List<Map<String,Object>> itemLists;

    int[] nums = new int[]{1,2,3,4,5,6,7,8,9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_lock);

    }


}
