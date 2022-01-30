package com.service.microjc.stType;

public class AppUserInfo {
    private String openID;//
    private int studentID;//
    private String jwwPass;//
    private String tsgPass;//
    private String yktPass;//
    private String chatID;//

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getJwwPass() {
        return jwwPass;
    }

    public void setJwwPass(String jwwPass) {
        this.jwwPass = jwwPass;
    }

    public String getTsgPass() {
        return tsgPass;
    }

    public void setTsgPass(String tsgPass) {
        this.tsgPass = tsgPass;
    }

    public String getYktPass() {
        return yktPass;
    }

    public void setYktPass(String yktPass) {
        this.yktPass = yktPass;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
}
