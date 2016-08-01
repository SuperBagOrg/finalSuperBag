package com.example.k.superbag2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.MemoItem;
import com.example.k.superbag2.utils.GetTime;

import org.litepal.crud.DataSupport;

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
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_memo_rec,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MemoItem item = memoItemList.get(position);
        holder.titleTV.setText(item.getTitle());
        if (item.getContent() != null){
            holder.contentTV.setText(item.getContent());
        }
        //如果设置了提醒，则显示“提醒”
        if (item.isAlarm()){
            holder.alarmTime.setText("提醒");
        }

        //设置编辑时间
        int editDay = item.getDay();
        int today = Integer.parseInt(new GetTime().getDay());
        if (item.getYear() == Integer.parseInt(new GetTime().getYear())) {
            if (editDay == today) {
                holder.editTime.setText("今天");
            } else if (editDay + 1 == today) {
                holder.editTime.setText("昨天");
            } else if (editDay + 2 == today) {
                holder.editTime.setText("前天");
            } else {
                holder.editTime.setText(item.getMonth()+"月"+item.getDay()+"日");
            }
        } else {
            holder.editTime.setText(item.getYear()+"年"+item.getMonth()+"月"+item.getDay()+"日");
        }

        //如果外部设置了点击事件
        if (onItemClickListener != null){
            holder.itemMemoLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //查看备忘
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemMemoLL,pos);
                }
            });

            holder.itemMemoLL.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //删除等
                    //TODO --
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemMemoLL,pos);
                    //必须返回true,否则会触发onClick
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return memoItemList.size();
    }

    //temp 为0表示新建时保存，1表示编辑时保存
    //index 表示当编辑时，更新数据库时需要的行号
    public void addItem(int position,MemoItem memoItem,int temp,int index){
        memoItemList.add(position,memoItem);
        if (temp == 0) {
            memoItem.save();
        } else {
            memoItem.update(DataSupport.count(MemoItem.class) - index);
        }
        notifyItemInserted(position);
        // TODO　更新数据库操作
    }

    public void removeItem(int position){
        memoItemList.remove(position);
        notifyItemRemoved(position);
        // TODO 数据库删除操作
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
