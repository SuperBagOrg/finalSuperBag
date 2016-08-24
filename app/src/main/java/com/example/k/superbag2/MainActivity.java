package com.example.k.superbag2;

        import android.app.AlarmManager;
        import android.app.AlertDialog;
        import android.app.DatePickerDialog;
        import android.app.PendingIntent;
        import android.app.TimePickerDialog;
        import android.content.BroadcastReceiver;
        import android.content.ContentValues;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.graphics.Color;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.preference.PreferenceManager;
        import android.support.v4.content.LocalBroadcastManager;
        import android.support.v4.view.ViewPager;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.StaggeredGridLayoutManager;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.view.animation.AccelerateInterpolator;
        import android.view.animation.DecelerateInterpolator;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.TimePicker;
        import android.widget.Toast;
        import com.bumptech.glide.Glide;
        import com.example.k.superbag2.activity.BaseActivity;
        import com.example.k.superbag2.activity.EditActivity;
        import com.example.k.superbag2.activity.PreviewActivity;
        import com.example.k.superbag2.activity.SearchActivity;
        import com.example.k.superbag2.adapter.FirstpageAdapter2;
        import com.example.k.superbag2.adapter.MainPagerAdapter;
        import com.example.k.superbag2.adapter.MemoRecyclerAdapter;
        import com.example.k.superbag2.bean.EditData;
        import com.example.k.superbag2.bean.ItemBean;
        import com.example.k.superbag2.bean.MemoItem;
        import com.example.k.superbag2.others.Constant;
        import com.example.k.superbag2.others.IsReception;
        import com.example.k.superbag2.utils.AlarmUtils;
        import com.example.k.superbag2.utils.DialogUtils;
        import com.example.k.superbag2.utils.GetImageUtils;
        import com.example.k.superbag2.utils.GetTime;
        import com.example.k.superbag2.utils.SaveUtils;
        import com.example.k.superbag2.view.DividerItemDecoration;
        import com.example.k.superbag2.view.GridDividerDecoration;
        import com.example.k.superbag2.view.HidingScrollListener;
        import com.example.k.superbag2.view.MyRecyclerView;
        import com.example.k.superbag2.view.MyScrollview;
        import com.flyco.animation.Attention.Tada;
        import com.flyco.animation.ZoomExit.ZoomOutExit;
        import com.flyco.dialog.listener.OnBtnClickL;
        import com.flyco.dialog.widget.NormalDialog;
        import com.nineoldandroids.view.ViewHelper;
        import org.litepal.crud.DataSupport;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Collections;
        import java.util.List;
        import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener
        , View.OnClickListener {

    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private Button mainDiaryBT, mainMemoBT;
    private TextView mainDiaryTV, mainMemoTV;
    private LinearLayout mainDiaryLL, mainMemoLL, mainBottomLL;
    private Toolbar toolbar;
    private TextView allDayTV,allDiaryTV,allPicTV;
    //diary
    private ImageView fPBackgroundIV, fPHeadIconIV;
    private TextView fPSummaryTV;
    private ListView fPListView;
    //----
    private MyRecyclerView fpRecyclerView;
    private MyScrollview scrollview;

    //    private FirstpageAdapter diaryAdapter;
    private FirstpageAdapter2 diaryAdapter;
    private List<ItemBean> itemBeanList;
    View diaryView;

    //memo
    private RecyclerView recyclerView;
    View memoView;

    private List<View> viewList = new ArrayList<>();
    private MemoRecyclerAdapter memoRecyclerAdapter;

    private int currentIndex = 0;

    //用于新建备忘时
    private boolean isAlarm = false;
    private boolean isSound = false;
    private boolean isShake = false;
    private String alarmTime = "";
    private String alarmTimeShow = "";

    //本地广播
    public LocalReceiver localReceiver;
    public static LocalBroadcastManager localBroadcastManager;
    public IntentFilter intentFilter;
    private String[] res;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置没有标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Log.d("现在时间是：",System.currentTimeMillis()+"");
        //第一：默认初始化
        Bmob.initialize(this, "8021c71fc35906b67dc7a4a0f64ef5da");
        initView();
        setListener();
        setPager();
        setDiaryView();
        setMemoView();
        initEvents();
        initLocalReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    private void initLocalReceiver(){
        localReceiver = new LocalReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction("dialog.changed");
        intentFilter.addAction("local.broadcast");
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }
    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainDiaryBT = (Button) findViewById(R.id.main_diary_BT);
        mainDiaryTV = (TextView) findViewById(R.id.main_diary_TV);
        mainDiaryLL = (LinearLayout) findViewById(R.id.main_bottom_diary_LL);
        mainMemoBT = (Button) findViewById(R.id.main_memo_BT);
        mainMemoTV = (TextView) findViewById(R.id.main_memo_TV);
        mainMemoLL = (LinearLayout) findViewById(R.id.main_bottom_memo_LL);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.getBackground().setAlpha(50);
        toolbar.setTitle("SuperBag");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.main_viewPager);

        mainBottomLL = (LinearLayout) findViewById(R.id.main_bottom_ll);
    }

    private void setListener() {
        mainDiaryLL.setOnClickListener(this);
        mainMemoLL.setOnClickListener(this);
        mainDiaryBT.setOnClickListener(this);
        mainMemoBT.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                //根据页面的不同，新建不同项目
                if (viewPager.getCurrentItem() == 0) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                } else {
                    addMemo(0, null, -1);
                }
                break;
            case R.id.action_search:
                Intent search_intent = new Intent(this,SearchActivity.class);
                search_intent.putExtra("type",viewPager.getCurrentItem());
                startActivity(search_intent);
                break;
        }
        return true;
    }

    private void setPager() {
        diaryView = LayoutInflater.from(this).inflate(R.layout.view_firstpage, null);
        viewList.add(diaryView);

        memoView = LayoutInflater.from(this).inflate(R.layout.memo_list, null);
        viewList.add(memoView);
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * 显示日记界面
     */
    private void setDiaryView() {
        fPBackgroundIV = (ImageView) diaryView.findViewById(R.id.top_background);
        fPHeadIconIV = (ImageView) diaryView.findViewById(R.id.top_head_icon);
        fPSummaryTV = (TextView) diaryView.findViewById(R.id.top_summary);
        fpRecyclerView = (MyRecyclerView) diaryView.findViewById(R.id.first_page_rv);
        scrollview = (MyScrollview) diaryView.findViewById(R.id.fp_scroll_view);
        //日记总览
        allDayTV = (TextView)diaryView.findViewById(R.id.fp_all_day);
        allDiaryTV = (TextView)diaryView.findViewById(R.id.fp_all_diary);
        allPicTV = (TextView)diaryView.findViewById(R.id.fp_all_pic);

        fpRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //添加分割线
        fpRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL_LIST));

        initPicAndSummary();
        initDiaryListView();
    }

    private void initPicAndSummary(){
        //设置头像背景
        Uri headUri = GetImageUtils.getUri(this, Constant.HEAD_ICON_URI);
        if (!headUri.toString().equals("")){
            Glide.with(MainActivity.this)
                    .load(headUri)
                    .asBitmap()
                    .into(fPHeadIconIV);
        }

        Uri bgUri = GetImageUtils.getUri(this,Constant.BACKGROUND_URI);
        if (!bgUri.toString().equals("")){
            Glide.with(MainActivity.this)
                    .load(bgUri)
                    .asBitmap()
                    .into(fPBackgroundIV);
        }

//        itemBeanList = new ArrayList<>();
        itemBeanList = DataSupport.findAll(ItemBean.class);
        if (itemBeanList.isEmpty()){

        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String installTime = sp.getString(Constant.INSTALL_TIME,"2016-08-15");
        GetTime gt = new GetTime();
        String nowTime = gt.getYear()+"-"+gt.getMonth()+"-"+gt.getDay();
        String betweenDay = gt.getBetweenDay(installTime,nowTime);
        allDayTV.setText(betweenDay);
        allDiaryTV.setText(itemBeanList.size()+"");
        long picNum = 0;
        for (int i = 0; i < itemBeanList.size(); i++){
            picNum += itemBeanList.get(i).getPicNum();
        }
        allPicTV.setText(picNum+"");
    }

    private void initDiaryListView(){
        itemBeanList = new ArrayList<>();
        itemBeanList = DataSupport.findAll(ItemBean.class);
        Collections.reverse(itemBeanList);//反转列表
        diaryAdapter = new FirstpageAdapter2(MainActivity.this,itemBeanList);
        fpRecyclerView.setAdapter(diaryAdapter);
        fpRecyclerView.setFocusable(false);
        fpRecyclerView.setItemAnimator(new DefaultItemAnimator());
        diaryAdapter.notifyDataSetChanged();

        diaryAdapter.setOnItemClickListener(new FirstpageAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
                //进入preViewActivity。携带当前diary的updateTime信息
                intent.putExtra(Constant.LINE_INDEX, itemBeanList.get(position).getUpdateTime());
                Log.d("edit"," :"+Constant.LINE_INDEX+itemBeanList.get(position).getUpdateTime());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                final NormalDialog dialog = new NormalDialog(MainActivity.this);
                Log.d("delete","position"+position);
                dialog.isTitleShow(false)//
                        .bgColor(Color.parseColor("#383838"))//
                        .cornerRadius(5)//
                        .content("确定删除这篇日记?")//
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
                dialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        Log.d("delete","itemBeanList.get(position).getUpdateTime()"+position+" "+itemBeanList.get(position).getDayTime());
                        String target = String.valueOf(itemBeanList.get(position).getUpdateTime());
                        DataSupport.deleteAll(ItemBean.class,"updateTime = ?",target);
                        DataSupport.deleteAll(EditData.class,"updateTime = ?",target);
                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        diaryAdapter.removeData(DataSupport.count(ItemBean.class) - position);
                        setDiaryView();
                        dialog.dismiss();
                    }
                });

            }
        });

        fpRecyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
                Log.d("隐藏执行","---------");
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        /*scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (i3 - i1 > 50){
                    showViews();
                }
                if (i3 - i1 < -50){
                    hideViews();
                }
            }
        });*/
    }

    private void setMemoView() {
        recyclerView = (RecyclerView) memoView.findViewById(R.id.memo_list_recyclerView);
        //必须设置，设置为StaggeredGridL...即为瀑布流布局
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        final List<MemoItem> memoLists = DataSupport.findAll(MemoItem.class);
        Collections.reverse(memoLists);

        memoRecyclerAdapter = new MemoRecyclerAdapter(MainActivity.this, memoLists);
        recyclerView.addItemDecoration(new GridDividerDecoration(this));
        recyclerView.setAdapter(memoRecyclerAdapter);
        //设置默认动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置点击事件
        memoRecyclerAdapter.setOnItemClickListener(new MemoRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //查看
                previewMemo(position,memoLists.get(position).getUpdateTime());
            }

            //长按，删除
            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("删除此条备忘？")
                        .setCancelable(true)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DataSupport.deleteAll(MemoItem.class,"updateTime = ?",""+memoLists.get(position).getUpdateTime());
                                memoRecyclerAdapter.removeItem(position);

                                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog dialog = builder.create();
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.windowAnimations = R.style.fade_in_out;
                dialog.show();
            }
        });

        recyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }

    private void hideViews() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        //----
        mainBottomLL.setVisibility(View.GONE);
    }

    private void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mainBottomLL.setVisibility(View.VISIBLE);
    }

    //参数为所点击的项在列表的位置
    private void previewMemo(final int i, final long updateTime) {
        View preview = LayoutInflater.from(MainActivity.this).inflate(R.layout.preview_memo, null);
        TextView preTitle, preContent, preDate,preAlarm;
        TextView preDelete, preEdit;

        preTitle = (TextView) preview.findViewById(R.id.pre_memo_title_tv);
        preContent = (TextView) preview.findViewById(R.id.pre_memo_content_tv);
        preAlarm = (TextView) preview.findViewById(R.id.pre_memo_alarm_tv);
        preDelete = (TextView) preview.findViewById(R.id.pre_memo_delete_bt);
        preEdit = (TextView) preview.findViewById(R.id.pre_memo_edit_bt);
        preDate = (TextView) preview.findViewById(R.id.pre_memo_alarm_date);
        final AlertDialog preMemoDialog = new AlertDialog.Builder(MainActivity.this).create();
        preMemoDialog.setView(preview);
        preMemoDialog.setCancelable(true);

        //避免数据库为空时设置数据
        if (DataSupport.findAll(MemoItem.class).size() == 0){
            return;
        }
        //设置数据
        final MemoItem memoItem = DataSupport.where("updateTime = ?",""+updateTime).find(MemoItem.class).get(0);
        preTitle.setText(memoItem.getTitle());
        preContent.setText(memoItem.getContent());
        preDate.setText(memoItem.getEditTime());
        if (memoItem.isAlarm()) {
            String str = memoItem.getAlarmTimeShow();
            if (memoItem.isSound()) {
                str = str + "\n提示音";
            }
            if (memoItem.isShake()) {
                str = str + "  震动";
            }
            preAlarm.setText(str);
        } else {
            preAlarm.setText("无");
        }

        preDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 删除
                memoRecyclerAdapter.removeItem(i);
                DataSupport.deleteAll(MemoItem.class,"updateTime = ?",""+updateTime);

                //搜索看有没设置提醒时间的memo，有：取最近的设置提醒。
                AlarmUtils alarmUtils = new AlarmUtils();
                alarmUtils.setAlarm();

                preMemoDialog.dismiss();
                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        preEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemo(1, memoItem, i);
                preMemoDialog.dismiss();
            }
        });
        DialogUtils.setDialog(MainActivity.this, preMemoDialog, 2, 3,R.style.fade_in_out);
        preMemoDialog.show();
    }

    /**
     * 新建备忘
     *
     * @param temp  为 0，表示新建一条备忘
     *              为 1，表示编辑一条现有的备忘
     * @param index 表示在列表中的位置
     */
    private void addMemo(final int temp, final MemoItem item, final int index) {

        final AlertDialog addMemoDialog = new AlertDialog.Builder(MainActivity.this).create();
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_memo, null);
        addMemoDialog.setView(dialogView);
        addMemoDialog.setCancelable(true);

        final EditText titleET = (EditText) dialogView.findViewById(R.id.add_memo_title_et);
        final EditText contentET = (EditText) dialogView.findViewById(R.id.add_memo_content_et);
        TextView dateTV = (TextView) dialogView.findViewById(R.id.add_memo_date_tv);
        CheckBox alarmCK = (CheckBox) dialogView.findViewById(R.id.add_memo_setAlarm_ck);
        final TextView alarmTimeTV = (TextView) dialogView.findViewById(R.id.add_memo_alarmTime_tv);
        final Switch soundSC = (Switch) dialogView.findViewById(R.id.add_memo_sound_sc);
        final Switch shakeSC = (Switch) dialogView.findViewById(R.id.add_memo_shake_sc);
        TextView OKBT = (TextView) dialogView.findViewById(R.id.add_memo_ok_bt);
        alarmTimeTV.setText(new GetTime().getSpecificTime());
        dateTV.setText(new GetTime().getSpecificTime());

        //编辑：初始化数据
        if (temp == 1) {
            titleET.setText(item.getTitle());
            contentET.setText(item.getContent());
            alarmCK.setChecked(item.isAlarm());
            soundSC.setChecked(item.isAlarm());
            shakeSC.setChecked(item.isShake());
            if (item.isAlarm()) {
                alarmTimeTV.setText(item.getAlarmTimeShow());
                alarmTimeTV.setClickable(true);
                soundSC.setClickable(true);
                shakeSC.setClickable(true);
            }
        }

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        alarmCK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()) {
                    case R.id.add_memo_setAlarm_ck:
                        isAlarm = b;
                        soundSC.setClickable(b);
                        alarmTimeTV.setClickable(b);
                        shakeSC.setClickable(b);
                        if (!b) {
                            soundSC.setChecked(b);
                            shakeSC.setChecked(b);
                        }
                        break;
                }
            }
        });
        soundSC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSound = b;
            }
        });
        shakeSC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isShake = b;
            }
        });

        alarmTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(alarmTimeTV);

            }
        });
        OKBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (c == null){
                    alarmTimeShow = alarmTimeTV.getText().toString();
                    alarmTime = ""+System.currentTimeMillis();
                }else {
                    alarmTime = c.getTimeInMillis()+"";
                    alarmTimeShow = c.get(Calendar.YEAR)+"-"+ c.get(Calendar.MONTH)+"-"+ c.get(Calendar.DAY_OF_MONTH)+"  "+ c.get(Calendar.HOUR_OF_DAY)+":"+ c.get(Calendar.MINUTE);
                }
                if (titleET.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "不能为空呦", Toast.LENGTH_SHORT).show();
                } else {
                    //新建时的保存
                    if (temp == 0) {
                        MemoItem memoItem = new MemoItem();
                        memoItem.setTitle(titleET.getText().toString());
                        memoItem.setContent(contentET.getText().toString());
                        memoItem.setAlarm(isAlarm);
                        memoItem.setSound(isSound);
                        memoItem.setShake(isShake);
                        memoItem.setAlarmTime(alarmTime);
                        memoItem.setEditTime(new GetTime().getSpecificTime());
                        memoItem.setAlarmTimeShow(alarmTimeShow);
                        GetTime gt = new GetTime();
                        String daytime = gt.getSpecificTime2Second();
                        memoItem.setUpdateTime(Long.parseLong(daytime));
                        memoItem.setEditTime(new GetTime().getSpecificTime());
                        memoRecyclerAdapter.addItem(0, memoItem, 0, -1,null);
                    } else {
                        //更新数据
//                        item.setTitle(titleET.getText().toString());
//                        item.setContent(contentET.getText().toString());
//                        item.setAlarm(isAlarm);
//                        item.setSound(isSound);
//                        item.setShake(isShake);
//                        String daytime = gt.getSpecificTime2Second();
//                        item.setUpdateTime(Long.parseLong(daytime));
//                        item.setAlarmTime(alarmTime);
//                        item.setAlarmTimeShow(alarmTimeShow);
//                        item.setEditTime(new GetTime().getSpecificTime());
                        // TODO 执行更新操作
                        ContentValues values = new ContentValues();
                        values.put("alarmTime",alarmTime);
                        values.put("alarmTimeShow",alarmTimeShow);
                        values.put("isAlarm",isAlarm ? "1":"0");
                        values.put("isShake",isShake ? "1":"0");
                        values.put("isSound",isSound ? "1":"0");
                        GetTime gt = new GetTime();
                        String daytime = gt.getSpecificTime2Second();
                        values.put("updateTime",Long.parseLong(daytime));
                        values.put("editTime",new GetTime().getSpecificTime());
                        values.put("title",titleET.getText().toString());
                        values.put("content",contentET.getText().toString());
                        values.put("editTime",new GetTime().getSpecificTime());

                        memoRecyclerAdapter.addItem(0, item, 1, index,values);
                        setMemoView();
                    }
                    //搜索看有没设置提醒时间的memo，有：取最近的设置提醒。
                    AlarmUtils alarmUtils = new AlarmUtils();
                    alarmUtils.setAlarm();
                }
                addMemoDialog.dismiss();
            }
        });
        addMemoDialog.show();
    }


    //设置提醒时间
    //返回值数组，第一个是具体时间（数字）,第二个是用于显示的时间
    private void setTime(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        c = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //要把timePicker写在前面，才会先显示datePicker,原因不知。。。
        //原因是：先show TimePicker。再show DataPicker。。。。覆盖了。。
        TimePickerDialog timePicker = new TimePickerDialog(this, 0,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        //得到的时间是24小时制
                        c.set(Calendar.HOUR_OF_DAY, i);
                        c.set(Calendar.MINUTE, i1);
                        textView.setText(c.get(Calendar.YEAR)+"-"+ c.get(Calendar.MONTH)+"-"+ c.get(Calendar.DAY_OF_MONTH)+"  "+ c.get(Calendar.HOUR_OF_DAY)+":"+ c.get(Calendar.MINUTE));
                        Toast.makeText(MainActivity.this, "提醒设置成功", Toast.LENGTH_SHORT).show();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        DatePickerDialog datePicker = new DatePickerDialog(this, 0, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                c.set(i, i1, i2);
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        timePicker.show();
        datePicker.show();
    }

    //设置抽屉。。
    private void initEvents() {
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = drawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                float leftScale = 1 - 0.2f * scale;
                //设置左边菜单滑动后占据屏幕大小
                ViewHelper.setScaleX(mMenu, leftScale);
                ViewHelper.setScaleY(mMenu, leftScale);
                //菜单透明度
                ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                //内容界面的偏移量
                ViewHelper.setTranslationX(mContent,
                        mMenu.getMeasuredWidth() * (1 - scale));
                ViewHelper.setPivotX(mContent, 0);
                ViewHelper.setPivotY(mContent,
                        mContent.getMeasuredHeight() / 2);
                //设置内容区域点击无效
                mContent.invalidate();

                ViewHelper.setScaleX(mContent, rightScale);
                ViewHelper.setScaleY(mContent, rightScale);

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        resetUI(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_bottom_diary_LL:
            case R.id.main_diary_BT:
                resetUI(0);
                break;
            case R.id.main_bottom_memo_LL:
            case R.id.main_memo_BT:
                resetUI(1);
                break;
        }
    }

    private void resetUI(int index) {
        if (currentIndex == index)
            return;
        currentIndex = index;
        viewPager.setCurrentItem(index);
        if (index == 0) {
            //设置toolbar的新建图标
//            toolbar.getMenu().getItem(1).setIcon(getResources().getDrawable(R.drawable.edit_black));
            mainDiaryBT.setBackground(getResources().getDrawable(R.drawable.diary_blue));
            mainDiaryTV.setTextColor(getResources().getColor(R.color.new_primary));
            mainMemoBT.setBackground(getResources().getDrawable(R.drawable.memo_black));
            mainMemoTV.setTextColor(getResources().getColor(R.color.gray));
        } else {
//            toolbar.getMenu().getItem(1).setIcon(getResources().getDrawable(R.drawable.add_black_round));
            mainDiaryBT.setBackground(getResources().getDrawable(R.drawable.diary_black));
            mainDiaryTV.setTextColor(getResources().getColor(R.color.gray));
            mainMemoBT.setBackground(getResources().getDrawable(R.drawable.memo_blue));
            mainMemoTV.setTextColor(getResources().getColor(R.color.new_primary));
        }
    }

    /**
     * 为了解决ListView在ScrollView中只能显示一行数据的问题
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public void onStart() {
        initView();
        //动态刷新页面
        setDiaryView();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //毫秒
    private long firstClickTime = 0;
    @Override
    public void onBackPressed() {
        if (firstClickTime < 0){
            Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
            firstClickTime = System.currentTimeMillis();
        } else {
            long secondClickTime = System.currentTimeMillis();
            if (secondClickTime - firstClickTime < 1000){
                finish();
            } else {
                firstClickTime = secondClickTime;
                Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //为了退出登录后，在login界面点击返回直接退出应用，而不是再次返回首页
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.QUIT_LOGIN,false)){
            finish();
        }
    }

    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("local.broadcast")){
                initDiaryListView();
                Toast.makeText(MyApplication.getContext(),"下载完成",Toast.LENGTH_SHORT).show();
            }else if (intent.getAction().equals("dialog.changed")){
                if (!IsReception.isApplicationBroughtToBackground(MyApplication.getContext())){
                    initView();
                }
            }
        }
    }
}
