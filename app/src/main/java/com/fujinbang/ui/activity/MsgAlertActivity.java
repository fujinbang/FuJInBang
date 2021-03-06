package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.ui.activity.iactivity.ISetActivity;
import com.fujinbang.presenter.SetPresenter;
import com.fujinbang.presenter.ipresenter.ISetPresenter;

public class MsgAlertActivity extends BaseActivity implements View.OnTouchListener, ISetActivity {

    private ImageView iv_back;
    private RelativeLayout rl_sound;
    private SwitchCompat sc_sound, sc_vibrate;
    private ISetPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_alert);

        initPresenter();
        initView();
    }

    private final void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        rl_sound = (RelativeLayout) findViewById(R.id.rl_msg_alert_sound);
        sc_sound = (SwitchCompat) findViewById(R.id.sc_msg_alert_sound);
        sc_vibrate = (SwitchCompat) findViewById(R.id.sc_msg_alert_vibrate);

        sc_sound.setChecked(presenter.isAlert());
        sc_vibrate.setChecked(presenter.isVibrate());
        sc_vibrate.setOnCheckedChangeListener(mCheckListener);
        sc_sound.setOnCheckedChangeListener(mCheckListener);

        iv_back.setOnTouchListener(this);
        rl_sound.setOnTouchListener(this);
    }

    private final void initPresenter() {
        presenter = new SetPresenter(this);
    }

    private final void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_msg_alert_sound:
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                if (r != null)
                    r.play();
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

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MsgAlertActivity.class));
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    private CompoundButton.OnCheckedChangeListener mCheckListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.sc_msg_alert_vibrate:
                    presenter.uploadVibrate(isChecked);
                    break;
                case R.id.sc_msg_alert_sound:
                    presenter.uploadAlert(isChecked);
                    break;
                default:
                    break;
            }
        }
    };
}
