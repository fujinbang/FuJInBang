package com.fujinbang.seekhelp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.fujinbang.R;
import com.fujinbang.seekhelp.AudioManager.AudioStateListener;

/**
 * 用于管理Dialog
 *
 * 
 */
public class DialogManager {
	private boolean isRecording = false;// 已经开始录音

	private AudioManager mAudioManager;

	private float mTime;

	private static final int MSG_AUDIO_PREPARED = 0x110;
	private static final int MSG_VOICE_CHANGED = 0x111;



	private AlertDialog.Builder builder;
	private ImageButton mButton;
	private ImageView mVoice;

	private Context mContext;
	
	 private AlertDialog dialog;//用于取消AlertDialog.Builder

	/**
	 * 录音完成后的回调
	 */
	public interface AudioFinishRecorderListener {
		void onFinish(float seconds, String filePath);
	}

	private AudioFinishRecorderListener audioFinishRecorderListener;

	public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
		audioFinishRecorderListener = listener;
	}


	/**
	 * 构造方法 传入上下文
	 */
	public DialogManager(Context context) {
		this.mContext = context;
		String dir = Environment.getExternalStorageDirectory()+"/fujinbang_vido";

		mAudioManager = AudioManager.getInstance(dir);
		mAudioManager.setOnAudioStateListener(new AudioStateListener() {

			public void wellPrepared() {
				mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
			}
		});
	}

	// 显示录音的对话框 并初始化录音
	public void showRecordingDialog() {
		
		builder = new AlertDialog.Builder(mContext, R.style.AudioDialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_recorder,null);


		mButton = (ImageButton) view.findViewById(R.id.id_recorder_button);
		mVoice = (ImageView) view.findViewById(R.id.id_recorder_dialog_voice);

		mButton.setOnLongClickListener(new View.OnLongClickListener() {

			public boolean onLongClick(View v) {

				mAudioManager.prepareAudio();

				return true;
			}
		});
		mButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_UP:
						dimissDialog();
						mAudioManager.release();
						if (audioFinishRecorderListener != null) {
							audioFinishRecorderListener.onFinish(mTime,mAudioManager.getCurrentFilePath());
						}
						reset();
						break;
				}
				return false;
			}
		});

		builder.setView(view);
		builder.create();
		dialog = builder.show();
	}

	// 显示取消的对话框
	public void dimissDialog() {
		if(dialog != null && dialog.isShowing()){ //显示状态
			dialog.dismiss();
			dialog = null;
		}
	}


	/*
	 * 获取音量大小的线程
	 */
	private Runnable mGetVoiceLevelRunnable = new Runnable() {

		public void run() {
			while (isRecording) {
				try {
					Thread.sleep(100);
					mTime += 0.1f;
					if (mTime > 29.9f){
						dimissDialog();
						mAudioManager.release();
						if (audioFinishRecorderListener != null) {
							audioFinishRecorderListener.onFinish(mTime,mAudioManager.getCurrentFilePath());
						}
						reset();
					}
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_AUDIO_PREPARED:
					isRecording = true;
					// 开启一个线程
					new Thread(mGetVoiceLevelRunnable).start();
					break;

				case MSG_VOICE_CHANGED:
					updateVoiceLevel(mAudioManager.getVoiceLevel(7));
					break;
			}

			super.handleMessage(msg);
		}
	};

	// 显示更新音量级别的对话框
	public void updateVoiceLevel(int level) {
		if(dialog != null && dialog.isShowing()){
			//设置图片的id
			int resId = mContext.getResources().getIdentifier("v"+level, "drawable", mContext.getPackageName());
			mVoice.setImageResource(resId);
		}
	}

	/**
	 * 恢复状态及标志位
	 */
	private void reset() {
		isRecording = false;
		mTime = 0;
	}

}
