package com.example.k.superbag2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.MemoItem;

import java.util.List;

/**
 * Created by K on 2016/7/28.
 */
public class MemoRecyclerAdapter extends RecyclerView.Adapter<MemoRecyclerAdapter.MyViewHolder> {

    private List<MemoItem> memoItemList;
    private Context context;

    public MemoRecyclerAdapter(Context context,List<MemoItem> memoItemList){
        this.context = context;
        this.memoItemList = memoItemList;
    }

    //因为recyclerView没有提供点击事件，所以要自己实现
    public interface ONItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    private ONItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(ONItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_memo_rec,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MemoItem item = memoItemList.get(position);
        holder.titleTV.setText(item.getTitle());
        if (item.getContent() != null){
            holder.contentTV.setText(item.getContent());
        }
        //有待完善，还有很大问题
        if (item.isAlarm()){
//            holder.alarmTime.setText("提醒时间:"+item.getAlarmTime());
        }
        holder.editTime.setText(item.getEditTime());

        //如果外部设置了点击事件
        if (onItemClickListener != null){
            holder.itemMemoLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //查看备忘
                }
            });

            holder.itemMemoLL.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //删除等
                    //TODO --
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return memoItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titleTV,contentTV,alarmTime,editTime;
        LinearLayout itemMemoLL;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTV = (TextView)itemView.findViewById(R.id.item_memo_title);
            contentTV = (TextView)itemView.findViewById(R.id.item_memo_content);
            alarmTime = (TextView)itemView.findViewById(R.id.item_memo_alarmTime);
            editTime = (TextView)itemView.findViewById(R.id.item_memo_edit_time);
            itemMemoLL = (LinearLayout)itemView.findViewById(R.id.item_memo_LL);
        }
    }
}
