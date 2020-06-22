package com.mjj.wxdemoreminds.controller;

import com.mjj.wxdemoreminds.entity.Reminds;
import com.mjj.wxdemoreminds.model.BaseResponse;
import com.mjj.wxdemoreminds.service.Service;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reminds")
public class Controller {

    @Autowired
    Service service;

    @GetMapping("/test")
    public BaseResponse test() {
        return BaseResponse.success();
    }


    @PostMapping("/save")
    public BaseResponse save(@RequestBody Reminds reminds) throws SchedulerException {
        return service.save(reminds);
    }

    @GetMapping
    public BaseResponse list() {
//        return BaseResponse.success(remindsRepository.findAll(Sort.by("id").descending()));
        return service.list();
    }

    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable Long id) throws SchedulerException {
        return service.delete(id);
    }

}
