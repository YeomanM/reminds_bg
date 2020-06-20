package com.mjj.wxdemoreminds.controller;

import com.mjj.wxdemoreminds.model.BaseResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResponse error(Exception e) {
        e.printStackTrace();
        return BaseResponse.fail();
    }

}
