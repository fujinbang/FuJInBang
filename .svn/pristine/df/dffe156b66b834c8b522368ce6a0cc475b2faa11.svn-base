package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fujinbang.R;
import com.fujinbang.ui.activity.iactivity.IQuietView;
import com.fujinbang.presenter.QuietPresenter;
import com.fujinbang.presenter.ipresenter.IQuietPresenter;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * 设置勿扰模式的界面
 */
public class QuietActivity extends BaseActivity implements View.OnTouchListener, CompoundButton.OnCheckedChangeListener, IQuietView {

    private ImageView iv_back;
    private LinearLayout ll_time;
    private TextView tv_start_time, tv_end_time;
    private RelativeLayout rl_start_time, rl_end_time;
    private SwitchCompat sc_quiet;
    private IQuietPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiet);

        initPresenter();
        initView();
    }

    private final void initPresenter() {
        presenter = new QuietPresenter(this);
    }

    private final void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        sc_quiet = (SwitchCompat) findViewById(R.id.sc_set_quiet);
        rl_start_time = (RelativeLayout) findViewById(R.id.rl_quiet_start_time);
        rl_end_time = (RelativeLayout) findViewById(R.id.rl_quiet_end_time);
        ll_time = (LinearLayout) findViewById(R.id.ll_quiet_time);
        tv_start_time = (TextView) findViewById(R.id.tv_quiet_starttime);
        tv_end_time = (TextView) findViewById(R.id.tv_quiet_endtime);

        sc_quiet.setChecked(true);
        iv_back.setOnTouchListener(this);
        sc_quiet.setOnCheckedChangeListener(this);
        rl_start_time.setOnTouchListener(this);
        rl_end_time.setOnTouchListener(this);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, QuietActivity.class);
        context.startActivity(intent);
    }

    private final void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_quiet_start_time:
                presenter.setStartTime();
                break;
            case R.id.rl_quiet_end_time:
                presenter.setEndTime();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
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
            case MotionEvent.ACTION_MOVE:
                v.setBackgroundColor(0x00000000);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.sc_set_quiet) {
            if (!isChecked) {
                ll_time.setVisibility(View.GONE);
            } else {
                ll_time.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showTimePickDialog(TimePickerDialog.OnTimeSetListener listener) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.newInstance(listener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false, false)
                .show(getSupportFragmentManager(), "timePickerDialog");
    }

    @Override
    public void setQuietMode(boolean isQuiet) {
        sc_quiet.setChecked(isQuiet);
    }

    @Override
    public void showStartTime(String time) {
        tv_start_time.setText(time);
    }

    @Override
    public void showEndTime(String time) {
        tv_end_time.setText(time);
    }
}
