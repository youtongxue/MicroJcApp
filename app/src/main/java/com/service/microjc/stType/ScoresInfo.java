package com.service.microjc.stType;

import java.util.List;

public class ScoresInfo {

    //定义一个集合存放课程成绩
    //这里不能将变量设置为static
    public List<Info> infos;


    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }

    //每一个Info对象都是一个课程成绩，信息
    public static class Info{
        private String xn;//学年
        private String xq;//学期
        private String code;//课程代码
        private String name;//课程名字
        private String type;//课程性质
        private String credit;//学分
        private String GPA;//绩点
        private String score;//成绩
        private String Minor;//辅修
        private String bk_cord;//补考成绩
        private String bkjm_cord;//卷面成绩
        private String cx_cord;//重修成绩
        private String xy;//开课学院
        private String bz;//备注
        private String cxbj;//重修标记
        private String specialities;//专业

        public String getXn() {
            return xn;
        }

        public void setXn(String xn) {
            this.xn = xn;
        }

        public String getXq() {
            return xq;
        }

        public void setXq(String xq) {
            this.xq = xq;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getGPA() {
            return GPA;
        }

        public void setGPA(String GPA) {
            this.GPA = GPA;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getMinor() {
            return Minor;
        }

        public void setMinor(String minor) {
            Minor = minor;
        }

        public String getBk_cord() {
            return bk_cord;
        }

        public void setBk_cord(String bk_cord) {
            this.bk_cord = bk_cord;
        }

        public String getBkjm_cord() {
            return bkjm_cord;
        }

        public void setBkjm_cord(String bkjm_cord) {
            this.bkjm_cord = bkjm_cord;
        }

        public String getCx_cord() {
            return cx_cord;
        }

        public void setCx_cord(String cx_cord) {
            this.cx_cord = cx_cord;
        }

        public String getXy() {
            return xy;
        }

        public void setXy(String xy) {
            this.xy = xy;
        }

        public String getBz() {
            return bz;
        }

        public void setBz(String bz) {
            this.bz = bz;
        }

        public String getCxbj() {
            return cxbj;
        }

        public void setCxbj(String cxbj) {
            this.cxbj = cxbj;
        }

        public String getSpecialities() {
            return specialities;
        }

        public void setSpecialities(String specialities) {
            this.specialities = specialities;
        }
    }


}
