package com.example.k.superbag2.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.k.superbag2.MainActivity;
import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.utils.AlarmUtils;
import com.example.k.superbag2.utils.DialogUtils;
import com.example.k.superbag2.utils.GetTime;
import com.example.k.superbag2.utils.SaveUtils;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

/**
 * Created by Aersasi on 2016/8/24.
 */
public class PreviewMemo extends BaseActivity {

    private boolean isAlarm;
    private boolean isSound;
    private boolean isShake;
    private Calendar c;
    private String alarmTimeShow;
    private String alarmTime;

    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                Intent intent = new Intent("dialog.changed");
                MainActivity.localBroadcastManager.sendBroadcast(intent);
                finish();
            }
        }
    };
    private Message msg = new Message();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        previewMemo(SaveUtils.getAlarmTime());
        Log.d("last","create"+SaveUtils.getAlarmTime());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("last","start");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("last","resume");

    }

    private void previewMemo(final long updateTime) {
        View preview = LayoutInflater.from(PreviewMemo.this).inflate(R.layout.preview_memo, null);
        TextView preTitle, preContent, preDate,preAlarm;
        TextView preDelete, preEdit;

        preTitle = (TextView) preview.findViewById(R.id.pre_memo_title_tv);
        preContent = (TextView) preview.findViewById(R.id.pre_memo_content_tv);
        preAlarm = (TextView) preview.findViewById(R.id.pre_memo_alarm_tv);
        preDelete = (TextView) preview.findViewById(R.id.pre_memo_delete_bt);
        preEdit = (TextView) preview.findViewById(R.id.pre_memo_edit_bt);
        preDate = (TextView) preview.findViewById(R.id.pre_memo_alarm_date);
        final AlertDialog preMemoDialog = new AlertDialog.Builder(PreviewMemo.this).create();
        preMemoDialog.setView(preview);
        preMemoDialog.setCancelable(true);
        Log.d("last","create"+updateTime);

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
                DataSupport.deleteAll(MemoItem.class,"updateTime = ?",""+updateTime);
                //搜索看有没设置提醒时间的memo，有：取最近的设置提醒。
                AlarmUtils alarmUtils = new AlarmUtils();
                alarmUtils.setAlarm();

                preMemoDialog.dismiss();
                Toast.makeText(PreviewMemo.this, "删除成功", Toast.LENGTH_SHORT).show();

                msg.what =1;
                hd.sendMessage(msg);
            }
        });
        preEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemo(memoItem);
                preMemoDialog.dismiss();
            }
        });
        DialogUtils.setDialog(PreviewMemo.this, preMemoDialog, 2, 3,R.style.fade_in_out);
        preMemoDialog.show();
    }
    private void addMemo(final MemoItem item) {

        final AlertDialog addMemoDialog = new AlertDialog.Builder(PreviewMemo.this).create();
        View dialogView = LayoutInflater.from(PreviewMemo.this).inflate(R.layout.add_memo, null);
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
                    Toast.makeText(PreviewMemo.this, "不能为空呦", Toast.LENGTH_SHORT).show();
                } else {
                        //更新数据
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
                        DataSupport.updateAll(MemoItem.class, values, "updateTime = ?",item.getUpdateTime()+"");
                    }
                    //搜索看有没设置提醒时间的memo，有：取最近的设置提醒。
                    AlarmUtils alarmUtils = new AlarmUtils();
                    alarmUtils.setAlarm();
                addMemoDialog.dismiss();
                msg.what = 1;
                hd.sendMessage(msg);
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
                        Toast.makeText(PreviewMemo.this, "提醒设置成功", Toast.LENGTH_SHORT).show();
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
}
