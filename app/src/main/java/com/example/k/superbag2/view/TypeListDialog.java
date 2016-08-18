package com.example.k.superbag2.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.k.superbag2.R;

import java.util.List;

/**
 * Created by Aersasi on 2016/8/18.
 */
public class TypeListDialog {
    private TextView txtTitle;
    private ListView lsvList;
    private String[] items;
    public Dialog dialog;
    private Context context;

    public TypeListDialog(Context context, List<String> list, AdapterView.OnItemClickListener onItemClickListener) {
        String[] items = new String[list.size()];
        items = list.toArray(items);

        initUI(context, items, onItemClickListener);
    }

    public TypeListDialog(Context context, String[] items, AdapterView.OnItemClickListener onItemClickListener) {
        initUI(context, items, onItemClickListener);
    }

    /**
     * 初始化对话框
     * @param context
     * @param items
     * @param onItemClickListener
     */
    @SuppressLint("InflateParams")
    private void initUI(Context context, String[] items, AdapterView.OnItemClickListener onItemClickListener) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.type_listdialog, null);
        this.lsvList = (ListView) view.findViewById(R.id.dialog_listview);
        this.txtTitle = (TextView) view.findViewById(R.id.bottom_title);
        lsvList.setAdapter(new ListAdapter());
        //弹出对话框
        dialog = new TypeDialog(context, view);
        this.items = items;
        lsvList.setOnItemClickListener(onItemClickListener);
    }

    public void setTitleVisiblity(int visibility) {
        txtTitle.setVisibility(visibility);
    }

    public void setTitleText(String text) {
        txtTitle.setText(text);
    }

    class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items==null ? 0 : items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;
            if (null == convertView) {
                convertView = new TextView(context);
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.dimen_50)));
                textView = (TextView) convertView;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f);
                textView.setTextColor(Color.BLACK);
                textView.setMaxLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.END);
            }else{
                textView = (TextView) convertView;
            }
            textView.setText(items[position]);
            return convertView;
        }

    }
}
