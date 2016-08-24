package com.example.k.superbag2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.utils.AlarmUtils;
import com.example.k.superbag2.utils.DialogUtils;
import com.example.k.superbag2.utils.SaveUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by K on 2016/8/22.
 */
public class PreviewMemoActivity extends Activity {

    private AlertDialog preMemoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        previewMemo();
    }
    //参数为所点击的项在列表的位置
    private void previewMemo() {
        View preview = LayoutInflater.from(this).inflate(R.layout.pre_memo2, null);
        TextView preTitle, preContent, preDate,preAlarm;
        TextView preDelete, preEdit;

        preTitle = (TextView) preview.findViewById(R.id.pre_memo_title_tv);
        preContent = (TextView) preview.findViewById(R.id.pre_memo_content_tv);
        preAlarm = (TextView) preview.findViewById(R.id.pre_memo_alarm_tv);
        preDelete = (TextView) preview.findViewById(R.id.pre_memo_delete_bt);
        preEdit = (TextView) preview.findViewById(R.id.pre_memo_edit_bt);
        preDate = (TextView) preview.findViewById(R.id.pre_memo_alarm_date);
        preMemoDialog = new AlertDialog.Builder(this).create();
        preMemoDialog.setView(preview);
        preMemoDialog.setCancelable(true);

        //设置数据
        List<MemoItem> list = DataSupport.where("alarmTime = ?",SaveUtils.getAlarmTime()+"").find(MemoItem.class);
        if (list.size() ==0){
            Log.d("alarm","size"+list.size());
            return;
        }
        final MemoItem memoItem = DataSupport.find(MemoItem.class,26);
        Log.d("alarm","size"+memoItem.getAlarmTime());

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
                    DataSupport.deleteAll(MemoItem.class,"updateTime = ?",SaveUtils.getAlarmTime()+"");

                    //搜索看有没设置提醒时间的memo，有：取最近的设置提醒。
                    AlarmUtils alarmUtils = new AlarmUtils();
                    alarmUtils.setAlarm();

                    preMemoDialog.dismiss();
                    Toast.makeText(PreviewMemoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            preEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preMemoDialog.dismiss();
                    finish();
                }
            });

            DialogUtils.setDialog(this, preMemoDialog, 2, 3,R.style.fade_in_out);
            preMemoDialog.show();

        }
}
