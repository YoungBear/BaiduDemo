package com.ysx.baidu.demo.bean;

import java.util.List;

/**
 * @author ysx
 * date 2017/10/25
 * description 百度地理搜索范湖结果实体类
 */

public class BaiduPlaceSugBean {


    /**
     * status : 0
     * message : ok
     * result : [{"name":"中兴购物商场","location":{"lat":25.026858,"lng":102.729786},"uid":"d5464cf879d1c52afda7783c","city":"昆明市","district":"官渡区","business":"","cityid":"104"},{"name":"中兴手机授权客户中心","location":{"lat":25.052923,"lng":102.701618},"uid":"2132025583ff8717a11de90b","city":"昆明市","district":"五华区","business":"","cityid":"104"},{"name":"中兴","location":{"lat":25.032571,"lng":102.697139},"uid":"3fabbd5f0797fdf23ce231d0","city":"昆明市","district":"西山区","business":"","cityid":"104"},{"name":"中信银行(北辰支行)","location":{"lat":25.091893,"lng":102.742991},"uid":"34059f97b335455571455bf0","city":"昆明市","district":"盘龙区","business":"","cityid":"104"},{"name":"中兴通讯公司昆明办事处","location":{"lat":25.043212,"lng":102.719125},"uid":"0440a545ffd7bfe928b97c36","city":"昆明市","district":"五华区","business":"","cityid":"104"},{"name":"中兴","location":{"lat":25.152164,"lng":102.743127},"uid":"62d4d548b30a26042b3a9f24","city":"昆明市","district":"盘龙区","business":"","cityid":"104"},{"name":"中兴售后","location":{"lat":25.047623,"lng":102.739524},"uid":"260e101eb8114b27d82f2891","city":"昆明市","district":"盘龙区","business":"","cityid":"104"},{"name":"中信银行(昆明滇池支行)","location":{"lat":25.023174,"lng":102.714772},"uid":"f60a15eddd1baeedbb17e7cf","city":"昆明市","district":"西山区","business":"","cityid":"104"},{"name":"中兴","location":{"lat":25.095496,"lng":102.652675},"uid":"8f7f8995ab0a6781b13c8152","city":"昆明市","district":"五华区","business":"","cityid":"104"},{"name":"中兴北路-道路","location":{"lat":25.034682,"lng":103.667735},"uid":"43ae6d0eb10d0c7303897215","city":"曲靖市","district":"陆良县","business":"","cityid":"249"}]
     */

    private int status;
    private String message;
    private List<ResultBean> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * name : 中兴购物商场
         * location : {"lat":25.026858,"lng":102.729786}
         * uid : d5464cf879d1c52afda7783c
         * city : 昆明市
         * district : 官渡区
         * business :
         * cityid : 104
         */

        private String name;
        private LocationBean location;
        private String uid;
        private String city;
        private String district;
        private String business;
        private String cityid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }

        public static class LocationBean {
            /**
             * lat : 25.026858
             * lng : 102.729786
             */

            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }
    }
}
