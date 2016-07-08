package com.fujinbang.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.ui.activity.iactivity.IMissionActivity;
import com.fujinbang.presenter.SignPresenter;
import com.fujinbang.mission.MissionAdapter;
import com.fujinbang.share.ShareManager;
import com.fujinbang.ui.view.CheckInButton;

public class MissionActivity extends BaseActivity implements IMissionActivity, View.OnTouchListener {

    private LinearLayout ll_qq, ll_wechat, ll_weibo, ll_sms;
    private LinearLayout ll_mission_rule, ll_mission_list, ll_mission_sublist, ll_mission_record;
    private ImageView iv_back, iv_mission_indicator;
    private CheckInButton cib_check_in;
    private ListView lv_mission;
    private MissionAdapter mMissionAdapter;
    private TextView tv_mission_info;
    private SignPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        initPresenter();
        initView();
    }

    private final void initView() {
        cib_check_in = (CheckInButton) findViewById(R.id.cib_check_in);
        ll_qq = (LinearLayout) findViewById(R.id.ll_qq);
        ll_wechat = (LinearLayout) findViewById(R.id.ll_wechat);
        ll_sms = (LinearLayout) findViewById(R.id.ll_sms);
        ll_weibo = (LinearLayout) findViewById(R.id.ll_weibo);
        ll_mission_list = (LinearLayout) findViewById(R.id.ll_mission_list);
        ll_mission_sublist = (LinearLayout) findViewById(R.id.ll_mission_sub_list);
        ll_mission_record = (LinearLayout) findViewById(R.id.ll_mission_record);
        ll_mission_rule = (LinearLayout) findViewById(R.id.ll_mission_rule);
        iv_mission_indicator = (ImageView) findViewById(R.id.iv_mission_indicator);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_mission = (ListView) findViewById(R.id.lv_list_mission);
        tv_mission_info = (TextView) findViewById(R.id.tv_mission_info);

        cib_check_in.setOnClickListener(presenter);
        ll_wechat.setOnTouchListener(this);
        ll_sms.setOnTouchListener(this);
        ll_qq.setOnTouchListener(this);
        ll_weibo.setOnTouchListener(this);
        ll_mission_list.setOnTouchListener(this);
        ll_mission_record.setOnTouchListener(this);
        ll_mission_rule.setOnTouchListener(this);
        iv_back.setOnTouchListener(this);
        tv_mission_info.setOnTouchListener(this);

        mMissionAdapter = new MissionAdapter(this);
        lv_mission.setAdapter(mMissionAdapter);
    }

    private final void initPresenter() {
        presenter = new SignPresenter(this);
    }

    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                MissionActivity.this.finish();
                break;
            case R.id.ll_qq:
                ShareManager.showShare(this, ShareManager.qq);
                break;
            case R.id.ll_weibo:
                ShareManager.showShare(this, ShareManager.sinaWeibo);
                break;
            case R.id.ll_wechat:
                ShareManager.showShare(this, ShareManager.weixin);
                break;
            case R.id.ll_sms:
                ShareManager.showShare(this, ShareManager.sms);
                break;
            case R.id.ll_mission_rule:
                MissionRuleActivity.startActivity(this);
                break;
            case R.id.ll_mission_record:
                MissionRecordActivity.startActivity(this);
                break;
            case R.id.ll_mission_list:
                updateMissionListView();
                break;
            case R.id.tv_mission_info:
                InviteInfoActivity.startActivity(this);
                break;
            default:
                break;
        }
    }

    private final void updateMissionListView() {
        if (!mMissionAdapter.isVisiable) {
            mMissionAdapter.isVisiable = true;
            mMissionAdapter.update();
            ll_mission_sublist.setVisibility(View.VISIBLE);
            iv_mission_indicator.setImageResource(R.drawable.mission_list_up);

            ValueAnimator animator = ValueAnimator.ofInt(0, 560).setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewGroup.LayoutParams lp = lv_mission.getLayoutParams();
                    lp.height = (int) valueAnimator.getAnimatedValue();
                    lv_mission.setLayoutParams(lp);
                }
            });
            animator.start();
        } else {
            mMissionAdapter.isVisiable = false;

            ValueAnimator animator = ValueAnimator.ofInt(560, 0).setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewGroup.LayoutParams lp = lv_mission.getLayoutParams();
                    lp.height = (int) valueAnimator.getAnimatedValue();
                    lv_mission.setLayoutParams(lp);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ll_mission_sublist.setVisibility(View.GONE);
                    iv_mission_indicator.setImageResource(R.drawable.mission_list_down);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    ll_mission_sublist.setVisibility(View.GONE);
                    iv_mission_indicator.setImageResource(R.drawable.mission_list_down);
                }
            });
            animator.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.updateSignInfoFromLocal();
        presenter.updateSignInfo();
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

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MissionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setCheck(boolean isChecked) {
        cib_check_in.setCheck(isChecked);
    }

    @Override
    public void setDays(int days) {
        cib_check_in.setDays(days);
    }

    @Override
    public void setIntegration(int integration) {
        cib_check_in.setIntegration(integration);
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }
}
