package com.fujinbang.mission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fujinbang.R;
import com.fujinbang.global.SimpleDataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class MissionAdapter extends BaseAdapter {

    private List<Mission> list = new ArrayList<>();

    private Context context;

    public boolean isVisiable = false;

    public MissionAdapter(Context context) {
        this.context = context;
        list.add(new Mission("首次发布求助", 5, false));
        list.add(new Mission("首次接单", 5, false));
        list.add(new Mission("完成求助次数达10次", 10, false));
        list.add(new Mission("完成接单次数达10次", 10, false));
    }

    public void update() {
        SimpleDataBase db = new SimpleDataBase(context);
        if (MissionModel.isToday(db.getPickLastDate())) {
            setMissionComplete(1, true);
            if (db.getPickNum() >= 10)
                setMissionComplete(3, true);
        }

        if (MissionModel.isToday(db.getPublishLastDate())) {
            setMissionComplete(0, true);
            if (db.getPublishNum() >= 10)
                setMissionComplete(2, true);
        }
    }

    public void setMissionComplete(int num, boolean isComplete) {
        if (0 <= num && num < list.size()) {
            list.get(num).setComplete(isComplete);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_item_mission, null, false);
        TextView tv_desc = (TextView) view.findViewById(R.id.tv_mission_desc);
        TextView tv_intergration = (TextView) view.findViewById(R.id.tv_mission_intergration);
        TextView tv_status = (TextView) view.findViewById(R.id.tv_mission_status);

        Mission mission = list.get(position);
        tv_desc.setText(mission.getDesc());
        tv_intergration.setText("+" + mission.getIntergration());
        tv_status.setText(mission.isComplete() ? "完成" : "未完成");
        return view;
    }
}
