package com.fujinbang.conversation;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fujinbang.R;
import com.fujinbang.global.MissionDetail;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.global.TimeCalculator;
import com.fujinbang.seekhelp.MediaManager;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.widget.CircleTransform;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by VITO on 2016/5/12.
 * 正在进行的任务，列表适配器
 */
public class ConversationListAdapter extends BaseExpandableListAdapter {
    protected static String dir = Environment.getExternalStorageDirectory()+"/fujinbang_vido";
    protected Context mContext;
    private LayoutInflater mLayoutInflater;
    private ExpandableListView mListView;
    private List<HashMap<String,Object>> mMission;
    private List<EMConversation> groupChat;
    SimpleDataBase simpleDataBase;

    public ConversationListAdapter(Context context, ExpandableListView listView) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mListView = listView;
        this.mMission = MissionDetail.getInstance().getMissionList();
        this.groupChat = MissionDetail.getInstance().getGroupChat();
        simpleDataBase = new SimpleDataBase(mContext);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mMission.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.item_now_child, null);
        ChildViewHolder childViewHolder = new ChildViewHolder();
        childViewHolder.desc = (TextView) convertView.findViewById(R.id.item_now_child_desc);
        childViewHolder.recoder = (FrameLayout) convertView.findViewById(R.id.item_now_child_recoder_lenght);
        childViewHolder.anim = convertView.findViewById(R.id.item_now_child_recoder_anim);
        childViewHolder.recoderTime = (TextView) convertView.findViewById(R.id.item_now_child_recoder_time);
        childViewHolder.time = (TextView) convertView.findViewById(R.id.item_now_child_time);
        childViewHolder.bonus = (TextView) convertView.findViewById(R.id.item_now_child_bonus);

        childViewHolder.desc.setText(mMission.get(groupPosition).get("desc").toString());
        childViewHolder.time.setText(
                mMission.get(groupPosition).get("start_time").toString() +
                "\n至\n" +
                mMission.get(groupPosition).get("end_time").toString());
        childViewHolder.bonus.setText(mMission.get(groupPosition).get("bonus").toString() + "积分");

        if (getGroup(groupPosition).containsKey("voicelength") &&
                getGroup(groupPosition).get("voicelength").toString().length() != 0 &&
                MissionDetail.getInstance().getLocalAudio().containsKey(getGroup(groupPosition).get("helpid").toString())){
            ChildOnClickListener mListener = new ChildOnClickListener();
            mListener.setPosition(groupPosition,childViewHolder);
            childViewHolder.recoder.setOnClickListener(mListener);
            childViewHolder.desc.setVisibility(View.GONE);
            childViewHolder.recoder.setVisibility(View.VISIBLE);
            childViewHolder.recoderTime.setVisibility(View.VISIBLE);
            childViewHolder.recoderTime.setText(MissionDetail.getInstance().getLocalAudio().get(getGroup(groupPosition).get("helpid").toString())+"\"");
        }

        return convertView;
    }

    class ChildOnClickListener implements View.OnClickListener {
        int position;
        ChildViewHolder childViewHolder;

        public void setPosition(int position, ChildViewHolder childViewHolder) {
            this.position = position;
            this.childViewHolder = childViewHolder;
        }

        @Override
        public void onClick(View v) {
            //播放帧动画
            childViewHolder.anim.setBackgroundResource(R.drawable.play_anim);
            AnimationDrawable animation = (AnimationDrawable) childViewHolder.anim.getBackground();
            animation.start();
            //播放录音
            MediaManager.playSound(mContext, new File(dir, mMission.get(position).get("helpid").toString()+".aac").getAbsolutePath(), new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    //停止播放帧动画
                    childViewHolder.anim.setBackgroundResource(R.drawable.adj);
                    MediaManager.release();
                }
            });
        }
    }

    class ChildViewHolder {
        TextView desc;
        FrameLayout recoder;
        View anim;
        TextView recoderTime;
        TextView time;
        TextView bonus;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public HashMap<String,Object> getGroup(int groupPosition) {
        if (groupPosition < mMission.size()) {
            return mMission.get(groupPosition);
        }
        return null;
    }

    @Override
    public int getGroupCount() {
        return mMission.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_mission, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.unreadLabel = (TextView) convertView.findViewById(R.id.item_mission_unread);
            groupViewHolder.avatar = (GroupAvatarView) convertView.findViewById(R.id.item_mission_avatar);
            groupViewHolder.expandBtn = (ImageView) convertView.findViewById(R.id.item_mission_btn);
            groupViewHolder.time = (TextView) convertView.findViewById(R.id.item_mission_time);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        updateTime(TimeCalculator.getRestTime(mMission.get(groupPosition).get("end_time").toString()), groupViewHolder);

        groupViewHolder.expandBtn.setTag(groupPosition);
        groupViewHolder.expandBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mListView.isGroupExpanded((int) v.getTag())) {
                            //未展开
                            mListView.expandGroup((int) v.getTag());
                        } else {
                            mListView.collapseGroup((int) v.getTag());
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        if (isExpanded){
            groupViewHolder.expandBtn.setBackgroundResource(R.drawable.expanded);
        }else {
            groupViewHolder.expandBtn.setBackgroundResource(R.drawable.unexpanded);
        }

        groupViewHolder.avatar.removeAllViews();
        ImageView imageView = new ImageView(mContext);
        String neederAvatar = "http://o73gf55zi.bkt.clouddn.com/"+mMission.get(groupPosition).get("neederid")+".png";
        Glide.with(mContext).load(neederAvatar).placeholder(R.drawable.integration).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new CircleTransform(mContext)).into(imageView);
        groupViewHolder.avatar.addView(imageView);
        if (MissionDetail.getInstance().getAttendersInfo().containsKey(mMission.get(groupPosition).get("helpid").toString())){
            Iterator iter = MissionDetail.getInstance().getAttendersInfo().get(mMission.get(groupPosition).get("helpid").toString()).entrySet().iterator();
            int memberNum = 1;
            while (iter.hasNext() && memberNum < 4) {
                Map.Entry entry = (Map.Entry) iter.next();
                int id = (int) entry.getValue();
                ImageView img = new ImageView(mContext);
                String avatar = "http://o73gf55zi.bkt.clouddn.com/"+ id +".png";
                Glide.with(mContext).load(avatar).placeholder(R.drawable.integration).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new CircleTransform(mContext)).into(img);
                groupViewHolder.avatar.addView(img);
                memberNum ++;
            }
        }

        if (groupChat.get(groupPosition).getUnreadMsgCount() > 0) {
            // 显示与此用户的消息未读数
            groupViewHolder.unreadLabel.setText(String.valueOf(groupChat.get(groupPosition).getUnreadMsgCount()));
            groupViewHolder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            groupViewHolder.unreadLabel.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    DecimalFormat decimalFormat = new DecimalFormat("00");
    private void updateTime(long second, GroupViewHolder groupViewHolder) {
        if (second <= 0){
            groupViewHolder.time.setText("0:00:00");
            groupViewHolder.time.setTextColor(0xff000000);
            return;
        }
        String time = second / 3600 + ":" + decimalFormat.format(second % 3600 / 60) + ":" + decimalFormat.format(second % 3600 % 60);
        groupViewHolder.time.setText(time);
        if (second/3600 < 24 && second > 0){
            groupViewHolder.time.setTextColor(0xffff0000);
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public static class GroupViewHolder {
        /**
         * 用户头像
         */
        GroupAvatarView avatar;
        /**
         * 任务剩余时间
         */
        TextView time;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 下拉列表按钮
         */
        ImageView expandBtn;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }
}
