package com.fujinbang.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.fujinbang.R;
import com.fujinbang.global.StatusBarCompat;

public class RegisterProtocolActivity extends Activity implements View.OnTouchListener {
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_protocol);
        StatusBarCompat.compat(this);
        back = (ImageView) findViewById(R.id.protocol_back);
        back.setOnTouchListener(this);
    }

    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.protocol_back:
                RegisterProtocolActivity.this.finish();
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                onClick(v);
                break;
            default:
                break;
        }
        return true;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterProtocolActivity.class);
        context.startActivity(intent);
    }
}
