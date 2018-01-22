package com.ysx.baidu.demo.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;

/**
 * @author ysx
 * @date 2018/1/22
 * @description 百度经纬度坐标转换
 * 百度官方手册：http://lbsyun.baidu.com/index.php?title=androidsdk/guide/tool/coordinate
 */

public class ConverterUtils {

    /**
     * 将GPS设备采集的原始GPS坐标转换成百度经纬度坐标
     * 即GPS坐标转换为BD09LL坐标
     *
     * @param sourceLatLng
     * @return
     */
    public static LatLng GPStoBD09LL(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordType.GPS);
        converter.coord(sourceLatLng);
        return converter.convert();
    }

    /**
     * 将火星坐标系坐标转换成百度经纬度坐标
     * 即GCJ02坐标转换为BD09LL坐标
     *
     * @param sourceLatLng
     * @return
     */
    public static LatLng GCJ02toBD09LL(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordType.COMMON);
        converter.coord(sourceLatLng);
        return converter.convert();
    }

    /**
     * 将百度墨卡托米制坐标系坐标转换成百度经纬度坐标
     * 即BD09MC转换成BD09LL
     *
     * @param sourceLatLng
     * @return
     */
    public static LatLng BD09MCtoBD09LL(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordType.BD09MC);
        converter.coord(sourceLatLng);
        return converter.convert();
    }

    /**
     * 将百度经纬度坐标转换成火星坐标系坐标
     * 即BD09LL坐标转换成GCJ02坐标
     *
     * @param sourceLatLng
     * @return
     */
    public static LatLng BD09LLtoGCJ02(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordType.BD09LL);
        converter.coord(sourceLatLng);
        return converter.convert();
    }
}
