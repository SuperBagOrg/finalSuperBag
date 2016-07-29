package com.example.k.superbag2;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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

import com.example.k.superbag2.activity.AlarmActivity;
import com.example.k.superbag2.activity.EditActivity;
import com.example.k.superbag2.activity.PreviewActivity;
import com.example.k.superbag2.adapter.FirstpageAdapter;
import com.example.k.superbag2.adapter.MainPagerAdapter;
import com.example.k.superbag2.adapter.MemoRecyclerAdapter;
import com.example.k.superbag2.bean.ItemBean;
import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.GetImageUtils;
import com.example.k.superbag2.utils.GetTime;
import com.nineoldandroids.view.ViewHelper;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
,View.OnClickListener{

    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private Button mainDiaryBT,mainMemoBT;
    private TextView mainDiaryTV,mainMemoTV;
    private LinearLayout mainDiaryLL,mainMemoLL;
    private Toolbar toolbar;
    //diary
    private ImageView fPBackgroundIV,fPHeadIconIV;
    private TextView fPSummaryTV;
    private ListView fPListView;
    //memo
    private RecyclerView recyclerView;

    private List<View> viewList = new ArrayList<>();

    private int currentIndex = 0;

    //用于新建备忘时
    private boolean isAlarm = false;
    private boolean isSound = false;
    private boolean isShake = false;
    private String alarmTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置没有标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
        initEvents();
        initDataBase();
        setPager();

    }

    //初始化数据库 2016/7/27
    private void initDataBase(){
        SQLiteDatabase myDatabase = Connector.getDatabase();
    }

    private void initView(){
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mainDiaryBT = (Button)findViewById(R.id.main_diary_BT);
        mainDiaryTV = (TextView)findViewById(R.id.main_diary_TV);
        mainDiaryLL = (LinearLayout)findViewById(R.id.main_bottom_diary_LL);
        mainMemoBT = (Button)findViewById(R.id.main_memo_BT);
        mainMemoTV = (TextView)findViewById(R.id.main_memo_TV);
        mainMemoLL = (LinearLayout)findViewById(R.id.main_bottom_memo_LL);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(50);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.main_viewPager);

    }

    private void setListener(){
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
        switch (item.getItemId()){
            case R.id.action_edit:
                if (viewPager.getCurrentItem() == 0) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    startActivity(intent);
                } else {
                    addMemo();
                }
                break;
            case R.id.action_search:

                break;
        }
        return true;
    }

    private void setPager(){
        setDiaryView();
        setMemoView();
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
    }

    private void setDiaryView(){
        View diaryView = LayoutInflater.from(this).inflate(R.layout.fragment_firstpage,null);
        viewList.add(diaryView);
        fPBackgroundIV = (ImageView)diaryView.findViewById(R.id.top_background);
        fPHeadIconIV = (ImageView)diaryView.findViewById(R.id.top_head_icon);
        fPSummaryTV = (TextView)diaryView.findViewById(R.id.top_summary);
        fPListView = (ListView)diaryView.findViewById(R.id.firstPage_lv);

        Bitmap headBM = GetImageUtils.getBMFromUri(this,"headIconUri");
        if (headBM != null) {
            fPHeadIconIV.setImageBitmap(headBM);
            Log.d("设置自定义头像成功","");
        } else {
            fPHeadIconIV.setImageResource(R.drawable.pic4);
        }

        Bitmap bgBM = GetImageUtils.getBMFromUri(this,"bgUri");
        if (bgBM != null) {
            fPBackgroundIV.setImageBitmap(bgBM);
        } else {
            fPBackgroundIV.setImageResource(R.drawable.pic6);
        }

        initDiaryListView();
    }

    private void initDiaryListView(){

        final List<ItemBean> itemBeanList = DataSupport.findAll(ItemBean.class);
        Collections.reverse(itemBeanList);//反转列表
        Log.d("列表长度：",itemBeanList.size()+"");
        FirstpageAdapter adapter = new FirstpageAdapter(this,R.layout.item_fp_1pic,itemBeanList);
        fPListView.setAdapter(adapter);
        //----------------
        setListViewHeightBasedOnChildren(fPListView);
        adapter.notifyDataSetChanged();

        fPListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //得到数据库中的行号
                int index = i;
                Log.d("索引是",index+"");
                Intent intent = new Intent(MainActivity.this,PreviewActivity.class);
                intent.putExtra(Constant.LINE_INDEX,index);
                startActivity(intent);
            }
        });

        fPListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        fPListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //缺少分割线
    private void setMemoView(){
        View memoView = LayoutInflater.from(this).inflate(R.layout.memo_list,null);
        viewList.add(memoView);
        recyclerView = (RecyclerView) memoView.findViewById(R.id.memo_list_recyclerView);
        //必须设置，设置为StaggeredGridL...即为瀑布流布局
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        try {
            List<MemoItem> memoLists = DataSupport.findAll(MemoItem.class);
            MemoRecyclerAdapter memoRecyclerAdapter = new MemoRecyclerAdapter(MainActivity.this,memoLists);

            recyclerView.setAdapter(memoRecyclerAdapter);
            //设置默认动画
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }catch (Exception e){

        }



    }

    //新建备忘录
    private void addMemo(){
        final AlertDialog addMemoDialog = new AlertDialog.Builder(MainActivity.this).create();
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_memo,null);
        addMemoDialog.setView(dialogView);
        addMemoDialog.setCancelable(true);

        final EditText titleET = (EditText)dialogView.findViewById(R.id.add_memo_title_et);
        final EditText contentET = (EditText)dialogView.findViewById(R.id.add_memo_content_et);
        TextView dateTV = (TextView)dialogView.findViewById(R.id.add_memo_date_tv);
        CheckBox alarmCK = (CheckBox)dialogView.findViewById(R.id.add_memo_setAlarm_ck);
        TextView alarmTimeTV = (TextView)dialogView.findViewById(R.id.add_memo_alarmTime_tv);
        Switch soundSC = (Switch)dialogView.findViewById(R.id.add_memo_sound_sc);
        Switch shakeSC = (Switch)dialogView.findViewById(R.id.add_memo_shake_sc);
        Button OKBT = (Button)dialogView.findViewById(R.id.add_memo_ok_bt);


        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        alarmCK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()){
                    case R.id.add_memo_setAlarm_ck:
                        isAlarm = b;
                        break;
                    case R.id.add_memo_sound_sc:
                        isSound = b;
                        break;
                    case R.id.add_memo_shake_sc:
                        isShake = b;
                        break;
                }
            }
        });
        alarmTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmTime = setTime();
            }
        });
        OKBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleET.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this,"不能为空呦",Toast.LENGTH_SHORT).show();
                } else {
                    MemoItem memoItem = new MemoItem();
                    memoItem.setTitle(titleET.getText().toString());
                    memoItem.setContent(contentET.getText().toString());
                    memoItem.setAlarm(isAlarm);
                    memoItem.setSound(isSound);
                    memoItem.setShake(isShake);
                    memoItem.setAlarmTime(alarmTime);
                    memoItem.setEditTime(new GetTime().getSpecificTime());
                    memoItem.save();
                }
                addMemoDialog.dismiss();
            }
        });
        addMemoDialog.show();
    }

    //设置提醒时间
    private String setTime(){
        Calendar calendar = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        //要把timePicker写在前面，才会先显示datePicker,原因不知。。。
        TimePickerDialog timePicker = new TimePickerDialog(this, 0,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        c.set(Calendar.HOUR_OF_DAY,i);
                        c.set(Calendar.MINUTE,i1);
                        Intent intent = new Intent(MainActivity.this,AlarmActivity.class);
                        PendingIntent pt = PendingIntent.getActivity(MainActivity.this,0,intent,0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pt);
                        Toast.makeText(MainActivity.this,"提醒设置成功",Toast.LENGTH_SHORT).show();
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePicker.show();

        DatePickerDialog datePicker = new DatePickerDialog(this, 0,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        c.set(i,i1,i2);
                    }
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();

        return c.getTimeInMillis()+"";
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
        switch (view.getId()){
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

    private void resetUI(int index){
        if (currentIndex == index)
            return;
        currentIndex = index;
        viewPager.setCurrentItem(index);
        if (index == 0){
            //设置toolbar的新建图标
//            toolbar.getMenu().getItem(1).setIcon(getResources().getDrawable(R.drawable.edit_black));
            mainDiaryBT.setBackground(getResources().getDrawable(R.drawable.diary_blue));
            mainDiaryTV.setTextColor(getResources().getColor(R.color.light_blue));
            mainMemoBT.setBackground(getResources().getDrawable(R.drawable.memo_black));
            mainMemoTV.setTextColor(getResources().getColor(R.color.gray));
        } else {
//            toolbar.getMenu().getItem(1).setIcon(getResources().getDrawable(R.drawable.add_black_round));
            mainDiaryBT.setBackground(getResources().getDrawable(R.drawable.diary_black));
            mainDiaryTV.setTextColor(getResources().getColor(R.color.gray));
            mainMemoBT.setBackground(getResources().getDrawable(R.drawable.memo_blue));
            mainMemoTV.setTextColor(getResources().getColor(R.color.light_blue));
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
        initDiaryListView();
        super.onStart();
    }
}
