package com.example.k.superbag2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.MyApplication;
import com.example.k.superbag2.R;
import com.example.k.superbag2.adapter.FirstpageAdapter;
import com.example.k.superbag2.adapter.FirstpageAdapter2;
import com.example.k.superbag2.adapter.MemoRecyclerAdapter;
import com.example.k.superbag2.bean.ItemBean;
import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.view.DividerItemDecoration;
import com.example.k.superbag2.view.GridDividerDecoration;
import com.example.k.superbag2.view.MyRecyclerView;
import com.example.k.superbag2.view.TypeDialog;
import com.example.k.superbag2.view.TypeListDialog;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Aersasi on 2016/8/16.
 */
public class SearchActivity extends BaseActivity {

    private Button bt_search;
    private String target;
    private List<ItemBean> list_diary;
    private List<MemoItem> list_memo;
    private FirstpageAdapter2 adapter_diary;
    private int READY_DIARY = 1;
    private int READY_MEMO = 2;
    private int NOT_READY = 0;
    private Message msg;
    private int type;
    private MyRecyclerView rv_search;
    private MemoRecyclerAdapter adapter_memo;
    private Button bt_back;
    private EditText et_search;
    private Intent get_intent;
    private String[] type_diary = {"内容","标签","天气","心情"};
    private String[] type_memo = {"内容","标题"};
    private String[] sort_search = {"时间逆序","时间顺序","内容逆序","内容顺序"};
    private String SORT_TYPE = "updateTime";
    private String SORT_TAG = "asc";
    private String SORT_CONTENT = "content";

    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //根据数据结构的不同，采用不同的adapter。
            if (msg.what == 1){
                Log.d("search","receive");
                adapter_diary = new FirstpageAdapter2(MyApplication.getContext(),list_diary);
                rv_search = (MyRecyclerView) findViewById(R.id.search_rv);
                rv_search.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                //添加分割线
                rv_search.addItemDecoration(new DividerItemDecoration(MyApplication.getContext(),DividerItemDecoration.VERTICAL_LIST));
                rv_search.setFocusable(false);
                rv_search.setItemAnimator(new DefaultItemAnimator());
                rv_search.setAdapter(adapter_diary);
                msg.what = NOT_READY;
            }else if (msg.what == 2){
                rv_search = (MyRecyclerView) findViewById(R.id.search_rv);
                rv_search.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                adapter_memo = new MemoRecyclerAdapter(MyApplication.getContext(),list_memo);
                rv_search.addItemDecoration(new GridDividerDecoration(MyApplication.getContext()));
                //设置默认动画
                rv_search.setItemAnimator(new DefaultItemAnimator());
                rv_search.setAdapter(adapter_memo);
                msg.what = NOT_READY;
            }
        }
    };
    private EditText type_from;
    private EditText type_sort;
    private TypeListDialog tld;
    private TypeListDialog tld_sort;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initAction();
    }
    private void initView(){
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_search);
        //为了进入activity时，不自动弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bt_back = (Button) findViewById(R.id.search_back);
        bt_search = (Button) findViewById(R.id.search_go);
        et_search = (EditText) findViewById(R.id.search_et);
        get_intent = getIntent();
        type = get_intent.getIntExtra("type",9);
        type_from = (EditText) findViewById(R.id.type_from);
        type_sort = (EditText) findViewById(R.id.type_sort);


    }

    private void initAction(){
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                finish();
            }
        });
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                target = et_search.getText().toString();
//                list_diary = DataSupport.where("content like ?","%"+target+"%").find(ItemBean.class);
//                msg = new Message();
//                msg.what = READY;
//                hd.sendMessage(msg);
                MyThread myThread = new MyThread();
                new Thread(myThread).start();
            }
        });
        type_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectType();
            }
        });
        type_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectSort();
            }
        });

    }
    private void showSelectSort(){
        if (tld_sort == null ){
                tld_sort = new TypeListDialog(SearchActivity.this, sort_search, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tld_sort.dialog.dismiss();
                        switch (i){
                            case 0:
                                type_sort.setText(sort_search[i]);
                                SORT_TYPE = "updateTime";
                                SORT_TAG = "asc";
                                break;
                            case 1:
                                type_sort.setText(sort_search[i]);
                                SORT_TYPE = "updateTime";
                                SORT_TAG = "desc";
                                break;
                            case 2:
                                type_sort.setText(sort_search[i]);
                                SORT_TYPE = "content";
                                SORT_TAG = "asc";
                                break;
                            case 3:
                                type_sort.setText(sort_search[i]);
                                SORT_TYPE = "content";
                                SORT_TAG = "desc";
                                break;
                            default:
                                break;
                        }
                    }
                });
            tld_sort.setTitleText("选择排序方式");
        }
        tld_sort.dialog.show();
    }
    private void showSelectType() {
        if(tld == null){
            //启动活动的intent携带了
            if (type == 0){
                tld = new TypeListDialog(SearchActivity.this,type_diary, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tld.dialog.dismiss();
                        switch (position){
                            case 0:
                                type_from.setText(type_diary[position]);
                                SORT_CONTENT = "content";
                                break;
                            case 1:
                                type_from.setText(type_diary[position]);
                                SORT_CONTENT = "tag1 or tag2 tag3";
                                break;
                            case 2:
                                type_from.setText(type_diary[position]);
                                SORT_CONTENT = "weather";
                                break;
                            case 3:
                                type_from.setText(type_diary[position]);
                                SORT_CONTENT = "feelings";
                                break;
                            default:
                                break;
                        }
                    }
                });
            }else {
                tld = new TypeListDialog(SearchActivity.this,type_memo, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tld.dialog.dismiss();
                        switch (position){
                            case 0:
                                type_from.setText(type_memo[position]);
                                SORT_CONTENT = "content";
                                break;
                            case 1:
                                type_from.setText(type_memo[position]);
                                SORT_CONTENT = "title";
                                break;
//                            case 2:
//                                type_from.setText(type_diary[position]);
//                                break;
//                            case 3:
//                                type_from.setText(type_diary[position]);
//                                break;
                            default:
                                break;
                        }
                    }
                });
            }
            tld.setTitleText("选择查询方式");

        }
        tld.dialog.show();
    }
    class MyThread implements Runnable{
        @Override
        public void run() {
            msg = new Message();
            if (type == 0){
                list_diary = DataSupport.where(SORT_CONTENT+" "+"like ?","%"+target+"%")
                        .order(SORT_TYPE+" "+SORT_TAG).find(ItemBean.class);
                msg.what = READY_DIARY;
                Log.d("search","click");
            }else {
                list_memo = DataSupport.where(SORT_CONTENT+" "+"like ?","%"+target+"%")
                        .order(SORT_TYPE+" "+SORT_TAG).find(MemoItem.class);
                msg.what = READY_MEMO;
            }
            hd.sendMessage(msg);
        }
    }
}
