package com.fujinbang.conversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.ui.view.SlideListView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import com.fujinbang.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;

/**
 * Created by VITO on 2016/5/17.
 * 系统消息listView适配器
 */
public class SystemMsgAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private EMConversation SysMsg;
    private SlideListView mListView;

    public SystemMsgAdapter(Context context, SlideListView mListView) {
        this.mInflater = LayoutInflater.from(context);
        this.SysMsg = EMClient.getInstance().chatManager().getConversation(SimpleDataBase.admin,
                EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE),
                true);
        this.mListView = mListView;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return SysMsg;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_else, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_else_name);
            holder.msg = (TextView) convertView.findViewById(R.id.item_else_msg);
            holder.msgNum = (TextView) convertView.findViewById(R.id.item_else_msg_num);
            holder.time = (TextView) convertView.findViewById(R.id.item_else_time);
            holder.img = (ImageView) convertView.findViewById(R.id.item_else_img);
            holder.delBtn = (RelativeLayout) convertView.findViewById(R.id.item_else_delete_button);
            holder.locationImg = (ImageView) convertView.findViewById(R.id.item_else_location_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText("附近帮");
        holder.name.setTextColor(0xffffaf00);
        holder.img.setImageResource(R.drawable.integration);
        holder.locationImg.setVisibility(View.GONE);
        if (SysMsg.getLastMessage() != null) {
            holder.msg.setText(SysMsg.getLastMessage().toString());
            holder.time.setText(String.valueOf(SysMsg.getLastMessage().getMsgTime()));
            if (SysMsg.getUnreadMsgCount() > 0) {
                mListView.setVisibility(View.VISIBLE);
                holder.msgNum.setText(SysMsg.getUnreadMsgCount() + "");
                holder.msgNum.setVisibility(View.VISIBLE);
            } else {
                holder.msgNum.setVisibility(View.GONE);
            }
        } else {
            holder.msgNum.setVisibility(View.GONE);
            holder.msg.setText("");
        }

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysMsg.markAllMessagesAsRead();
                mListView.setVisibility(View.GONE);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView name;
        TextView msg;
        TextView msgNum;
        TextView time;
        ImageView img;
        RelativeLayout delBtn;
        ImageView locationImg;
    }
}
