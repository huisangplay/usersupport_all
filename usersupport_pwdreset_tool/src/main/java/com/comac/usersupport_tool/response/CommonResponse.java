package com.comac.usersupport_tool.response;

public class CommonResponse {
    public Integer code;
    public String message;
    public CommonResponse(Integer code,String message){
        this.code=code;
        this.message=message;
    }
}
