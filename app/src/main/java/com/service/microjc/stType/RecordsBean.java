package com.service.microjc.stType;

import java.util.List;

public class RecordsBean {
    //创建Bean类用于接收，JSon数据
    private List<recordsInfosBean> recordsInfos;

    public List<recordsInfosBean> getRecordsInfos() {
        return recordsInfos;
    }

    public void setRecordsInfos(List<recordsInfosBean> recordsInfos) {
        this.recordsInfos = recordsInfos;
    }

    public static class recordsInfosBean {
        /**
         *              "type": "微信转入",
         *             "time": "2021-07-01 12:34:42",
         *             "spentMoney": "100.00",
         *             "surplusMoney": "105.58",
         *             "returnMoney": null,
         *             "machine": "01-05-901-四川大学锦城学院",
         *             "shop": "微信充值"
         * */
        //变量名跟JSON数据的字段名需要一致
        private String type ;
        private String time;
        private String spentMoney;
        private String surplusMoney;
        private String returnMoney;
        private String machine;
        private String shop;
        private String shopImage;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSpentMoney() {
            return spentMoney;
        }

        public void setSpentMoney(String spentMoney) {
            this.spentMoney = spentMoney;
        }

        public String getSurplusMoney() {
            return surplusMoney;
        }

        public void setSurplusMoney(String surplusMoney) {
            this.surplusMoney = surplusMoney;
        }

        public String getReturnMoney() {
            return returnMoney;
        }

        public void setReturnMoney(String returnMoney) {
            this.returnMoney = returnMoney;
        }

        public String getMachine() {
            return machine;
        }

        public void setMachine(String machine) {
            this.machine = machine;
        }

        public String getShop() {
            return shop;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }

        public String getShopImage() {
            return shopImage;
        }

        public void setShopImage(String shopImage) {
            this.shopImage = shopImage;
        }
    }

}

