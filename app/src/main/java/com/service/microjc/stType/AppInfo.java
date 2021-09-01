package com.service.microjc.stType;

public class AppInfo {
    private float version;//存放服务器软件版本号
    private String upDataContent;//存放当前版本更新内容

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getUpDataContent() {
        return upDataContent;
    }

    public void setUpDataContent(String upDataContent) {
        this.upDataContent = upDataContent;
    }
}
