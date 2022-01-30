package com.service.microjc.stType;

public class SecurityContent {
    private String aesKey;//经过rsa加密的aes密钥
    private String aesContent;//aes加密内容

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getAesContent() {
        return aesContent;
    }

    public void setAesContent(String aesContent) {
        this.aesContent = aesContent;
    }
}
