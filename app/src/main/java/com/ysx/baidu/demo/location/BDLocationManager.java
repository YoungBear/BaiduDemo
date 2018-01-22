package com.ysx.baidu.demo.location;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.ysx.baidu.demo.location.service.LocationService;
import com.ysx.baidu.demo.location.service.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ysx
 */

public class BDLocationManager {

    private static final String TAG = "BDLocationManager";

    private LocationService mLocationService;


    private BDLocation mLocation;

    private boolean isInited;

    private List<LocationCallback> mLocationCallbacks = new LinkedList<>();

    //是否支持室内定位
    private boolean isIndoorLocationSupport = false;

    private BDLocationManager() {

    }

    public static BDLocationManager getInstance() {
        return BDLocationManagerHolder.sInstance;
    }

    private static class BDLocationManagerHolder {
        private static final BDLocationManager sInstance = new BDLocationManager();

    }

    public void init(Context context) {
        if (!isInited) {
            mLocationService = new LocationService(context.getApplicationContext());
            mLocationService.setLocationOption(getBDLocationOption());
            isInited = true;
        }

    }

    public void startLocation() {
        if (isInited) {
            mLocationService.registerListener(mBDLocationListener);
            mLocationService.start();
        }
    }

    public void stopLocation() {
        if (isInited) {
            mLocationService.stop();
            mLocationService.unregisterListener(mBDLocationListener);

            //clear callbacks
            mLocationCallbacks.clear();
        }
    }


    private BDAbstractLocationListener mBDLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.d(TAG, "onReceiveLocation: bdLocation: " + bdLocation);
            if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
                mLocation = bdLocation;
                isIndoorLocationSupport =
                        mLocation.getIndoorLocationSurpport() == BDLocation.INDOOR_LOCATION_SURPPORT_TRUE;
                for (LocationCallback item : mLocationCallbacks) {
                    item.onReceiveLocation(bdLocation);
                }
            }
        }
    };

    /**
     * 配置定位参数
     */
    private LocationClientOption getBDLocationOption() {
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //高精度
        locOption.setCoorType(Utils.CoorType_BD09LL); //使用百度经纬度坐标，与地图坐标类型保持一致
        locOption.setOpenGps(true); // 打开GPS定位
        locOption.setScanSpan(2000); //定位间隔，2000ms，必须大于1000ms才有效
        locOption.setIsNeedAddress(true); //需要地址信息
        locOption.setIsNeedLocationDescribe(true); //需要地址描述
        locOption.setNeedDeviceDirect(true); //需要设备方向结果
        locOption.setLocationNotify(true); //设置当gps有效时按照1秒1次频率输出GPS结果
        locOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

        locOption.setIsNeedAltitude(true);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locOption.disableCache(true);//禁止启用缓存定位
        return locOption;
    }


    public BDLocation getLocation() {
        return mLocation;
    }

    public void addLocationCallback(LocationCallback callback) {
        if (!mLocationCallbacks.contains(callback)) {
            mLocationCallbacks.add(callback);
        }
    }

    public void removeLocationCallback(LocationCallback callback) {
        if (mLocationCallbacks.contains(callback)) {
            mLocationCallbacks.remove(callback);
        }
    }

    public boolean isIndoorLocationSupport() {
        return isIndoorLocationSupport;
    }

    public boolean startIndoorMode() {
        if (isInited) {
            return mLocationService.startIndoorMode();
        }
        return false;
    }

    public interface LocationCallback {
        void onReceiveLocation(BDLocation location);
    }

}
