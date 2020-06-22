package com.mjj.wxdemoreminds.controller;

import com.mjj.wxdemoreminds.model.BaseResponse;
import com.mjj.wxdemoreminds.util.HttpAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authc")
public class AuthcController {

    @Value("${wx.appId}")
    private String appid;

    @Value("${wx.appSecret}")
    private String appSecret;



    @GetMapping("/{code}")
    public BaseResponse authc(@PathVariable String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
        url = String.format(url, appid, appSecret, code);
        System.out.println(url);
        try {
            String data = HttpAccessor.doAttemptHttpGet(url);
            System.out.println(data);
            return BaseResponse.success(code);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.fail(e.getLocalizedMessage());
        }
    }

}
