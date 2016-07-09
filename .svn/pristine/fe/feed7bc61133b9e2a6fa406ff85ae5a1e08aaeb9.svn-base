package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fujinbang.R;
import com.fujinbang.mission.Mission;
import com.fujinbang.mission.MissionModel;

import java.util.List;

public class MissionRecordActivity extends BaseActivity implements View.OnTouchListener {

    private TextView tv_delete;
    private ImageView iv_back;
    private ListView lv_record;
    private List<Mission> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_record);

        initView();
    }

    private final void initView() {
        tv_delete = (TextView) findViewById(R.id.tv_mission_record_delete);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_record = (ListView) findViewById(R.id.lv_list_mission_record);

        list = new MissionModel(this).getMissionList();
        lv_record.setAdapter(new MissionRecordAdapter());
        iv_back.setOnTouchListener(this);
        tv_delete.setOnTouchListener(this);
    }

    private final void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MissionRecordActivity.class));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundColor(0xffe8e8e8);
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundColor(0x00000000);
                onClick(v);
                break;
            default:
                break;
        }
        return true;
    }

    class MissionRecordAdapter extends BaseAdapter {

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
            View view = LayoutInflater.from(MissionRecordActivity.this).inflate(R.layout.listview_item_mission_record, parent, false);
            TextView content = (TextView) view.findViewById(R.id.tv_mission_record_content);
            TextView date = (TextView) view.findViewById(R.id.tv_mission_record_date);

            Mission mission = list.get(position);
            content.setText(mission.getDesc());
            date.setText(mission.getDate());
            return view;
        }
    }
}
