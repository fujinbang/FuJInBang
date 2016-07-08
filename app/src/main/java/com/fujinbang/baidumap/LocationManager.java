package com.fujinbang.baidumap;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.fujinbang.global.SimpleDataBase;

import java.util.HashSet;

/**
 * Created by Administrator on 2016/5/17.
 */
public class LocationManager implements RadarSearchListener {

    /**
     * 单例
     */
    private static LocationManager singleInstance;
    /**
     * 百度定位
     */
    private LocationClient mLocationClient;
    /**
     * 百度雷达 用于搜索附近的用户
     */
    RadarSearchManager mManager;
    /**
     * 监听获得当前用户的坐标
     */
    private HashSet<OnLocationListener> listeners = new HashSet<>();
    /**
     * 监听获得附近的其他用户的信息
     */
    private OnNearByLocationListener mNearByListener;

    private Context mContext;
    private SimpleDataBase db;

    private LocationManager(Context context) {
        mContext = context;
        db = new SimpleDataBase(mContext);
        initLocationClient();
        mManager = RadarSearchManager.getInstance();
        //周边雷达设置监听
        mManager.addNearbyInfoListener(this);
        //周边雷达设置用户身份标识，id为空默认是设备标识
        mManager.setUserID(db.getClientId() + "");
    }

    public static LocationManager getInstance(Context context) {
        if (singleInstance == null) {
            synchronized (LocationManager.class) {
                if (singleInstance == null) {
                    singleInstance = new LocationManager(context);
                }
            }
        }

        singleInstance.mContext = context;
        return singleInstance;
    }

    /**
     * 开始定位 每五秒回调一次
     */
    public final void start() {
        if (mLocationClient == null)
            initLocationClient();
        mLocationClient.start();
    }

    /**
     * 停止定位
     */
    public final void stop() {
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient = null;
        }
    }

    /**
     * 马上向定位服务请求要获取位置
     */
    public final void requestRightNow() {
        if (mLocationClient != null)
            mLocationClient.requestLocation();
    }

    /**
     * 初始化百度定位器
     */
    private void initLocationClient() {
        mLocationClient = new LocationClient(mContext.getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        //返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(10000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        //option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {//注册监听函数
            @Override
            public void onReceiveLocation(BDLocation location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                /*上传位置到百度雷达*/
                RadarUploadInfo info = new RadarUploadInfo();
                info.comments = db.getClientId() + "";
                info.pt = new LatLng(latitude, longitude);
                mManager.uploadInfoRequest(info);

                /*把坐标存储到本地*/
                db.putLocationX(latitude);
                db.putLocationY(longitude);

                /*把定位的信息传递到其他监听者*/
                for (OnLocationListener listener : listeners) {
                    if (listener != null)
                        listener.onRecieveLocation(location);
                }
            }
        });
    }

    public final void requestNearByLocation(LatLng pt, OnNearByLocationListener listener) {
        //设置监听器
        mNearByListener = listener;
        //构造请求参数，其中centerPt是自己的位置坐标
        RadarNearbySearchOption option = new RadarNearbySearchOption().centerPt(pt).pageCapacity(10).pageNum(0).radius(db.getRange());
        //发起查询请求
        mManager.nearbyInfoRequest(option);
    }

    public final void addOnLocationListener(OnLocationListener listener) {
        listeners.add(listener);
        //Log.i("zy", "listeners add listener");
    }

    public final void removeOnLocationListener(OnLocationListener listener) {
        boolean success = listeners.remove(listener);
        //Log.i("zy", "listeners remove" + success);
    }

    public void destroy() {
        //停止定位
        stop();
        //移除百度雷达监听
        mManager.removeNearbyInfoListener(this);
        //清除用户信息
        mManager.clearUserInfo();
        mManager.destroy();
        mManager = null;
    }

    @Override
    public void onGetNearbyInfoList(RadarNearbyResult radarNearbyResult, RadarSearchError radarSearchError) {
        if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
            //获取成功，处理数据
            if (mNearByListener != null) mNearByListener.onRevieveNearByLocation(radarNearbyResult);
        } else {
            //获取失败
            Log.e("zy", "获取附近用户信息失败：" + radarSearchError.toString());
        }
    }

    @Override
    public void onGetUploadState(RadarSearchError radarSearchError) {
        if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
            //上传成功
            Log.i("zy", "上传自己到位置到百度雷达成功！");
        } else {
            //上传失败
            Log.e("zy", "上传位置到百度雷达失败：" + radarSearchError.toString());
        }
    }

    @Override
    public void onGetClearInfoState(RadarSearchError radarSearchError) {
        Log.e("zy", radarSearchError.toString());
    }

    public interface OnLocationListener {
        void onRecieveLocation(BDLocation location);
    }

    public interface OnNearByLocationListener {
        void onRevieveNearByLocation(RadarNearbyResult result);
    }
}