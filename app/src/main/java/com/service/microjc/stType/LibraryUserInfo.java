package com.service.microjc.stType;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


/**
 * 创建LibraryUserInfo 对象用于接收json数据，需要和后端返回的json匹配
 */
public class LibraryUserInfo implements Serializable{
    private String loginStatus;//登录状态
    private String studentName;//姓名
    private String studentId;//学号
    private String password;//密码
    private String faculty;//院系
    private String phoneNumber;//手机号
    private String borrowed;//已借本数
    private String available;//可借本数
    private String overtime;//超时本数
    public static List<BooksInfo> booksData = new ArrayList<>();//借阅书籍详情

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(String borrowed) {
        this.borrowed = borrowed;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public List<BooksInfo> getBooksData() {
        return booksData;
    }

    public void setBooksData(List<BooksInfo> booksData) {
        LibraryUserInfo.booksData = booksData;
    }

    public class BooksInfo {
        private String name;//书籍名称
        private String borrowingTime;//借阅时间
        private String returnTime;//归还时间

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBorrowingTime() {
            return borrowingTime;
        }

        public void setBorrowingTime(String borrowingTime) {
            this.borrowingTime = borrowingTime;
        }

        public String getReturnTime() {
            return returnTime;
        }

        public void setReturnTime(String returnTime) {
            this.returnTime = returnTime;
        }
    }

    @Override
    public String toString() {
        return "LibraryUserInfo{" +
                "studentName='" + studentName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", password='" + password + '\'' +
                ", faculty='" + faculty + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", borrowed='" + borrowed + '\'' +
                ", available='" + available + '\'' +
                ", overtime='" + overtime + '\'' +
                '}';
    }
}
