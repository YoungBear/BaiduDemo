# BaiduMap SDK 学习

## 1. 关于Android端AK值申请及签名配置

[官方地址](http://lbsyun.baidu.com/apiconsole/key/create)

申请ak：

![](https://github.com/YoungBear/MyBlog/blob/master/pngs/baidu/baidu_ak_apply_android.png)

其中，百度api-key是与签名文件和包名绑定的，所以我们需要先获取**签名文件**，签名文件与SHA1值对应。我们需要输入**开发板SHA1**和**发布版SHA1**。

默认在Debug模式下，我们的签名文件是`~/.android/debug.keystore`，即用户目录下的`.android/debug.keystore`文件，其密码默认都为`android`。可以使用如下命令来获取签名文件的SHA1值。

```
keytool -list -v -keystore <签名文件>
```

输入密码后，可以查看结果：

查看debug签名文件的SHA1值：

![](https://github.com/YoungBear/MyBlog/blob/master/pngs/baidu/get_sha1_debug.png)

查看release签名文件的SHA1值：

![](https://github.com/YoungBear/MyBlog/blob/master/pngs/baidu/get_sha1_release.png)




其中，release签名文件可以用Android Studio来申请获得，用户需要输入文件名，密码，别名，别名密码。

通过`Build->Genetate Signed APK...->Next->Create New...`进入。

生成新的签名文件：

![](https://github.com/YoungBear/MyBlog/blob/master/pngs/baidu/as_apply_sign_file.png)

### 配置签名文件

在app目录下的build.gradle文件:

```
android {
    compileSdkVersion 26
    defaultConfig {
        // 其他配置
    }

    // 签名配置
    signingConfigs {
        debug {
            storeFile file('./debug.keystore')
            storePassword 'android'
            keyAlias 'androiddebugkey'
            keyPassword 'android'
        }
        release {
            storeFile file('<签名文件>')
            storePassword '<签名密码>'
            keyAlias '<别名>'
            keyPassword '<别名密码>'
        }
    }

    buildTypes {
        debug {
            // 其他配置
            signingConfig signingConfigs.debug //使用debug签名
        }
        release {
            // 其他配置
            signingConfig signingConfigs.release // 使用release签名
        }
    }

    // jni库
    sourceSets {
        main {
            jniLibs.srcDir 'libs'
        }
    }

}
```
一般情况下，签名文件及密码这些敏感信息，是不建议直接放在gradle代码里的，而应该建立一个配置文件`config.properties`，然后将该配置文件加到`.gitignore`文件中，即不在git库保存，放在安全的地方。下面有一节专门讲这个配置。

### 使用配置文件存放敏感信息

可以在app目录下建立一个配置文件`config.properties`，在里边存放签名文件及密码等敏感信息。

![](https://github.com/YoungBear/MyBlog/blob/master/pngs/baidu/config_preoperties.png)

```
# 配置文件 app/config.properties

# debug
DEBUG_STORE_FILE = ./debug.keystore
DEBUG_STORE_PASSWORD = android
DEBUG_KEY_ALIAS = androiddebugkey
DEBUG_KEY_PASSWORD = android

# release
RELEASE_STORE_FILE = ****
RELEASE_STORE_PASSWORD = ****
RELEASE_KEY_ALIAS = ****
RELEASE_KEY_PASSWORD = ****

```

在app目录下的build.gradle使用配置文件：

```
// 加载配置文件
Properties props = new Properties()
FileInputStream fis = new FileInputStream(file("./config.properties"))
BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
props.load(bf)

android {
    // 签名配置
    signingConfigs {
        debug {
            storeFile file(props['DEBUG_STORE_FILE'])
            storePassword props['DEBUG_STORE_PASSWORD']
            keyAlias props['DEBUG_KEY_ALIAS']
            keyPassword props['DEBUG_KEY_PASSWORD']
        }
        release {
            storeFile file(props['RELEASE_STORE_FILE'])
            storePassword props['RELEASE_STORE_PASSWORD']
            keyAlias props['RELEASE_KEY_ALIAS']
            keyPassword props['RELEASE_KEY_PASSWORD']
        }
    }

    // 其他配置

}
```

先加载配置文件，然后使用`props['键']`来获取值。

另外，也可以使用`manifestPlaceholders`来配置不同的**渠道号**等信息。后续专门写一节来具体描述。

## 2. 在AndroidManifest文件中配置AK值

在Application标签下，配置mata-data值：

```
        <meta-data
            android:name="com.baidu.lbsapi.API_KEY"
            android:value="<你申请的ak值>" />
```

## 3. 权限申请

百度地图及定位所需要的权限列表。

```
    <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" />
    <!-- 这个权限用于进行网络定位 -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <!-- 这个权限用于访问GPS定位 -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <!-- 用于访问wifi网络信息，wifi信息会用于进行网络定位 -->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <!-- 获取运营商信息，用于支持提供运营商信息相关的接口 -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <!-- 用于读取手机当前的状态 -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <!-- 写入扩展存储，向扩展卡写入数据，用于写入离线定位数据 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!-- 访问网络，网络定位需要上网 -->
    <uses-permission android:name="android.permission.INTERNET" />
```

其中，在Android 6.0之后，即如果`compileSdkVersion >= 23`，危险权限需要**动态申请**。

在百度地图开发中，需要在获取到权限之后，初始化地图SDK：

```
SDKInitializer.initialize(getApplicationContext());
SDKInitializer.setCoordType(CoordType.BD09LL);
```

**需要注意的是**，这个初始化必须在含有MapView的Activity的setContentView()**之前**，否则会因为没有初始化而导致崩溃。

## 4. 百度定位配置Service

如果使用百度定位，需要在AndroidManifest文件的Application标签下配置service标签：

```
        <service
            android:name="com.baidu.location.f"
            android:enabled="true"
            android:process=":remote" />
```

## 5. 添加混淆配置

百度地图相关混淆配置：

```
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
```

## 6. 百度 Place Suggestion API

由于最新版的百度SDK(2017-10-27)，在Android SDK 已经不再支持**地点输入提示服务**，所以如果我们需要模糊搜索的时候，需要单独申请服务端应用。

原有申请的Android SDK，可以继续使用**Place API v2**。

Android已经没有 **Place Api v2**

![](https://github.com/YoungBear/MyBlog/blob/master/pngs/baidu/baidu_ak_apply_android_1.png)

Server应用有 **Place Api v2**

![](https://github.com/YoungBear/MyBlog/blob/master/pngs/baidu/baidu_ak_apply_server.png)



[官方文档](http://lbsyun.baidu.com/index.php?title=webapi/place-suggestion-api)

访问URL：

eg.

```
http://api.map.baidu.com/place/v2/suggestion?query=天安门&region=北京&city_limit=true&output=json&ak=你的ak //GET请求
```

具体要求请查看[官方文档](http://lbsyun.baidu.com/index.php?title=webapi/place-suggestion-api)。


服务器端有两种校验方式：

- IP白名单校验
- sn校验


### IP白名单校验
只有IP白名单内的服务器才能成功发起调用。

格式:

```
202.198.16.3,202.198.0.0/16 
```

填写**IP地址**或**IP前缀网段**，**英文半角逗号**分隔。如果不想对IP做任何限制，请设置为**0.0.0.0/0** （谨慎使用，AK如果泄露配额会被其用户消费，上线前可以用作Debug，线上正式ak请设置合理的IP白名单）

### sn校验

申请获取到的server应用的ak值及sk值：

![](https://github.com/YoungBear/MyBlog/blob/master/pngs/baidu/baidu_ak_apply_server_result.png)

[官方sn说明文档](http://lbsyun.baidu.com/index.php?title=lbscloud/api/appendix)

官方是以前缀`/geocoder/v2/?`为例的，由于我们需要使用的是地点输入提示服务，所以我们需要将前缀改为`/place/v2/suggestion?`。

```
http://api.map.baidu.com/place/v2/suggestion?q=丽江&region=全国&output=json&ak=your_ak&timestamp=1509167078641&sn=7de5a22212ffaa9e326444c75a58f9a0
／／后面的sn就是要计算的，sk不需要在url里出现，但是在计算sn的时候需要sk（假设sk=yoursk）
```

所以，我们简单说一下sn的生成方法：


这个是工具类SnUtils，用来生成sn。

```
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
```

实际调用demo：

需要注意的是，sn字段需要放在最后。

官方说明：

计算sn跟参数对出现顺序有关，get请求请使用LinkedHashMap保存<key,value>，该方法根据key的插入顺序排序；post请使用TreeMap保存<key,value>，该方法会自动将key按照字母a-z顺序排序。所以get请求可自定义参数顺序（**sn参数必须在最后**）发送请求，但是post请求必须按照字母a-z顺序填充body（sn参数必须在最后）。以get请求为例：

`http://api.map.baidu.com/geocoder/v2/?address=百度大厦&output=json&ak=yourak`

paramsMap中先放入address，再放output，然后放ak，放入顺序必须跟get请求中对应参数的出现顺序保持一致。

```
public class BaiduTest {
    public static void main(String[] args) {
        Map<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put("q", "丽江");
        paramsMap.put("region", "全国");

        paramsMap.put("output", "json");
        paramsMap.put("ak", "your_ak");
        paramsMap.put("timestamp", String.valueOf(System.currentTimeMillis()));

        String sn = SnUtils.getSnValue("/place/v2/suggestion?", "your_sk", paramsMap);
        /**
         * sn值要放在最后
         */
        paramsMap.put("sn", sn);

        String url = null;
        try {
            url = "http://api.map.baidu.com/place/v2/suggestion" + "?" + SnUtils.toQueryString(paramsMap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("url: " + url);
    }
}
```

### Android代码实现

说明：

首页是地图页面，会调用百度定位，成功后，地图会将当前位置移动到屏幕中心。

搜索：

点击搜索后，可以进行模糊搜索，即调用Place Sug API，返回一个结果列表，其中包含经纬度信息，点击item的时候，将finish掉这个SearchActivity，并将item的经纬度信息回传给地图Activity。

核心代码为：

设置item监听:

```
    mPlaceSugAdapter.setOnItemClickListener(new PlaceSugAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "Result onItemClick, position: " + position);
                backToMapActivity(mData.get(position));
            }
        });
```

退出并返回地图Activity：

```
    /**
     * @param resultBean 百度位置信息
     *                   返回到MapActivity
     */
    private void backToMapActivity(BaiduPlaceSugBean.ResultBean resultBean) {
        Intent data = new Intent();
        data.putExtra(SearchPlaceConstant.EXTRA_LATITUDE, resultBean.getLocation().getLat());
        data.putExtra(SearchPlaceConstant.EXTRA_LONGITUDE, resultBean.getLocation().getLng());
        setResult(Activity.RESULT_OK, data);
        Log.d(TAG, "backToGisActivity:"
                + "\nlatitude: " + resultBean.getLocation().getLat()
                + "\nlongitude: " + resultBean.getLocation().getLng());

        finish();
    }
```

startActivityForResult的回调方法：

获取传递来的经纬度，然后地图定位到该位置。并用Marker标出。

```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SEARCH) {
            if (resultCode == Activity.RESULT_OK) {
                double latitude = data.getDoubleExtra(SearchPlaceConstant.EXTRA_LATITUDE, 0D);
                double longitude = data.getDoubleExtra(SearchPlaceConstant.EXTRA_LONGITUDE, 0D);
                LatLng latLng = new LatLng(latitude, longitude);

                Log.d(TAG, "onActivityResult: mLatLng: " + latLng);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(latLng);
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
                mBaiduMap.setMapStatus(mapStatusUpdate);
                if (mSearchBd == null) {
                    mSearchBd = BitmapDescriptorFactory.fromResource(R.drawable.ic_location);
                }
                MarkerOptions option = new MarkerOptions().icon(mSearchBd).position(latLng);
                mBaiduMap.addOverlay(option);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
```

![地址输入提示服务demo](https://github.com/YoungBear/MyBlog/blob/master/pngs/baidu/baidu_place_sug_demo_360_640.gif)


## [Demo地址](https://github.com/YoungBear/BaiduDemo)

## Tips：

1. 该demo在master分支上忽略了配置文件app/config.properties，如果您想查看配置文件，可以切换到config_dev分支。




## 参考：

http://blog.csdn.net/yujihu989/article/details/54589684


