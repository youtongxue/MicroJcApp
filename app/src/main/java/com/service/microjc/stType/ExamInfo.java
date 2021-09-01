package com.service.microjc.stType;

import java.util.List;

/**
 * @Date 2021-8-7
 * @Author 游同学
 * */
public class ExamInfo {
    //定义一个集合存放课程成绩
    public List<Info> infos;

    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }

    public static class Info{
        //   !!! 这里也有个坑，Spring boot后端实体类变量名是首字母大写，而被序列化传给前端，大部分变量名都会变成小写

        //考试信息实体类
        private String time;//考试时间
        private String id;//课程编号
        private String place;//考试教室
        private String how;//考试方式
        private String where;//考试校区
        private String name;//考试名字
        private String number;//座位号
        private String studentName;//参考同学名字

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getHow() {
            return how;
        }

        public void setHow(String how) {
            this.how = how;
        }

        public String getWhere() {
            return where;
        }

        public void setWhere(String where) {
            this.where = where;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }
    }

}
