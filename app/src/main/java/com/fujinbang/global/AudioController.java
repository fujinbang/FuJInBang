package com.fujinbang.global;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by VITO on 2016/6/2.
 *
 */
public class AudioController {
    private static AudioController audioController;
    UploadManager uploadManager;

    private AudioController(){
        uploadManager = new UploadManager();
    }

    public static AudioController getInstance() {
        if (null == audioController) {
            synchronized (AudioController.class) {
                if (null == audioController) {
                    audioController = new AudioController();
                }
            }
        }
        return audioController;
    }

    public void uploadAudio(int helpid, String token, String filepath, final UploadListener uploadListener){
        File data = new File(filepath);
        String key = String.valueOf(helpid)+".aac";
        uploadManager.put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                        if (info.isOK()){
                            uploadListener.OnUploadSucceed();
                        }else {
                            uploadListener.OnUploadError();
                            Log.e("qiniu",info.error);
                        }
                    }
                },
                new UploadOptions(null, null, false,
                        new UpProgressHandler(){
                            public void progress(String key, double percent){
                                Log.i("qiniu", key + ": " + percent);
                                uploadListener.OnUploadProgress(percent);
                            }
                        }, null));
    }

    public interface UploadListener {
        void OnUploadSucceed();
        void OnUploadError();
        void OnUploadProgress(double percent);
    }
}
