package com.ysx.baidu.demo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author ysx
 *         date 2017/10/27
 *         description
 */

public class SnUtils {
    private static final String TAG = "SnUtils";

    /**
     * @param host      前缀 /place/v2/suggestion?
     * @param sk        sk值
     * @param paramsMap 存储参数及值的map
     * @return 获取sn签名
     */
    public static String getSnValue(String host, String sk, Map<String, String> paramsMap) {
        try {
            String paramsStr = toQueryString(paramsMap);
            String wholeStr = new String(host + paramsStr + sk);
            String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
            return MD5(tempStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 对Map内所有value作utf8编码，拼接返回结果
     *
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toQueryString(Map<?, ?> data)
            throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey() + "=");
            queryString.append(URLEncoder.encode((String) pair.getValue(),
                    "UTF-8") + "&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    /**
     * 来自stackoverflow的MD5计算方法
     * 调用了MessageDigest库函数，并把byte数组结果转换成16进制
     *
     * @param md5
     * @return
     */
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
