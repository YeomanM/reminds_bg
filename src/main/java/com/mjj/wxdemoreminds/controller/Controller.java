package com.mjj.wxdemoreminds.controller;

import com.mjj.wxdemoreminds.entity.Reminds;
import com.mjj.wxdemoreminds.model.BaseResponse;
import com.mjj.wxdemoreminds.repository.RemindsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reminds")
public class Controller {

    @Autowired RemindsRepository remindsRepository;

    @GetMapping("/test")
    public BaseResponse test() {
        return BaseResponse.success();
    }


    @PostMapping("/save")
    public BaseResponse save(@RequestBody Reminds reminds) {
        return BaseResponse.success(remindsRepository.save(reminds));
    }

    @GetMapping
    public BaseResponse list() {
//        return BaseResponse.success(remindsRepository.findAll(Sort.by("id").descending()));
        return BaseResponse.success(remindsRepository.findRemindsByOpenIdOrderByIdDesc(""));
    }

    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        remindsRepository.deleteById(id);
        return BaseResponse.success();
    }

}
