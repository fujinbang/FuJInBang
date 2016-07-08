package com.fujinbang.conversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fujinbang.global.MissionDetail;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.global.TimeCalculator;
import com.fujinbang.R;
import com.hyphenate.easeui.widget.CircleTransform;

/**
 * Created by VITO on 2016/5/16.
 * 求助消息列表适配器
 */
public class HelpListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    protected Context mContext;
    private List<HashMap<String,Object>> helpMsg;
    protected SimpleDataBase simpleDataBase;

    private DeleteBtnListener delBtnOnClickListener;

    public HelpListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.helpMsg = MissionDetail.getInstance().getInvitedMissionList();
        this.simpleDataBase = new SimpleDataBase(mContext);
    }
    @Override
    public int getCount() {
        return helpMsg.size();
    }

    @Override
    public Object getItem(int position) {
        return helpMsg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_else,null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_else_name);
            holder.dist = (TextView) convertView.findViewById(R.id.item_else_location_dist);
            holder.msg = (TextView) convertView.findViewById(R.id.item_else_msg);
            holder.time = (TextView) convertView.findViewById(R.id.item_else_time);
            holder.img = (ImageView) convertView.findViewById(R.id.item_else_img);
            holder.delBtn = (RelativeLayout) convertView.findViewById(R.id.item_else_delete_button);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        if (helpMsg.get(position).containsKey("nickName")){
            holder.name.setText(helpMsg.get(position).get("nickName").toString());
            holder.msg.setText(helpMsg.get(position).get("nickName").toString()+"向你发来求助，点击查看");
        }

        if (helpMsg.get(position).containsKey("regDate")){
            TimeCalculator timeCalculator = new TimeCalculator(helpMsg.get(position).get("regDate").toString());
            if (timeCalculator.isSameDate()){
                holder.time.setText(timeCalculator.getHourMin());
            }else {
                holder.time.setText(timeCalculator.getMonthDay());
            }
        }

        String avatar = "http://o73gf55zi.bkt.clouddn.com/"+ helpMsg.get(position).get("neederid").toString() +".png";
        Glide.with(mContext)
                .load(avatar)
                .placeholder(R.drawable.integration)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform(mContext))
                .into(holder.img);

        holder.dist.setText((int) DistanceUtil.getDistance(
                new LatLng(simpleDataBase.getLocationX(), simpleDataBase.getLocationY()),
                new LatLng((double) helpMsg.get(position).get("x"), (double) helpMsg.get(position).get("y"))) + "m");

        holder.delBtn.setTag(position);
        if (delBtnOnClickListener!=null){
            holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delBtnOnClickListener.onBtnClick((int)v.getTag());
                }
            });
        }

        return convertView;
    }
    /*存放控件*/
    public final class ViewHolder{
        TextView name;
        TextView dist;
        TextView msg;
        TextView time;
        ImageView img;
        RelativeLayout delBtn;
    }

    public interface DeleteBtnListener {
        void onBtnClick(int position);
    }

    public void setDelBtnOnClickListener(DeleteBtnListener delBtnOnClickListener){
        this.delBtnOnClickListener = delBtnOnClickListener;
    }
}
