package com.example.k.superbag2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.k.superbag2.R;
import com.example.k.superbag2.bean.ItemBean;
import com.example.k.superbag2.others.Constant;
import com.example.k.superbag2.utils.GetTime;

import java.util.List;

/**
 * Created by K on 2016/8/3.
 */
public class FirstpageAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ItemBean> itemBeanList;
    private LayoutInflater inflater;

    public FirstpageAdapter2(Context context, List<ItemBean> itemBeen){
        this.context = context;
        this.itemBeanList = itemBeen;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    public void removeData(int position){
        itemBeanList.remove(position);
        notifyItemRemoved(position);
    }

    private OnItemClickListener onItemClickListener = null;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_NO_PIC){
            return new Pic0ViewHolder(inflater.inflate(R.layout.item_firstpage_0pic,parent,false));
        } else if (viewType == Constant.TYPE_ONE_PIC){
            return new Pic1ViewHolder(inflater.inflate(R.layout.item_fp_1pic,parent,false));
        } else if (viewType == Constant.TYPE_TWO_PIC){
            return new Pic2ViewHolder(inflater.inflate(R.layout.item_fp_2pic,parent,false));
        } else if (viewType == Constant.TYPE_THREE_PIC){
            return new Pic3ViewHolder(inflater.inflate(R.layout.item_fp_3pic,parent,false));
        } else {
            return new Pic4ViewHolder(inflater.inflate(R.layout.item_fp_4pic,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemBean itemBean = itemBeanList.get(position);
        if (holder instanceof Pic0ViewHolder){
            if (!itemBean.getTag1().equals("")) {
                ((Pic0ViewHolder)holder).tag1.setText(itemBean.getTag1());
                ((Pic0ViewHolder)holder).tag1.setVisibility(View.VISIBLE);
                ((Pic0ViewHolder)holder).tag.setVisibility(View.VISIBLE);
            }

            if (!itemBean.getTag2().equals("")){
                ((Pic0ViewHolder)holder).tag2.setText(itemBean.getTag2());
                ((Pic0ViewHolder)holder).tag2.setVisibility(View.VISIBLE);
            }
            ((Pic0ViewHolder)holder).oldTime.setText(itemBean.getDayTime());
            ((Pic0ViewHolder)holder).time.setText(itemBean.getDay()+"");
            ((Pic0ViewHolder)holder).month.setText("/"+itemBean.getMonth());
            ((Pic0ViewHolder)holder).week.setText( GetTime.getWeekOfDate(itemBean.getYear()+"",itemBean.getMonth()+"",itemBean.getDay()+""));
            ((Pic0ViewHolder)holder).content.setText(itemBean.getContent());
            ((Pic0ViewHolder)holder).weather.setText(itemBean.getWeather());
            ((Pic0ViewHolder)holder).feelings.setText(itemBean.getFeelings());

            if (onItemClickListener != null){
                ((Pic0ViewHolder) holder).fpLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(((Pic0ViewHolder) holder).fpLL,pos);
                    }
                });

                ((Pic0ViewHolder) holder).fpLL.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //删除等
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(((Pic0ViewHolder) holder).fpLL,pos);
                        //必须返回true,否则会触发onClick
                        return true;
                    }
                });
            }
        } else if (holder instanceof Pic1ViewHolder){
            if (!itemBean.getTag1().equals("")){
                ((Pic1ViewHolder)holder).tag1.setText(itemBean.getTag1());
                ((Pic1ViewHolder)holder).tag1.setVisibility(View.VISIBLE);
                ((Pic1ViewHolder)holder).tag.setVisibility(View.VISIBLE);

            }
            if (!itemBean.getTag2().equals("")){
                ((Pic1ViewHolder)holder).tag2.setVisibility(View.VISIBLE);
                ((Pic1ViewHolder)holder).tag2.setText(itemBean.getTag2());
            }
            ((Pic1ViewHolder)holder).oldTime.setText(itemBean.getDayTime());
            ((Pic1ViewHolder)holder).time.setText(itemBean.getDay()+"");
            ((Pic1ViewHolder)holder).month.setText("/"+itemBean.getMonth());
            ((Pic1ViewHolder)holder).week.setText( GetTime.getWeekOfDate(itemBean.getYear()+"",itemBean.getMonth()+"",itemBean.getDay()+""));
            ((Pic1ViewHolder)holder).content.setText(itemBean.getContent());
            ((Pic1ViewHolder)holder).weather.setText(itemBean.getWeather());
            ((Pic1ViewHolder)holder).feelings.setText(itemBean.getFeelings());

            Glide.with(context)
                    .load(itemBean.getPic1())
                    .asBitmap()
                    .into(((Pic1ViewHolder)holder).iv);
            if (onItemClickListener != null){
                ((Pic1ViewHolder) holder).fpLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(((Pic1ViewHolder) holder).fpLL,pos);
                    }
                });

                ((Pic1ViewHolder) holder).fpLL.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //删除等
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(((Pic1ViewHolder) holder).fpLL,pos);
                        //必须返回true,否则会触发onClick
                        return true;
                    }
                });
            }

        } else if (holder instanceof Pic2ViewHolder){
            if (!itemBean.getTag1().equals("")){
                ((Pic2ViewHolder)holder).tag1.setText(itemBean.getTag1());
                ((Pic2ViewHolder)holder).tag1.setVisibility(View.VISIBLE);
                ((Pic2ViewHolder)holder).tag.setVisibility(View.VISIBLE);

            }
            if (!itemBean.getTag2().equals("")){
                ((Pic2ViewHolder)holder).tag2.setText(itemBean.getTag2());
                ((Pic2ViewHolder)holder).tag2.setVisibility(View.VISIBLE);
            }
            ((Pic2ViewHolder)holder).oldTime.setText(itemBean.getDayTime());
            ((Pic2ViewHolder)holder).time.setText(itemBean.getDay()+"");
            ((Pic2ViewHolder)holder).month.setText("/"+itemBean.getMonth());
            ((Pic2ViewHolder)holder).week.setText( GetTime.getWeekOfDate(itemBean.getYear()+"",itemBean.getMonth()+"",itemBean.getDay()+""));
            ((Pic2ViewHolder)holder).content.setText(itemBean.getContent());
            ((Pic2ViewHolder)holder).weather.setText(itemBean.getWeather());
            ((Pic2ViewHolder)holder).feelings.setText(itemBean.getFeelings());
            Glide.with(context)
                    .load(itemBean.getPic1())
                    .asBitmap()
                    .into(((Pic2ViewHolder)holder).iv1);
            Glide.with(context)
                    .load(itemBean.getPic2())
                    .asBitmap()
                    .into(((Pic2ViewHolder)holder).iv2);

            if (onItemClickListener != null){
                ((Pic2ViewHolder) holder).fpLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(((Pic2ViewHolder) holder).fpLL,pos);
                    }
                });

                ((Pic2ViewHolder) holder).fpLL.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //删除等
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(((Pic2ViewHolder) holder).fpLL,pos);
                        //必须返回true,否则会触发onClick
                        return true;
                    }
                });
            }
        } else if (holder instanceof Pic3ViewHolder){
            if (!itemBean.getTag1().equals("")){
                ((Pic3ViewHolder)holder).tag1.setText(itemBean.getTag1());
                ((Pic3ViewHolder)holder).tag1.setVisibility(View.VISIBLE);
                ((Pic3ViewHolder)holder).tag.setVisibility(View.VISIBLE);

            }
            if (!itemBean.getTag2().equals("")){
                ((Pic3ViewHolder)holder).tag2.setText(itemBean.getTag2());
                ((Pic3ViewHolder)holder).tag2.setVisibility(View.VISIBLE);
            }
            ((Pic3ViewHolder)holder).oldTime.setText(itemBean.getDayTime());
            ((Pic3ViewHolder)holder).time.setText(itemBean.getDay()+"");
            ((Pic3ViewHolder)holder).month.setText("/"+itemBean.getMonth());
            ((Pic3ViewHolder)holder).week.setText( GetTime.getWeekOfDate(itemBean.getYear()+"",itemBean.getMonth()+"",itemBean.getDay()+""));
            ((Pic3ViewHolder)holder).content.setText(itemBean.getContent());
            ((Pic3ViewHolder)holder).weather.setText(itemBean.getWeather());
            ((Pic3ViewHolder)holder).feelings.setText(itemBean.getFeelings());
            Glide.with(context)
                    .load(itemBean.getPic1())
                    .asBitmap()
                    .into(((Pic3ViewHolder)holder).iv1);
            Glide.with(context)
                    .load(itemBean.getPic2())
                    .asBitmap()
                    .into(((Pic3ViewHolder)holder).iv2);
            Glide.with(context)
                    .load(itemBean.getPic3())
                    .asBitmap()
                    .into(((Pic3ViewHolder)holder).iv3);

            if (onItemClickListener != null) {
                ((Pic3ViewHolder) holder).fpLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(((Pic3ViewHolder) holder).fpLL, pos);
                    }
                });

                ((Pic3ViewHolder) holder).fpLL.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //删除等
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(((Pic3ViewHolder) holder).fpLL, pos);
                        //必须返回true,否则会触发onClick
                        return true;
                    }
                });
            }
        } else {
            if (!itemBean.getTag1().equals("")){
                ((Pic4ViewHolder)holder).tag1.setText(itemBean.getTag1());
                ((Pic4ViewHolder)holder).tag1.setVisibility(View.VISIBLE);
                ((Pic4ViewHolder)holder).tag.setVisibility(View.VISIBLE);
            }
            if (!itemBean.getTag2().equals("")){
                ((Pic4ViewHolder)holder).tag2.setText(itemBean.getTag2());
                ((Pic4ViewHolder)holder).tag2.setVisibility(View.VISIBLE);
            }
            ((Pic4ViewHolder)holder).oldTime.setText(itemBean.getDayTime());
            ((Pic4ViewHolder)holder).time.setText(itemBean.getDay()+"");
            ((Pic4ViewHolder)holder).month.setText("/"+itemBean.getMonth());
            ((Pic4ViewHolder)holder).week.setText( GetTime.getWeekOfDate(itemBean.getYear()+"",itemBean.getMonth()+"",itemBean.getDay()+""));
            ((Pic4ViewHolder)holder).content.setText(itemBean.getContent());
            ((Pic4ViewHolder)holder).weather.setText(itemBean.getWeather());
            ((Pic4ViewHolder)holder).feelings.setText(itemBean.getFeelings());
            Glide.with(context)
                    .load(itemBean.getPic1())
                    .asBitmap()
                    .into(((Pic4ViewHolder)holder).iv1);
            Glide.with(context)
                    .load(itemBean.getPic2())
                    .asBitmap()
                    .into(((Pic4ViewHolder)holder).iv2);
            Glide.with(context)
                    .load(itemBean.getPic3())
                    .asBitmap()
                    .into(((Pic4ViewHolder)holder).iv3);
            Glide.with(context)
                    .load(itemBean.getPic4())
                    .asBitmap()
                    .into(((Pic4ViewHolder)holder).iv4);

            if (onItemClickListener != null) {
                ((Pic4ViewHolder) holder).fpLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(((Pic4ViewHolder) holder).fpLL, pos);
                    }
                });

                ((Pic4ViewHolder) holder).fpLL.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //删除等
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(((Pic4ViewHolder) holder).fpLL, pos);
                        //必须返回true,否则会触发onClick
                        return true;
                    }
                });
            }
        }

    }


    @Override
    public int getItemViewType(int position) {
        return itemBeanList.get(position).getPicNum();
    }

    @Override
    public int getItemCount() {
        return itemBeanList.size();
    }

    class Pic0ViewHolder extends RecyclerView.ViewHolder {

        TextView time,content,oldTime,tag1,tag2,tag3,weather,feelings;
        TextView month,week;
        LinearLayout fpLL;
        ImageView tag;

        public Pic0ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.fp_0pic_time);
            content = (TextView)itemView.findViewById(R.id.fp_0pic_content);
            oldTime = (TextView)itemView.findViewById(R.id.fp_0pic_oldTime);
            tag1 = (TextView) itemView.findViewById(R.id.fp_0pic_tag1);
            tag2 = (TextView) itemView.findViewById(R.id.fp_0pic_tag2);
            tag3 = (TextView) itemView.findViewById(R.id.fp_0pic_tag3);
            weather = (TextView) itemView.findViewById(R.id.fp_0pic_weather);
            feelings = (TextView) itemView.findViewById(R.id.fp_0pic_feelings);
            fpLL = (LinearLayout) itemView.findViewById(R.id.fp_0pic_ll);
            month = (TextView) itemView.findViewById(R.id.fp_0pic_month);
            week = (TextView) itemView.findViewById(R.id.fp_0pic_week);
            tag = (ImageView) itemView.findViewById(R.id.fp_0pic_tag);
        }
    }

    class Pic1ViewHolder extends RecyclerView.ViewHolder {
        TextView time,content,oldTime,tag1,tag2,tag3,weather,feelings;
        ImageView iv;
        LinearLayout fpLL;
        TextView month,week;
        ImageView tag;

        public Pic1ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.fp_1pic_time);
            content = (TextView)itemView.findViewById(R.id.fp_1pic_content);
            oldTime = (TextView)itemView.findViewById(R.id.fp_1pic_oldTime);
            tag1 = (TextView)itemView.findViewById(R.id.fp_1pic_tag1);
            tag2 = (TextView) itemView.findViewById(R.id.fp_1pic_tag2);
            tag3 = (TextView) itemView.findViewById(R.id.fp_1pic_tag3);
            iv = (ImageView) itemView.findViewById(R.id.fp_1pic_iv);
            weather = (TextView) itemView.findViewById(R.id.fp_1pic_weather);
            feelings = (TextView) itemView.findViewById(R.id.fp_1pic_feelings);
            fpLL = (LinearLayout) itemView.findViewById(R.id.fp_1pic_ll);
            month = (TextView) itemView.findViewById(R.id.fp_1pic_month);
            week = (TextView) itemView.findViewById(R.id.fp_1pic_week);
            tag = (ImageView) itemView.findViewById(R.id.fp_1pic_tag);
        }
    }

    class Pic2ViewHolder extends RecyclerView.ViewHolder {
        TextView time,content,oldTime,tag1,tag2,tag3,weather,feelings;
        ImageView iv1,iv2;
        LinearLayout fpLL;
        TextView month,week;
        ImageView tag;

        public Pic2ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.fp_2pic_time);
            content = (TextView) itemView.findViewById(R.id.fp_2pic_content);
            oldTime = (TextView) itemView.findViewById(R.id.fp_2pic_oldTime);
            tag1 = (TextView) itemView.findViewById(R.id.fp_2pic_tag1);
            tag2 = (TextView)itemView.findViewById(R.id.fp_2pic_tag2);
            tag3 = (TextView)itemView.findViewById(R.id.fp_2pic_tag3);
            iv1 = (ImageView)itemView.findViewById(R.id.fp_2pic_iv1);
            iv2 = (ImageView)itemView.findViewById(R.id.fp_2pic_iv2);
            weather = (TextView)itemView.findViewById(R.id.fp_2pic_weather);
            feelings = (TextView)itemView.findViewById(R.id.fp_2pic_feelings);
            fpLL = (LinearLayout) itemView.findViewById(R.id.fp_2pic_ll);
            month = (TextView) itemView.findViewById(R.id.fp_2pic_month);
            week = (TextView) itemView.findViewById(R.id.fp_2pic_week);
            tag = (ImageView) itemView.findViewById(R.id.fp_2pic_tag);
        }
    }

    class Pic3ViewHolder extends RecyclerView.ViewHolder {
        TextView time,content,oldTime,tag1,tag2,tag3,weather,feelings;
        ImageView iv1,iv2,iv3;
        LinearLayout fpLL;
        TextView month,week;
        ImageView tag;

        public Pic3ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.fp_3pic_time);
            content = (TextView) itemView.findViewById(R.id.fp_3pic_content);
            oldTime = (TextView) itemView.findViewById(R.id.fp_3pic_oldTime);
            tag1 = (TextView) itemView.findViewById(R.id.fp_3pic_tag1);
            tag2 = (TextView)itemView.findViewById(R.id.fp_3pic_tag2);
            tag3 = (TextView)itemView.findViewById(R.id.fp_3pic_tag3);
            iv1 = (ImageView)itemView.findViewById(R.id.fp_3pic_iv1);
            iv2 = (ImageView)itemView.findViewById(R.id.fp_3pic_iv2);
            iv3 = (ImageView)itemView.findViewById(R.id.fp_3pic_iv3);
            weather = (TextView)itemView.findViewById(R.id.fp_3pic_weather);
            feelings = (TextView)itemView.findViewById(R.id.fp_3pic_feelings);
            fpLL = (LinearLayout) itemView.findViewById(R.id.fp_3pic_ll);
            month = (TextView) itemView.findViewById(R.id.fp_3pic_month);
            week = (TextView) itemView.findViewById(R.id.fp_3pic_week);
            tag = (ImageView) itemView.findViewById(R.id.fp_3pic_tag);
        }
    }

    class Pic4ViewHolder extends RecyclerView.ViewHolder {
        TextView time,content,oldTime,tag1,tag2,tag3,weather,feelings;
        ImageView iv1,iv2,iv3,iv4;
        LinearLayout fpLL;
        TextView month,week;
        ImageView tag;

        public Pic4ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.fp_4pic_time);
            content = (TextView) itemView.findViewById(R.id.fp_4pic_content);
            oldTime = (TextView) itemView.findViewById(R.id.fp_4pic_oldTime);
            tag1 = (TextView) itemView.findViewById(R.id.fp_4pic_tag1);
            tag2 = (TextView)itemView.findViewById(R.id.fp_4pic_tag2);
            tag3 = (TextView)itemView.findViewById(R.id.fp_4pic_tag3);
            iv1 = (ImageView)itemView.findViewById(R.id.fp_4pic_iv1);
            iv2 = (ImageView)itemView.findViewById(R.id.fp_4pic_iv2);
            iv3 = (ImageView)itemView.findViewById(R.id.fp_4pic_iv3);
            iv4 = (ImageView)itemView.findViewById(R.id.fp_4pic_iv4);
            weather = (TextView)itemView.findViewById(R.id.fp_4pic_weather);
            feelings = (TextView)itemView.findViewById(R.id.fp_4pic_feelings);
            fpLL = (LinearLayout) itemView.findViewById(R.id.fp_4pic_ll);
            month = (TextView) itemView.findViewById(R.id.fp_4pic_month);
            week = (TextView) itemView.findViewById(R.id.fp_4pic_week);
            tag = (ImageView) itemView.findViewById(R.id.fp_4pic_tag);
        }
    }
}
