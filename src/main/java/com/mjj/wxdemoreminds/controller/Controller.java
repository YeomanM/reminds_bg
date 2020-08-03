package com.mjj.wxdemoreminds.controller;

import com.mjj.wxdemoreminds.entity.Reminds;
import com.mjj.wxdemoreminds.model.BaseResponse;
import com.mjj.wxdemoreminds.service.Service;
import com.mjj.wxdemoreminds.util.CacheUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/reminds")
public class Controller {

    @Autowired
    Service service;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/test")
    public BaseResponse test() {
        return BaseResponse.success();
    }


    @PostMapping("/save")
    public BaseResponse save(@RequestBody Reminds reminds) throws SchedulerException {
        reminds.setOpenId(this.getOpenId());
        return service.save(reminds);
    }

    @GetMapping
    public BaseResponse list() {
        String openId = this.getOpenId();
        System.out.println(openId);
        return service.list(openId);
    }

    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable Long id) throws SchedulerException {
        return service.delete(id);
    }


    private String getOpenId() {
        String sign = request.getHeader("sign");
        return (String) CacheUtil.get(sign);
    }

}
