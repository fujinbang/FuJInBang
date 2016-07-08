package com.clipimageview;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 裁剪图片的Activity
 *
 * @author xiechengfa2000@163.com
 * @ClassName: CropImageActivity
 * @Description:
 * @date 2015-5-8 下午3:39:22
 */
public class ClipImageActivity extends Activity implements OnClickListener {
    public static final String RESULT_PATH = "crop_image";
    private static final String KEY = "path";
    public static final String TMP_PATH = "clip_temp.png";
    private ClipImageLayout mClipImageLayout = null;

    public static void startActivityForResult(Activity activity, String path, int code) {
        Intent intent = new Intent(activity, ClipImageActivity.class);
        intent.putExtra(KEY, path);
        activity.startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image_layout);

        mClipImageLayout = (ClipImageLayout) findViewById(R.id.clipImageLayout);
        String path = getIntent().getStringExtra(KEY);

        // 有的系统返回的图片是旋转了，有的没有旋转，所以处理
        int degree = readBitmapDegree(path);
        Bitmap bitmap = createBitmap(path);
        if (bitmap != null) {
            if (degree == 0) {
                mClipImageLayout.setImageBitmap(bitmap);
            } else {
                mClipImageLayout.setImageBitmap(rotateBitmap(degree, bitmap));
            }
        } else {
            finish();
        }
        findViewById(R.id.okBtn).setOnClickListener(this);
        findViewById(R.id.cancleBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.okBtn) {
            Bitmap bitmap = mClipImageLayout.clip();

            String path = Environment.getExternalStorageDirectory() + "/"
                    + TMP_PATH;
            Log.i("zy", path);
            saveBitmap(bitmap, path);

            Intent intent = new Intent();
            intent.putExtra(RESULT_PATH, path);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    public final static Bitmap getClipBitmap(Intent data) {
        String path = data.getStringExtra(ClipImageActivity.RESULT_PATH);
        Bitmap photo = BitmapFactory.decodeFile(path);
        return photo;
    }

    public final static File getClipFile(Intent data) {
        String path = data.getStringExtra(ClipImageActivity.RESULT_PATH);
        File file = new File(path);
        return file;
    }

    public final static String getClipFilePath(Intent data) {
        return data.getStringExtra(ClipImageActivity.RESULT_PATH);
    }

    private void saveBitmap(Bitmap bitmap, String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }

        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fOut);
            fOut.flush();
        } catch (IOException e1) {
            Log.e("zy", "clipImageActivty :" + e1);
        } finally {
            try {
                if (fOut != null)
                    fOut.close();
            } catch (IOException e) {
                Log.e("zy", "clipImageActivity:" + e);
            }
        }
    }

    /**
     * 创建图片
     *
     * @param path
     * @return
     */
    private Bitmap createBitmap(String path) {
        if (path == null) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        // 调用上面定义的方法计算inSampleSize值
        opts.inSampleSize = calculateInSampleSize(opts, width, height);

        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inDither = false;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 读取图像的旋转度
    private int readBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    // 旋转图片
    private Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return resizedBitmap;
    }
}
