package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fujinbang.R;
import com.fujinbang.presenter.FeedBackPresenter;

import rx.Observable;

public class FeedbackOnlineActivity extends BaseActivity implements View.OnTouchListener {

    private FeedBackPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_online);

        initPresenter();
        initView();
    }

    private final void initPresenter() {
        presenter = new FeedBackPresenter(this);
    }

    private final void initView() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        Button btn_ok = (Button) findViewById(R.id.btn_feedback_online_ok);
        TextView title = (TextView) findViewById(R.id.title_feedback_online);
        TextView hint = (TextView) findViewById(R.id.hint_feedback_online);
        final EditText et_content = (EditText) findViewById(R.id.et_feedback_online_content);

        if (getIntent().getBooleanExtra("flag", false)) {
            title.setText("举报");
            hint.setText("举报内容");
            hint.setTextColor(0xffff0000);
        }

        iv_back.setOnTouchListener(this);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.feedback(et_content.getText().toString());
                finish();
            }
        });
    }

    public static void startActivity(Context context) {
        startActivity(context, false);
    }

    public static void startActivity(Context context, boolean flag) {
        Intent intent = new Intent(context, FeedbackOnlineActivity.class);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("onTouch", v.getDrawingCacheBackgroundColor() + "");
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

    public void showToast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
}
