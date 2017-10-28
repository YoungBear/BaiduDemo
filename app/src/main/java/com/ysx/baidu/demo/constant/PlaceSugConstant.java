package com.ysx.baidu.demo.constant;

/**
 * @author ysx
 *         date 2017/10/27
 *         description
 *         百度地理位置搜索 Place Suggestion API
 *         参考: http://lbsyun.baidu.com/index.php?title=webapi/place-suggestion-api
 */

public final class PlaceSugConstant {

    /**
     * host
     */
    public static final String BAIDU_PLACE_SUG_HOST =
            "http://api.map.baidu.com/place/v2/suggestion";

    /**
     * 计算sn时，由于我们使用的是placeSug API，则前缀为"/place/v2/suggestion?"
     */
    public static final String SN_HOST = "/place/v2/suggestion?";

    // 搜索时需要传递的值的key
    /**
     * q: 关键词
     * 必传
     * 上地、天安、中关、shanghai
     * 输入建议关键字（支持全拼检索）
     */
    public static final String KEY_KEYWORD = "q";

    /**
     * region: 区域
     * 必传
     * 全国、北京市、131、江苏省等
     * 支持全国、省、城市及对应百度编码（citycode）。
     * （指定的区域的返回结果加权，可能返回其他城市高权重结果。
     * 若要对返回结果区域严格限制，请使用city_limit参数）
     */
    public static final String KEY_REGION = "region";

    /**
     * city_limit: 区域限制
     * "true" or "false"
     * 可选参数，默认为false，即不限制区域
     * 取值为"true"，仅返回region中指定城市检索结果
     */
    public static final String KEY_CITY_LIMIT = "city_limit";

    /**
     * location: 位置
     * "40.047857537164,116.31353434477"
     * 可选参数
     * 传入location参数后，返回结果将以距离进行排序
     */
    public static final String KEY_LOCATION = "location";

    /**
     * ret_coordtype: 坐标类型
     * gcj02ll
     * 可选参数，默认为百度经纬度坐标 bd09ll
     * 添加后POI返回国测局经纬度坐标
     */
    public static final String KEY_RET_COORDTYPE = "ret_coordtype";

    /**
     * output: 返回数据类型
     * xml 或者 json
     * 可选参数，默认为 xml
     */
    public static final String KEY_OUTPUT = "output";

    /**
     * ak: 开发者秘钥，必选
     */
    public static final String KEY_AK = "ak";

    /**
     * mcode: 安全码
     * app端必传
     * mcode在ak结果页面显示，格式为<SHA1>;<package_name>
     */
    public static final String KEY_MCODE = "mcode";

    /**
     * sn值
     */
    public static final String KEY_SN = "sn";

    /**
     * 时间戳
     */
    public static final String KEY_TIME_STAMP = "timestamp";


    // 搜索时需要传递的值的取值常量

    /**
     * region取值
     * 因为是云南产品，所以区域优先设置为云南
     */
    public static final String VALUE_REGION = "全国";

    /**
     * city_limit的取值
     * true表示要对返回结果区域严格限制
     * false要对返回结果不限制区域
     */
    public static final String VALUE_CITY_LIMIT_TRUE = "true";
    public static final String VALUE_CITY_LIMIT_FALSE = "false";

    /**
     * ret_coordtype的取值，gcj02ll表示国测局经纬度坐标
     */
    public static final String VALUE_RET_COORDTYPE = "gcj02ll";

    /**
     * output的取值，表示返回数据格式
     */
    public static final String VALUE_OUTPUT_XML = "xml";
    public static final String VALUE_OUTPUT_JSON = "json";

    /**
     * 开发者秘钥取值，开发者申请取得
     * // TODO: 2017/10/26 在配置文件存放，不要放在代码中
     */
    public static final String VALUE_AK = "7TOlbqUrZlvfGeLXQVexGthjdTHzwQqf";

    /**
     * 开发者MCode
     * 这只是debug的安全码，release的不一样
     * // TODO: 2017/10/27 需要区分debug和release的安全码
     * 可以采用manifestPlaceholders 或者 BuildConfig
     */
    public static final String VALUE_MCODE = "84:3D:AA:CE:CB:9C:8E:BA:EC:87:AE:43:A2:DC:8E:72:3A:1B:49:A0;com.ysx.baidu.demo";

    /**
     * 申请的ak值所对应的sk值
     * // TODO: 2017/10/28 将该值写入配置文件
     * 可以采用manifestPlaceholders 或者 BuildConfig 动态设置
     */
    public static final String VALUE_SK = "BlpGvFS7vyF9LuW3i5ssqAEaQeq4Qlp8";

}
