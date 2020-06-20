package com.mjj.wxdemoreminds.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {

    private int errorCode;
    private String errorMsg;
    private Object data;

    public final static int SUCCESS_CODE = 0;
    public final static int DEFAULT_ERROR_CODE = 500;
    public final static String DEFAULT_ERROR_MSG = "Handle Error!";

    public BaseResponse(){}

    public BaseResponse(int errorCde, String errorMsg, Object data) {
        this.errorCode = errorCde;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public static BaseResponse success() {
        return new BaseResponse(SUCCESS_CODE, "", null);
    }

    public static BaseResponse success(Object errorMsg) {
        return new BaseResponse(SUCCESS_CODE, "", errorMsg);
    }

    public static BaseResponse success(String errorMsg, Object data) {
        return new BaseResponse(SUCCESS_CODE, errorMsg, data);
    }

    public static BaseResponse fail() {
        return new BaseResponse(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG, null);
    }

    public static BaseResponse fail(int errorCode) {
        return new BaseResponse(errorCode, DEFAULT_ERROR_MSG, null);
    }

    public static BaseResponse fail(String errorMsg) {
        return new BaseResponse(DEFAULT_ERROR_CODE, errorMsg, null);
    }

    public static BaseResponse fail(int errorCode, String errorMsg) {
        return new BaseResponse(errorCode, errorMsg, null);
    }



}
