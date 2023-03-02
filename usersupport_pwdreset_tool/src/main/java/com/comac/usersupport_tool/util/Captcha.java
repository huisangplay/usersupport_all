package com.comac.usersupport_tool.util;

public class Captcha {

    public static String key="zhouhui500958";

    private String code;
    private Long createTime;
    private String number;
    private String digest;

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getNumber() {
        return number;
    }

    public String getCode() {
        return code;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Captcha(String number,String code) {
        this.code=code;
        this.createTime = System.currentTimeMillis();
        this.number = number;
        try {
            this.digest=SHA256.hashV2(number+code+createTime+key);
        } catch (Exception e) {
            this.digest="";
        }
    }

    public String toString(){
        return number+";"+code+";"+createTime+";"+digest;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
