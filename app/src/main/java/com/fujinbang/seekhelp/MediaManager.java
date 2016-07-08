package com.fujinbang.seekhelp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;

public class MediaManager {

	private static MediaPlayer mMediaPlayer; 
	private static boolean isPause;

	/**
	 * 播放音乐
	 * @param filePath
	 * @param onCompletionListener
	 */
	public static void playSound(Context context, String filePath,OnCompletionListener onCompletionListener) {
		if (mMediaPlayer == null) {
			//mMediaPlayer = new MediaPlayer();
			mMediaPlayer = MediaPlayer.create(context, Uri.parse(filePath));

			//设置一个error监听器
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {

				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
					mMediaPlayer.reset();
					return false;
				}
			});
		} else {
			//mMediaPlayer.reset();
			mMediaPlayer = MediaPlayer.create(context, Uri.parse(filePath));
		}

		try {
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(onCompletionListener);
			//mMediaPlayer.setDataSource(filePath);
			//mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 暂停播放
	 */
	public static void pause() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { //正在播放的时候
			mMediaPlayer.pause();
			isPause = true;
		}
	}

	/**
	 * 当前是isPause状态
	 */
	public static void resume() {
		if (mMediaPlayer != null && isPause) {  
			mMediaPlayer.start();
			isPause = false;
		}
	}

	/**
	 * 释放资源
	 */
	public static void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
}
