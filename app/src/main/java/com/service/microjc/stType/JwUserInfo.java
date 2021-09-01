package com.service.microjc.stType;

import java.io.Serializable;

public class JwUserInfo implements Serializable {
    private String loginStatus;//服务器返回的登录状态
    private String studentName;//姓名
    private String userId;//用户学号
    private String department;//学院
    private String specialities;//专业
    private String userClass;//班级

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String specialities) {
        this.specialities = specialities;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }
}
